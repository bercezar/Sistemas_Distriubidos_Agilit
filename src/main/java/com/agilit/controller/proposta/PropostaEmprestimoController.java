package com.agilit.controller.proposta;

import com.agilit.config.AppException;
import com.agilit.model.PropostaEmprestimo;
import com.agilit.util.CalculadoraEmprestimo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para gerenciar Propostas de Empréstimo.
 * Propostas são públicas (visíveis para todos os Devedores).
 * Operações: READ, CANCEL
 */
@Path("/proposta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PropostaEmprestimoController {

    private static final EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("agilitPU");

    /**
     * Listar todas as propostas ativas (públicas para Devedores)
     * GET /api/proposta/publicas
     */
    @GET
    @Path("/publicas")
    public Response listarPropostasPublicas() {
        EntityManager em = emf.createEntityManager();
        
        try {
            List<PropostaEmprestimo> propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA' ORDER BY p.dataCriacao DESC",
                PropostaEmprestimo.class
            ).getResultList();

            return Response.ok(propostas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar propostas do credor logado
     * GET /api/proposta/credor/{credorId}
     */
    @GET
    @Path("/credor/{credorId}")
    public Response listarPorCredor(@PathParam("credorId") Long credorId) {
        EntityManager em = emf.createEntityManager();
        
        try {
            List<PropostaEmprestimo> propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.credor.id = :credorId ORDER BY p.dataCriacao DESC",
                PropostaEmprestimo.class
            )
            .setParameter("credorId", credorId)
            .getResultList();

            return Response.ok(propostas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Buscar proposta por ID público
     * GET /api/proposta/publico/{idPublico}
     */
    @GET
    @Path("/publico/{idPublico}")
    public Response buscarPorIdPublico(@PathParam("idPublico") String idPublico) {
        EntityManager em = emf.createEntityManager();
        
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

            return Response.ok(propostas.get(0)).build();

        } finally {
            em.close();
        }
    }

    /**
     * Buscar proposta por ID
     * GET /api/proposta/{id}
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, id);
            
            if (proposta == null) {
                throw new AppException("Proposta não encontrada", 404);
            }

            return Response.ok(proposta).build();

        } finally {
            em.close();
        }
    }

    /**
     * Cancelar proposta
     * PUT /api/proposta/{id}/cancelar
     */
    @PUT
    @Path("/{id}/cancelar")
    public Response cancelar(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, id);
            
            if (proposta == null) {
                throw new AppException("Proposta não encontrada", 404);
            }

            if ("CANCELADA".equals(proposta.getStatus())) {
                throw new AppException("Proposta já está cancelada", 400);
            }

            if ("ACEITA".equals(proposta.getStatus())) {
                throw new AppException("Não é possível cancelar proposta já aceita", 400);
            }

            // Verificar se tem interesses pendentes
            Long countInteresses = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'PENDENTE'",
                Long.class
            )
            .setParameter("propostaId", id)
            .getSingleResult();

            if (countInteresses > 0) {
                throw new AppException(
                    "Não é possível cancelar proposta com interesses pendentes. Rejeite os interesses primeiro.", 
                    400
                );
            }

            proposta.setStatus("CANCELADA");
            em.merge(proposta);
            em.getTransaction().commit();

            return Response.ok(proposta).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao cancelar proposta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Obter detalhes completos da proposta com cálculos
     * GET /api/proposta/{id}/detalhes
     */
    @GET
    @Path("/{id}/detalhes")
    public Response obterDetalhes(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, id);
            
            if (proposta == null) {
                throw new AppException("Proposta não encontrada", 404);
            }

            // Calcular opções de parcelas
            List<CalculadoraEmprestimo.OpcaoParcela> opcoesParcelas = 
                CalculadoraEmprestimo.calcularOpcoesParcelas(
                    proposta.getValorDisponivel(),
                    proposta.getParcelasMinimas(),
                    proposta.getParcelasMaximas(),
                    proposta.getTaxaJuros()
                );

            // Calcular data da primeira parcela
            LocalDate dataPrimeiraParcela = CalculadoraEmprestimo.calcularDataPrimeiraParcela(
                LocalDate.now(),
                proposta.getDiasAtePrimeiraCobranca()
            );

            // Contar interesses
            Long countInteresses = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId",
                Long.class
            )
            .setParameter("propostaId", id)
            .getSingleResult();

            // Montar resposta
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("proposta", proposta);
            detalhes.put("opcoesParcelas", opcoesParcelas);
            detalhes.put("dataPrimeiraParcela", dataPrimeiraParcela);
            detalhes.put("totalInteresses", countInteresses);

            return Response.ok(detalhes).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar propostas por status
     * GET /api/proposta/status/{status}
     */
    @GET
    @Path("/status/{status}")
    public Response listarPorStatus(@PathParam("status") String status) {
        EntityManager em = emf.createEntityManager();
        
        try {
            // Validar status
            if (!status.matches("ATIVA|CANCELADA|ACEITA")) {
                throw new AppException("Status inválido. Use: ATIVA, CANCELADA ou ACEITA", 400);
            }

            List<PropostaEmprestimo> propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.status = :status ORDER BY p.dataCriacao DESC",
                PropostaEmprestimo.class
            )
            .setParameter("status", status)
            .getResultList();

            return Response.ok(propostas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Buscar propostas por faixa de valor
     * GET /api/proposta/buscar?valorMin={min}&valorMax={max}
     */
    @GET
    @Path("/buscar")
    public Response buscarPorValor(
            @QueryParam("valorMin") Double valorMin,
            @QueryParam("valorMax") Double valorMax) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            StringBuilder jpql = new StringBuilder(
                "SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA'"
            );

            if (valorMin != null) {
                jpql.append(" AND p.valorDisponivel >= :valorMin");
            }

            if (valorMax != null) {
                jpql.append(" AND p.valorDisponivel <= :valorMax");
            }

            jpql.append(" ORDER BY p.dataCriacao DESC");

            var query = em.createQuery(jpql.toString(), PropostaEmprestimo.class);

            if (valorMin != null) {
                query.setParameter("valorMin", valorMin);
            }

            if (valorMax != null) {
                query.setParameter("valorMax", valorMax);
            }

            List<PropostaEmprestimo> propostas = query.getResultList();

            return Response.ok(propostas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Obter estatísticas de uma proposta
     * GET /api/proposta/{id}/estatisticas
     */
    @GET
    @Path("/{id}/estatisticas")
    public Response obterEstatisticas(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, id);
            
            if (proposta == null) {
                throw new AppException("Proposta não encontrada", 404);
            }

            // Contar interesses por status
            Long totalInteresses = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId",
                Long.class
            ).setParameter("propostaId", id).getSingleResult();

            Long interessesPendentes = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'PENDENTE'",
                Long.class
            ).setParameter("propostaId", id).getSingleResult();

            Long interessesAprovados = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'APROVADO'",
                Long.class
            ).setParameter("propostaId", id).getSingleResult();

            Long emprestimosGerados = em.createQuery(
                "SELECT COUNT(e) FROM Emprestimo e WHERE e.propostaOrigem.id = :propostaId",
                Long.class
            ).setParameter("propostaId", id).getSingleResult();

            Map<String, Object> estatisticas = new HashMap<>();
            estatisticas.put("totalInteresses", totalInteresses);
            estatisticas.put("interessesPendentes", interessesPendentes);
            estatisticas.put("interessesAprovados", interessesAprovados);
            estatisticas.put("emprestimosGerados", emprestimosGerados);
            estatisticas.put("status", proposta.getStatus());

            return Response.ok(estatisticas).build();

        } finally {
            em.close();
        }
    }
}

// Made with Bob
