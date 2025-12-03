package com.agilit.controller.credor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.OfertaEmprestimo;
import com.agilit.model.PropostaEmprestimo;
import com.agilit.util.GeradorIdPublico;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;

/**
 * UC-C04: Gerar Proposta de Empréstimo
 * <<include>> Definir Juros e Validade
 * 
 * Transforma uma oferta privada em proposta pública que pode ser vista por devedores
 * 
 * Endpoint: POST /api/credor/gerar-proposta
 * 
 * Body exemplo:
 * {
 *   "ofertaId": 1,
 *   "taxaJuros": 2.5
 * }
 * 
 * Regras de Negócio:
 * - Oferta deve existir e estar ativa
 * - Gera ID público único para a proposta
 * - Permite ajustar taxa de juros ao gerar proposta
 * - Proposta herda dados da oferta
 */
@Path("/credor/gerar-proposta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GerarPropostaEmprestimoController {
    
    @POST
    public Response gerarProposta(GerarPropostaDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Validação: ID da oferta obrigatório
            if (dto.ofertaId == null) {
                throw new AppException("ID da oferta é obrigatório", 400);
            }
            
            // Buscar oferta
            OfertaEmprestimo oferta = em.find(OfertaEmprestimo.class, dto.ofertaId);
            if (oferta == null) {
                throw new AppException("Oferta não encontrada", 404);
            }
            
            // Validação: Oferta deve estar ativa
            if (!oferta.getAtiva()) {
                throw new AppException("Oferta não está ativa", 400);
            }
            
            // Gerar ID público único
            String idPublico = gerarIdPublicoUnico(em);
            
            // Criar proposta baseada na oferta
            PropostaEmprestimo proposta = new PropostaEmprestimo();
            proposta.setIdPublico(idPublico);
            proposta.setOfertaOrigem(oferta);
            proposta.setCredor(oferta.getCredor());
            proposta.setNomeCredor(oferta.getCredor().getNome());
            proposta.setValorDisponivel(oferta.getValorDisponivel());
            proposta.setParcelasMinimas(oferta.getParcelasMinimas());
            proposta.setParcelasMaximas(oferta.getParcelasMaximas());
            proposta.setDiasAtePrimeiraCobranca(oferta.getDiasAtePrimeiraCobranca());
            
            // Permitir ajuste de juros (caso fornecido no DTO)
            if (dto.taxaJuros != null) {
                if (dto.taxaJuros < 0) {
                    throw new AppException("Taxa de juros deve ser maior ou igual a zero", 400);
                }
                proposta.setTaxaJuros(dto.taxaJuros);
            } else {
                proposta.setTaxaJuros(oferta.getTaxaJuros());
            }
            
            proposta.setDataCriacao(LocalDateTime.now());
            proposta.setStatus("ATIVA");
            
            em.persist(proposta);
            em.getTransaction().commit();
            
            return Response.status(Response.Status.CREATED).entity(proposta).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao gerar proposta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Gera um ID público único para a proposta
     */
    private String gerarIdPublicoUnico(EntityManager em) {
        String idPublico;
        int tentativas = 0;
        
        do {
            idPublico = GeradorIdPublico.gerar();
            Long count = em.createQuery(
                "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.idPublico = :idPublico",
                Long.class
            )
            .setParameter("idPublico", idPublico)
            .getSingleResult();
            
            if (count == 0) {
                return idPublico;
            }
            
            tentativas++;
            if (tentativas > 10) {
                throw new AppException("Erro ao gerar ID público único após 10 tentativas", 500);
            }
        } while (true);
    }
    
    /**
     * Listar propostas geradas pelo credor
     * GET /api/credor/gerar-proposta/minhas/{credorId}
     */
    @GET
    @Path("/minhas/{credorId}")
    public Response listarMinhasPropostas(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var propostas = em.createQuery(
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
     * Listar apenas propostas ativas do credor
     * GET /api/credor/gerar-proposta/minhas/{credorId}/ativas
     */
    @GET
    @Path("/minhas/{credorId}/ativas")
    public Response listarMinhasPropostasAtivas(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var propostas = em.createQuery(
                "SELECT p FROM PropostaEmprestimo p WHERE p.credor.id = :credorId AND p.status = 'ATIVA' ORDER BY p.dataCriacao DESC",
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
     * Cancelar proposta
     * PUT /api/credor/gerar-proposta/{propostaId}/cancelar
     */
    @PUT
    @Path("/{propostaId}/cancelar")
    public Response cancelarProposta(@PathParam("propostaId") Long propostaId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, propostaId);
            
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
            .setParameter("propostaId", propostaId)
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
     * DTO para gerar proposta
     */
    public static class GerarPropostaDTO {
        public Long ofertaId;
        public Double taxaJuros; // Opcional: permite ajustar juros ao gerar proposta
    }
}

// Made with Bob
