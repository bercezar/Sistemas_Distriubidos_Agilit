package com.agilit.controller.devedor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.Devedor;
import com.agilit.model.InteresseProposta;
import com.agilit.model.PropostaEmprestimo;
import com.agilit.util.NotificacaoService;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * UC-D04: Selecionar uma Proposta de Empréstimo
 * Demonstra interesse em uma proposta específica
 * 
 * Endpoint: POST /api/devedor/selecionar-proposta
 * 
 * Body exemplo:
 * {
 *   "propostaId": 1,
 *   "devedorId": 1
 * }
 * 
 * Regras de Negócio:
 * - Proposta deve existir e estar ativa
 * - Devedor deve ter dados cadastrais completos (endereço, cidade, estado, CEP)
 * - Devedor não pode ter interesse duplicado na mesma proposta
 * - Notifica o credor sobre o novo interesse
 */
@Path("/devedor/selecionar-proposta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SelecionarPropostaController {
    
    /**
     * Demonstrar interesse em uma proposta
     */
    @POST
    public Response selecionarProposta(SelecionarPropostaDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Validações básicas
            if (dto.propostaId == null) {
                throw new AppException("ID da proposta é obrigatório", 400);
            }
            
            if (dto.devedorId == null) {
                throw new AppException("ID do devedor é obrigatório", 400);
            }
            
            // Buscar proposta
            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, dto.propostaId);
            if (proposta == null) {
                throw new AppException("Proposta não encontrada", 404);
            }
            
            // Validação: Proposta deve estar ativa
            if (!"ATIVA".equals(proposta.getStatus())) {
                throw new AppException("Esta proposta não está mais ativa", 400);
            }
            
            // Buscar devedor
            Devedor devedor = em.find(Devedor.class, dto.devedorId);
            if (devedor == null) {
                throw new AppException("Devedor não encontrado", 404);
            }
            
            // Validação: Dados completos do devedor
            if (devedor.getEndereco() == null || devedor.getCidade() == null || 
                devedor.getEstado() == null || devedor.getCep() == null) {
                throw new AppException(
                    "Complete seus dados cadastrais (endereço, cidade, estado, CEP) antes de selecionar uma proposta", 
                    400
                );
            }
            
            // Validação: Verificar se já existe interesse
            Long count = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.devedor.id = :devedorId",
                Long.class
            )
            .setParameter("propostaId", dto.propostaId)
            .setParameter("devedorId", dto.devedorId)
            .getSingleResult();
            
            if (count > 0) {
                throw new AppException("Você já demonstrou interesse nesta proposta", 409);
            }
            
            // Criar interesse
            InteresseProposta interesse = new InteresseProposta();
            interesse.setProposta(proposta);
            interesse.setDevedor(devedor);
            interesse.setDataInteresse(LocalDateTime.now());
            interesse.setStatus("PENDENTE");
            interesse.setConfirmacaoCredor(false);
            interesse.setConfirmacaoDevedor(false);
            
            em.persist(interesse);
            
            // Notificar credor sobre novo interesse
            NotificacaoService.notificarNovoInteresse(em, proposta.getCredor(), proposta, devedor);
            
            em.getTransaction().commit();
            
            // Montar resposta
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Interesse registrado com sucesso");
            response.put("interesse", interesse);
            response.put("proximoPasso", "Aguarde a aprovação do credor");
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao selecionar proposta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar meus interesses
     * GET /api/devedor/selecionar-proposta/meus/{devedorId}
     */
    @GET
    @Path("/meus/{devedorId}")
    public Response listarMeusInteresses(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var interesses = em.createQuery(
                "SELECT i FROM InteresseProposta i WHERE i.devedor.id = :devedorId ORDER BY i.dataInteresse DESC",
                InteresseProposta.class
            )
            .setParameter("devedorId", devedorId)
            .getResultList();
            
            return Response.ok(interesses).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar interesses pendentes (aguardando aprovação do credor)
     * GET /api/devedor/selecionar-proposta/meus/{devedorId}/pendentes
     */
    @GET
    @Path("/meus/{devedorId}/pendentes")
    public Response listarInteressesPendentes(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var interesses = em.createQuery(
                "SELECT i FROM InteresseProposta i WHERE i.devedor.id = :devedorId AND i.status = 'PENDENTE' ORDER BY i.dataInteresse DESC",
                InteresseProposta.class
            )
            .setParameter("devedorId", devedorId)
            .getResultList();
            
            return Response.ok(interesses).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar interesses aprovados (aguardando confirmação)
     * GET /api/devedor/selecionar-proposta/meus/{devedorId}/aprovados
     */
    @GET
    @Path("/meus/{devedorId}/aprovados")
    public Response listarInteressesAprovados(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var interesses = em.createQuery(
                "SELECT i FROM InteresseProposta i WHERE i.devedor.id = :devedorId AND i.status = 'APROVADO' ORDER BY i.dataInteresse DESC",
                InteresseProposta.class
            )
            .setParameter("devedorId", devedorId)
            .getResultList();
            
            return Response.ok(interesses).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Cancelar interesse (antes de ser aprovado)
     * DELETE /api/devedor/selecionar-proposta/{interesseId}
     */
    @DELETE
    @Path("/{interesseId}")
    public Response cancelarInteresse(@PathParam("interesseId") Long interesseId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            InteresseProposta interesse = em.find(InteresseProposta.class, interesseId);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }
            
            // Validação: Só pode cancelar se ainda estiver pendente
            if (!"PENDENTE".equals(interesse.getStatus())) {
                throw new AppException("Só é possível cancelar interesses pendentes", 400);
            }
            
            interesse.setStatus("CANCELADO");
            em.merge(interesse);
            
            em.getTransaction().commit();
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Interesse cancelado com sucesso");
            
            return Response.ok(response).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao cancelar interesse: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Obter detalhes de um interesse específico
     * GET /api/devedor/selecionar-proposta/interesse/{interesseId}
     */
    @GET
    @Path("/interesse/{interesseId}")
    public Response obterDetalhesInteresse(@PathParam("interesseId") Long interesseId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            InteresseProposta interesse = em.find(InteresseProposta.class, interesseId);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }
            
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("interesse", interesse);
            detalhes.put("proposta", interesse.getProposta());
            detalhes.put("credor", interesse.getProposta().getCredor().getNome());
            detalhes.put("podeConfirmar", "APROVADO".equals(interesse.getStatus()) && !interesse.getConfirmacaoDevedor());
            detalhes.put("podeCancelar", "PENDENTE".equals(interesse.getStatus()));
            
            return Response.ok(detalhes).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * DTO para selecionar proposta
     */
    public static class SelecionarPropostaDTO {
        public Long propostaId;
        public Long devedorId;
    }
}

// Made with Bob
