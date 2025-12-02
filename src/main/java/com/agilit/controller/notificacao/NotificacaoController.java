package com.agilit.controller.notificacao;

import com.agilit.config.AppException;
import com.agilit.model.Notificacao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller para gerenciar Notificações.
 * Permite listar, marcar como lida e deletar notificações.
 */
@Path("/notificacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificacaoController {

    private static final EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("agilitPU");

    /**
     * Listar todas as notificações de um usuário
     * GET /api/notificacao/{tipoDestinatario}/{destinatarioId}
     * 
     * @param tipoDestinatario CREDOR ou DEVEDOR
     * @param destinatarioId ID do usuário
     */
    @GET
    @Path("/{tipoDestinatario}/{destinatarioId}")
    public Response listar(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            // Validar tipo
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            List<Notificacao> notificacoes = em.createQuery(
                "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id ORDER BY n.dataCriacao DESC",
                Notificacao.class
            )
            .setParameter("tipo", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .getResultList();

            return Response.ok(notificacoes).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar notificações não lidas
     * GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/nao-lidas
     */
    @GET
    @Path("/{tipoDestinatario}/{destinatarioId}/nao-lidas")
    public Response listarNaoLidas(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            // Validar tipo
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            List<Notificacao> notificacoes = em.createQuery(
                "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = false ORDER BY n.dataCriacao DESC",
                Notificacao.class
            )
            .setParameter("tipo", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .getResultList();

            return Response.ok(notificacoes).build();

        } finally {
            em.close();
        }
    }

    /**
     * Contar notificações não lidas
     * GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/count-nao-lidas
     */
    @GET
    @Path("/{tipoDestinatario}/{destinatarioId}/count-nao-lidas")
    public Response contarNaoLidas(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            // Validar tipo
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            Long count = em.createQuery(
                "SELECT COUNT(n) FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = false",
                Long.class
            )
            .setParameter("tipo", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .getSingleResult();

            return Response.ok(count).build();

        } finally {
            em.close();
        }
    }

    /**
     * Buscar notificação por ID
     * GET /api/notificacao/{id}
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            Notificacao notificacao = em.find(Notificacao.class, id);
            
            if (notificacao == null) {
                throw new AppException("Notificação não encontrada", 404);
            }

            return Response.ok(notificacao).build();

        } finally {
            em.close();
        }
    }

    /**
     * Marcar notificação como lida
     * PUT /api/notificacao/{id}/marcar-lida
     */
    @PUT
    @Path("/{id}/marcar-lida")
    public Response marcarComoLida(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            Notificacao notificacao = em.find(Notificacao.class, id);
            
            if (notificacao == null) {
                throw new AppException("Notificação não encontrada", 404);
            }

            if (!notificacao.getLida()) {
                notificacao.marcarComoLida();
                em.merge(notificacao);
            }

            em.getTransaction().commit();

            return Response.ok(notificacao).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao marcar notificação como lida: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Marcar todas as notificações como lidas
     * PUT /api/notificacao/{tipoDestinatario}/{destinatarioId}/marcar-todas-lidas
     */
    @PUT
    @Path("/{tipoDestinatario}/{destinatarioId}/marcar-todas-lidas")
    public Response marcarTodasComoLidas(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            // Validar tipo
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            int updated = em.createQuery(
                "UPDATE Notificacao n SET n.lida = true, n.dataLeitura = :dataLeitura " +
                "WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = false"
            )
            .setParameter("dataLeitura", LocalDateTime.now())
            .setParameter("tipo", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .executeUpdate();

            em.getTransaction().commit();

            return Response.ok("{\"marcadas\": " + updated + "}").build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao marcar notificações: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Deletar notificação
     * DELETE /api/notificacao/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            Notificacao notificacao = em.find(Notificacao.class, id);
            
            if (notificacao == null) {
                throw new AppException("Notificação não encontrada", 404);
            }

            em.remove(notificacao);
            em.getTransaction().commit();

            return Response.noContent().build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao deletar notificação: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Deletar todas as notificações lidas
     * DELETE /api/notificacao/{tipoDestinatario}/{destinatarioId}/lidas
     */
    @DELETE
    @Path("/{tipoDestinatario}/{destinatarioId}/lidas")
    public Response deletarLidas(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            // Validar tipo
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            int deleted = em.createQuery(
                "DELETE FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = true"
            )
            .setParameter("tipo", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .executeUpdate();

            em.getTransaction().commit();

            return Response.ok("{\"deletadas\": " + deleted + "}").build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao deletar notificações: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Filtrar notificações por tipo
     * GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/tipo/{tipo}
     */
    @GET
    @Path("/{tipoDestinatario}/{destinatarioId}/tipo/{tipo}")
    public Response filtrarPorTipo(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId,
            @PathParam("tipo") String tipo) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            // Validar tipo destinatário
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            List<Notificacao> notificacoes = em.createQuery(
                "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipoDestinatario " +
                "AND n.destinatarioId = :id AND n.tipo = :tipo ORDER BY n.dataCriacao DESC",
                Notificacao.class
            )
            .setParameter("tipoDestinatario", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .setParameter("tipo", tipo)
            .getResultList();

            return Response.ok(notificacoes).build();

        } finally {
            em.close();
        }
    }

    /**
     * Obter notificações recentes (últimas 24 horas)
     * GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/recentes
     */
    @GET
    @Path("/{tipoDestinatario}/{destinatarioId}/recentes")
    public Response listarRecentes(
            @PathParam("tipoDestinatario") String tipoDestinatario,
            @PathParam("destinatarioId") Long destinatarioId) {
        
        EntityManager em = emf.createEntityManager();
        
        try {
            // Validar tipo
            if (!tipoDestinatario.matches("CREDOR|DEVEDOR")) {
                throw new AppException("Tipo de destinatário inválido. Use: CREDOR ou DEVEDOR", 400);
            }

            LocalDateTime ultimas24h = LocalDateTime.now().minusHours(24);

            List<Notificacao> notificacoes = em.createQuery(
                "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo " +
                "AND n.destinatarioId = :id AND n.dataCriacao >= :data ORDER BY n.dataCriacao DESC",
                Notificacao.class
            )
            .setParameter("tipo", tipoDestinatario)
            .setParameter("id", destinatarioId)
            .setParameter("data", ultimas24h)
            .getResultList();

            return Response.ok(notificacoes).build();

        } finally {
            em.close();
        }
    }
}

// Made with Bob
