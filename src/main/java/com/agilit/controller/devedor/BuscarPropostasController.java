package com.agilit.controller.devedor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.PropostaEmprestimo;
import com.agilit.util.CalculadoraEmprestimo;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UC-D03: Buscar Propostas de Empréstimo
 * Lista propostas públicas disponíveis para devedores
 * 
 * Endpoints:
 * - GET /api/devedor/buscar-propostas - Lista todas as propostas ativas
 * - GET /api/devedor/buscar-propostas/{idPublico} - Busca proposta específica
 * - GET /api/devedor/buscar-propostas/{idPublico}/detalhes - Detalhes completos com simulações
 * 
 * Query Parameters para filtros:
 * - valorMin: Valor mínimo
 * - valorMax: Valor máximo
 * - parcelasMin: Parcelas mínimas
 * - parcelasMax: Parcelas máximas
 * - taxaJurosMax: Taxa de juros máxima
 */
@Path("/devedor/buscar-propostas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BuscarPropostasController {
    
    /**
     * Listar todas as propostas ativas com filtros opcionais
     * GET /api/devedor/buscar-propostas
     */
    @GET
    public Response buscarPropostas(
            @QueryParam("valorMin") Double valorMin,
            @QueryParam("valorMax") Double valorMax,
            @QueryParam("parcelasMin") Integer parcelasMin,
            @QueryParam("parcelasMax") Integer parcelasMax,
            @QueryParam("taxaJurosMax") Double taxaJurosMax) {
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            StringBuilder jpql = new StringBuilder(
                "SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA'"
            );
            
            // Aplicar filtros
            if (valorMin != null) {
                jpql.append(" AND p.valorDisponivel >= :valorMin");
            }
            if (valorMax != null) {
                jpql.append(" AND p.valorDisponivel <= :valorMax");
            }
            if (parcelasMin != null) {
                jpql.append(" AND p.parcelasMaximas >= :parcelasMin");
            }
            if (parcelasMax != null) {
                jpql.append(" AND p.parcelasMinimas <= :parcelasMax");
            }
            if (taxaJurosMax != null) {
                jpql.append(" AND p.taxaJuros <= :taxaJurosMax");
            }
            
            jpql.append(" ORDER BY p.dataCriacao DESC");
            
            var query = em.createQuery(jpql.toString(), PropostaEmprestimo.class);
            
            // Setar parâmetros
            if (valorMin != null) query.setParameter("valorMin", valorMin);
            if (valorMax != null) query.setParameter("valorMax", valorMax);
            if (parcelasMin != null) query.setParameter("parcelasMin", parcelasMin);
            if (parcelasMax != null) query.setParameter("parcelasMax", parcelasMax);
            if (taxaJurosMax != null) query.setParameter("taxaJurosMax", taxaJurosMax);
            
            List<PropostaEmprestimo> propostas = query.getResultList();
            
            return Response.ok(propostas).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Buscar proposta por ID público
     * GET /api/devedor/buscar-propostas/{idPublico}
     */
    @GET
    @Path("/{idPublico}")
    public Response buscarPorId(@PathParam("idPublico") String idPublico) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<PropostaEmprestimo> propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.idPublico = :idPublico",
                PropostaEmprestimo.class
            )
            .setParameter("idPublico", idPublico)
            .getResultList();
            
            if (propostas.isEmpty()) {
                throw new AppException("Proposta não encontrada", 404);
            }
            
            PropostaEmprestimo proposta = propostas.get(0);
            
            // Verificar se está ativa
            if (!"ATIVA".equals(proposta.getStatus())) {
                Map<String, Object> response = new HashMap<>();
                response.put("proposta", proposta);
                response.put("aviso", "Esta proposta não está mais ativa");
                return Response.ok(response).build();
            }
            
            return Response.ok(proposta).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Obter detalhes completos da proposta com simulações de parcelas
     * GET /api/devedor/buscar-propostas/{idPublico}/detalhes
     */
    @GET
    @Path("/{idPublico}/detalhes")
    public Response obterDetalhes(@PathParam("idPublico") String idPublico) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<PropostaEmprestimo> propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.idPublico = :idPublico",
                PropostaEmprestimo.class
            )
            .setParameter("idPublico", idPublico)
            .getResultList();
            
            if (propostas.isEmpty()) {
                throw new AppException("Proposta não encontrada", 404);
            }
            
            PropostaEmprestimo proposta = propostas.get(0);
            
            // Calcular opções de parcelas
            List<CalculadoraEmprestimo.OpcaoParcela> opcoesParcelas = 
                CalculadoraEmprestimo.calcularOpcoesParcelas(
                    proposta.getValorDisponivel(),
                    proposta.getParcelasMinimas(),
                    proposta.getParcelasMaximas(),
                    proposta.getTaxaJuros()
                );
            
            // Calcular data da primeira parcela
            java.time.LocalDate dataPrimeiraParcela = CalculadoraEmprestimo.calcularDataPrimeiraParcela(
                java.time.LocalDate.now(),
                proposta.getDiasAtePrimeiraCobranca()
            );
            
            // Contar interesses
            Long countInteresses = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId",
                Long.class
            )
            .setParameter("propostaId", proposta.getId())
            .getSingleResult();
            
            // Montar resposta detalhada
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("proposta", proposta);
            detalhes.put("opcoesParcelas", opcoesParcelas);
            detalhes.put("dataPrimeiraParcela", dataPrimeiraParcela);
            detalhes.put("totalInteresses", countInteresses);
            detalhes.put("disponivel", "ATIVA".equals(proposta.getStatus()));
            
            return Response.ok(detalhes).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Buscar propostas por faixa de valor (atalho)
     * GET /api/devedor/buscar-propostas/faixa/{faixa}
     * 
     * Faixas predefinidas:
     * - ate-1000: até R$ 1.000
     * - 1000-5000: R$ 1.000 a R$ 5.000
     * - 5000-10000: R$ 5.000 a R$ 10.000
     * - acima-10000: acima de R$ 10.000
     */
    @GET
    @Path("/faixa/{faixa}")
    public Response buscarPorFaixa(@PathParam("faixa") String faixa) {
        Double valorMin = null;
        Double valorMax = null;
        
        switch (faixa.toLowerCase()) {
            case "ate-1000":
                valorMax = 1000.0;
                break;
            case "1000-5000":
                valorMin = 1000.0;
                valorMax = 5000.0;
                break;
            case "5000-10000":
                valorMin = 5000.0;
                valorMax = 10000.0;
                break;
            case "acima-10000":
                valorMin = 10000.0;
                break;
            default:
                throw new AppException("Faixa inválida. Use: ate-1000, 1000-5000, 5000-10000, acima-10000", 400);
        }
        
        return buscarPropostas(valorMin, valorMax, null, null, null);
    }
    
    /**
     * Buscar propostas com menor taxa de juros
     * GET /api/devedor/buscar-propostas/menor-taxa
     */
    @GET
    @Path("/menor-taxa")
    public Response buscarMenorTaxa(@QueryParam("limite") @DefaultValue("10") Integer limite) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA' ORDER BY p.taxaJuros ASC",
                PropostaEmprestimo.class
            )
            .setMaxResults(limite)
            .getResultList();
            
            return Response.ok(propostas).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Buscar propostas mais recentes
     * GET /api/devedor/buscar-propostas/recentes
     */
    @GET
    @Path("/recentes")
    public Response buscarRecentes(@QueryParam("limite") @DefaultValue("10") Integer limite) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA' ORDER BY p.dataCriacao DESC",
                PropostaEmprestimo.class
            )
            .setMaxResults(limite)
            .getResultList();
            
            return Response.ok(propostas).build();
            
        } finally {
            em.close();
        }
    }
}

 
