package com.agilit.controller.parcela;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
// import com.agilit.model.Credor;
import com.agilit.model.Emprestimo;
import com.agilit.model.Parcela;
import com.agilit.util.NotificacaoService;
import com.agilit.util.VerificadorStatusEmprestimo;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para gerenciar Parcelas de Empréstimos.
 * Permite listar, visualizar e marcar parcelas como pagas.
 */
@Path("/parcela")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParcelaController {

    /**
     * Listar parcelas de um empréstimo
     * GET /api/parcela/emprestimo/{emprestimoId}
     */
    @GET
    @Path("/emprestimo/{emprestimoId}")
    public Response listarPorEmprestimo(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Emprestimo emprestimo = em.find(Emprestimo.class, emprestimoId);
            
            if (emprestimo == null) {
                throw new AppException("Empréstimo não encontrado", 404);
            }

            List<Parcela> parcelas = em.createQuery(
                "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId ORDER BY p.numeroParcela",
                Parcela.class
            )
            .setParameter("emprestimoId", emprestimoId)
            .getResultList();

            // Atualizar status de atraso
            for (Parcela parcela : parcelas) {
                parcela.verificarAtraso();
            }

            return Response.ok(parcelas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Buscar parcela por ID
     * GET /api/parcela/{id}
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Parcela parcela = em.find(Parcela.class, id);
            
            if (parcela == null) {
                throw new AppException("Parcela não encontrada", 404);
            }

            parcela.verificarAtraso();

            return Response.ok(parcela).build();

        } finally {
            em.close();
        }
    }

    /**
     * Marcar parcela como paga (apenas Credor)
     * PUT /api/parcela/{id}/pagar
     */
    @PUT
    @Path("/{id}/pagar")
    public Response marcarComoPaga(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            Parcela parcela = em.find(Parcela.class, id);
            
            if (parcela == null) {
                throw new AppException("Parcela não encontrada", 404);
            }

            if (parcela.getPaga()) {
                throw new AppException("Parcela já está paga", 400);
            }

            // Marcar como paga usando o utilitário
            VerificadorStatusEmprestimo.marcarParcelaPaga(parcela, em);

            // Verificar se empréstimo foi quitado
            Emprestimo emprestimo = parcela.getEmprestimo();
            if (VerificadorStatusEmprestimo.isQuitado(emprestimo)) {
                // Notificar ambos sobre quitação
                NotificacaoService.notificarQuitacao(
                    em, "CREDOR", emprestimo.getCredor().getId(), emprestimo
                );
                NotificacaoService.notificarQuitacao(
                    em, "DEVEDOR", emprestimo.getDevedor().getId(), emprestimo
                );
            } else {
                // Notificar credor sobre pagamento
                NotificacaoService.notificarPagamento(
                    em, emprestimo.getCredor(), parcela
                );
            }

            em.getTransaction().commit();

            return Response.ok(parcela).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao marcar parcela como paga: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Listar parcelas pendentes de um empréstimo
     * GET /api/parcela/emprestimo/{emprestimoId}/pendentes
     */
    @GET
    @Path("/emprestimo/{emprestimoId}/pendentes")
    public Response listarPendentes(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<Parcela> parcelas = em.createQuery(
                "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false ORDER BY p.numeroParcela",
                Parcela.class
            )
            .setParameter("emprestimoId", emprestimoId)
            .getResultList();

            // Atualizar status de atraso
            for (Parcela parcela : parcelas) {
                parcela.verificarAtraso();
            }

            return Response.ok(parcelas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar parcelas pagas de um empréstimo
     * GET /api/parcela/emprestimo/{emprestimoId}/pagas
     */
    @GET
    @Path("/emprestimo/{emprestimoId}/pagas")
    public Response listarPagas(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<Parcela> parcelas = em.createQuery(
                "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = true ORDER BY p.numeroParcela",
                Parcela.class
            )
            .setParameter("emprestimoId", emprestimoId)
            .getResultList();

            return Response.ok(parcelas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar parcelas atrasadas de um empréstimo
     * GET /api/parcela/emprestimo/{emprestimoId}/atrasadas
     */
    @GET
    @Path("/emprestimo/{emprestimoId}/atrasadas")
    public Response listarAtrasadas(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            LocalDate hoje = LocalDate.now();
            
            List<Parcela> parcelas = em.createQuery(
                "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false AND p.dataVencimento < :hoje ORDER BY p.numeroParcela",
                Parcela.class
            )
            .setParameter("emprestimoId", emprestimoId)
            .setParameter("hoje", hoje)
            .getResultList();

            // Atualizar status de atraso
            for (Parcela parcela : parcelas) {
                parcela.verificarAtraso();
            }

            return Response.ok(parcelas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Obter resumo de parcelas de um empréstimo
     * GET /api/parcela/emprestimo/{emprestimoId}/resumo
     */
    @GET
    @Path("/emprestimo/{emprestimoId}/resumo")
    public Response obterResumo(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Emprestimo emprestimo = em.find(Emprestimo.class, emprestimoId);
            
            if (emprestimo == null) {
                throw new AppException("Empréstimo não encontrado", 404);
            }

            // Atualizar status do empréstimo
            VerificadorStatusEmprestimo.atualizarStatusEmprestimo(emprestimo);

            // Calcular totais
            double totalPago = VerificadorStatusEmprestimo.calcularTotalPago(emprestimo);
            double totalPendente = VerificadorStatusEmprestimo.calcularTotalPendente(emprestimo);
            long parcelasAtrasadas = VerificadorStatusEmprestimo.contarParcelasAtrasadas(emprestimo);

            // Contar parcelas por status
            Long parcelasPagas = em.createQuery(
                "SELECT COUNT(p) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = true",
                Long.class
            ).setParameter("emprestimoId", emprestimoId).getSingleResult();

            Long parcelasPendentes = em.createQuery(
                "SELECT COUNT(p) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false",
                Long.class
            ).setParameter("emprestimoId", emprestimoId).getSingleResult();

            // Próxima parcela a vencer
            List<Parcela> proximasParcelas = em.createQuery(
                "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false ORDER BY p.dataVencimento",
                Parcela.class
            )
            .setParameter("emprestimoId", emprestimoId)
            .setMaxResults(1)
            .getResultList();

            Parcela proximaParcela = proximasParcelas.isEmpty() ? null : proximasParcelas.get(0);

            // Montar resumo
            Map<String, Object> resumo = new HashMap<>();
            resumo.put("emprestimoId", emprestimoId);
            resumo.put("valorTotal", emprestimo.getValorTotal());
            resumo.put("totalPago", totalPago);
            resumo.put("totalPendente", totalPendente);
            resumo.put("numeroParcelas", emprestimo.getNumeroParcelas());
            resumo.put("parcelasPagas", parcelasPagas);
            resumo.put("parcelasPendentes", parcelasPendentes);
            resumo.put("parcelasAtrasadas", parcelasAtrasadas);
            resumo.put("status", emprestimo.getStatus());
            resumo.put("proximaParcela", proximaParcela);
            resumo.put("percentualPago", (totalPago / emprestimo.getValorTotal()) * 100);

            return Response.ok(resumo).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar todas as parcelas vencidas do sistema (para job de notificação)
     * GET /api/parcela/vencidas
     */
    @GET
    @Path("/vencidas")
    public Response listarTodasVencidas() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<Parcela> parcelas = VerificadorStatusEmprestimo.buscarParcelasVencidas();
            return Response.ok(parcelas).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar parcelas próximas do vencimento (próximos 7 dias)
     * GET /api/parcela/emprestimo/{emprestimoId}/proximas-vencimento
     */
    @GET
    @Path("/emprestimo/{emprestimoId}/proximas-vencimento")
    public Response listarProximasVencimento(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate daquiSete = hoje.plusDays(7);
            
            List<Parcela> parcelas = em.createQuery(
                "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId " +
                "AND p.paga = false " +
                "AND p.dataVencimento BETWEEN :hoje AND :daquiSete " +
                "ORDER BY p.dataVencimento",
                Parcela.class
            )
            .setParameter("emprestimoId", emprestimoId)
            .setParameter("hoje", hoje)
            .setParameter("daquiSete", daquiSete)
            .getResultList();

            return Response.ok(parcelas).build();

        } finally {
            em.close();
        }
    }
}

 
