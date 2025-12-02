package com.agilit.controller.interesse;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.*;
import com.agilit.util.CalculadoraEmprestimo;
import com.agilit.util.NotificacaoService;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerenciar Interesses em Propostas.
 * Implementa o fluxo completo: demonstrar interesse, aprovar, confirmar bilateralmente e criar empréstimo.
 */
@Path("/interesse")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InteressePropostaController {

    /**
     * Devedor demonstra interesse em uma proposta
     * POST /api/interesse
     */
    @POST
    public Response demonstrarInteresse(InteresseProposta interesse) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            // Validações
            if (interesse.getProposta() == null || interesse.getProposta().getId() == null) {
                throw new AppException("Proposta é obrigatória", 400);
            }

            if (interesse.getDevedor() == null || interesse.getDevedor().getId() == null) {
                throw new AppException("Devedor é obrigatório", 400);
            }

            // Buscar proposta
            PropostaEmprestimo proposta = em.find(PropostaEmprestimo.class, 
                                                  interesse.getProposta().getId());
            if (proposta == null) {
                throw new AppException("Proposta não encontrada", 404);
            }

            if (!"ATIVA".equals(proposta.getStatus())) {
                throw new AppException("Proposta não está ativa", 400);
            }

            // Buscar devedor
            Devedor devedor = em.find(Devedor.class, interesse.getDevedor().getId());
            if (devedor == null) {
                throw new AppException("Devedor não encontrado", 404);
            }

            // Validar dados completos do devedor
            if (devedor.getEndereco() == null || devedor.getCidade() == null || 
                devedor.getEstado() == null || devedor.getCep() == null) {
                throw new AppException(
                    "Devedor deve ter dados completos (endereço, cidade, estado, CEP) para demonstrar interesse", 
                    400
                );
            }

            // Verificar se já existe interesse deste devedor nesta proposta
            Long count = em.createQuery(
                "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.devedor.id = :devedorId",
                Long.class
            )
            .setParameter("propostaId", proposta.getId())
            .setParameter("devedorId", devedor.getId())
            .getSingleResult();

            if (count > 0) {
                throw new AppException("Você já demonstrou interesse nesta proposta", 409);
            }

            // Criar interesse
            interesse.setProposta(proposta);
            interesse.setDevedor(devedor);
            interesse.setDataInteresse(LocalDateTime.now());
            interesse.setStatus("PENDENTE");
            interesse.setConfirmacaoCredor(false);
            interesse.setConfirmacaoDevedor(false);

            em.persist(interesse);

            // Notificar credor
            NotificacaoService.notificarNovoInteresse(em, proposta.getCredor(), proposta, devedor);

            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(interesse).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao demonstrar interesse: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Listar interessados em uma proposta (para o Credor)
     * GET /api/interesse/proposta/{propostaId}
     */
    @GET
    @Path("/proposta/{propostaId}")
    public Response listarInteressados(@PathParam("propostaId") Long propostaId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<InteresseProposta> interesses = em.createQuery(
                "SELECT i FROM InteresseProposta i WHERE i.proposta.id = :propostaId ORDER BY i.dataInteresse DESC",
                InteresseProposta.class
            )
            .setParameter("propostaId", propostaId)
            .getResultList();

            return Response.ok(interesses).build();

        } finally {
            em.close();
        }
    }

    /**
     * Listar meus interesses (para o Devedor)
     * GET /api/interesse/devedor/{devedorId}
     */
    @GET
    @Path("/devedor/{devedorId}")
    public Response listarMeusInteresses(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<InteresseProposta> interesses = em.createQuery(
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
     * Credor aprova interesse
     * PUT /api/interesse/{id}/aprovar
     */
    @PUT
    @Path("/{id}/aprovar")
    public Response aprovar(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            InteresseProposta interesse = em.find(InteresseProposta.class, id);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }

            if (!"PENDENTE".equals(interesse.getStatus())) {
                throw new AppException("Apenas interesses pendentes podem ser aprovados", 400);
            }

            interesse.setStatus("APROVADO");
            em.merge(interesse);

            // Notificar devedor
            NotificacaoService.notificarAprovacao(em, interesse.getDevedor(), interesse);

            em.getTransaction().commit();

            return Response.ok(interesse).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao aprovar interesse: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Credor rejeita interesse
     * PUT /api/interesse/{id}/rejeitar
     */
    @PUT
    @Path("/{id}/rejeitar")
    public Response rejeitar(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            InteresseProposta interesse = em.find(InteresseProposta.class, id);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }

            if (!"PENDENTE".equals(interesse.getStatus()) && !"APROVADO".equals(interesse.getStatus())) {
                throw new AppException("Apenas interesses pendentes ou aprovados podem ser rejeitados", 400);
            }

            interesse.setStatus("REJEITADO");
            em.merge(interesse);

            // Notificar devedor
            NotificacaoService.notificarRejeicao(em, interesse.getDevedor(), interesse);

            em.getTransaction().commit();

            return Response.ok(interesse).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao rejeitar interesse: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Credor confirma empréstimo
     * PUT /api/interesse/{id}/confirmar-credor
     */
    @POST
    @Path("/{id}/confirmar-credor")
    public Response confirmarCredor(@PathParam("id") Long id, ConfirmacaoDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            InteresseProposta interesse = em.find(InteresseProposta.class, id);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }

            if (!"APROVADO".equals(interesse.getStatus())) {
                throw new AppException("Interesse deve estar aprovado para confirmação", 400);
            }

            if (interesse.getConfirmacaoCredor()) {
                throw new AppException("Credor já confirmou", 400);
            }

            // Validar número de parcelas escolhido
            if (dto.numeroParcelas == null) {
                throw new AppException("Número de parcelas é obrigatório", 400);
            }

            PropostaEmprestimo proposta = interesse.getProposta();
            if (dto.numeroParcelas < proposta.getParcelasMinimas() || 
                dto.numeroParcelas > proposta.getParcelasMaximas()) {
                throw new AppException(
                    String.format("Número de parcelas deve estar entre %d e %d", 
                                 proposta.getParcelasMinimas(), proposta.getParcelasMaximas()),
                    400
                );
            }

            // Verificar saldo do credor
            Credor credor = proposta.getCredor();
            if (credor.getSaldoDisponivel() < proposta.getValorDisponivel()) {
                throw new AppException("Credor não possui saldo disponível suficiente", 400);
            }

            interesse.setConfirmacaoCredor(true);
            interesse.setDataConfirmacaoCredor(LocalDateTime.now());
            em.merge(interesse);

            // Se ambos confirmaram, criar empréstimo
            if (interesse.getConfirmacaoDevedor()) {
                criarEmprestimo(em, interesse, dto.numeroParcelas);
            } else {
                // Notificar devedor para confirmar
                NotificacaoService.criarNotificacao(
                    em, "DEVEDOR", interesse.getDevedor().getId(),
                    "CONFIRMACAO_PENDENTE",
                    "Aguardando sua Confirmação",
                    "O credor confirmou o empréstimo. Confirme para finalizar."
                );
            }

            em.getTransaction().commit();

            return Response.ok(interesse).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao confirmar: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Devedor confirma empréstimo
     * PUT /api/interesse/{id}/confirmar-devedor
     */
    @POST
    @Path("/{id}/confirmar-devedor")
    public Response confirmarDevedor(@PathParam("id") Long id, ConfirmacaoDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            InteresseProposta interesse = em.find(InteresseProposta.class, id);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }

            if (!"APROVADO".equals(interesse.getStatus())) {
                throw new AppException("Interesse deve estar aprovado para confirmação", 400);
            }

            if (interesse.getConfirmacaoDevedor()) {
                throw new AppException("Devedor já confirmou", 400);
            }

            // Validar número de parcelas
            if (dto.numeroParcelas == null) {
                throw new AppException("Número de parcelas é obrigatório", 400);
            }

            PropostaEmprestimo proposta = interesse.getProposta();
            if (dto.numeroParcelas < proposta.getParcelasMinimas() || 
                dto.numeroParcelas > proposta.getParcelasMaximas()) {
                throw new AppException(
                    String.format("Número de parcelas deve estar entre %d e %d", 
                                 proposta.getParcelasMinimas(), proposta.getParcelasMaximas()),
                    400
                );
            }

            interesse.setConfirmacaoDevedor(true);
            interesse.setDataConfirmacaoDevedor(LocalDateTime.now());
            em.merge(interesse);

            // Se ambos confirmaram, criar empréstimo
            if (interesse.getConfirmacaoCredor()) {
                criarEmprestimo(em, interesse, dto.numeroParcelas);
            } else {
                // Notificar credor para confirmar
                NotificacaoService.criarNotificacao(
                    em, "CREDOR", proposta.getCredor().getId(),
                    "CONFIRMACAO_PENDENTE",
                    "Aguardando sua Confirmação",
                    String.format("O devedor %s confirmou interesse na proposta %s", 
                                 interesse.getDevedor().getNome(), proposta.getIdPublico())
                );
            }

            em.getTransaction().commit();

            return Response.ok(interesse).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao confirmar: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Devedor cancela interesse
     * DELETE /api/interesse/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response cancelar(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            InteresseProposta interesse = em.find(InteresseProposta.class, id);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }

            if ("APROVADO".equals(interesse.getStatus()) && 
                (interesse.getConfirmacaoCredor() || interesse.getConfirmacaoDevedor())) {
                throw new AppException("Não é possível cancelar interesse com confirmações", 400);
            }

            interesse.setStatus("CANCELADO");
            em.merge(interesse);

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
            throw new AppException("Erro ao cancelar interesse: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Método privado para criar empréstimo após confirmação bilateral
     */
    private void criarEmprestimo(EntityManager em, InteresseProposta interesse, Integer numeroParcelas) {
        PropostaEmprestimo proposta = interesse.getProposta();
        Credor credor = proposta.getCredor();
        Devedor devedor = interesse.getDevedor();

        // Calcular valores
        double valorPrincipal = proposta.getValorDisponivel();
        double juros = CalculadoraEmprestimo.calcularJurosSimples(
            valorPrincipal, proposta.getTaxaJuros(), numeroParcelas
        );
        double valorTotal = valorPrincipal + juros;
        double valorParcela = valorTotal / numeroParcelas;

        // Criar empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setDevedor(devedor);
        emprestimo.setCredor(credor);
        emprestimo.setPropostaOrigem(proposta);
        emprestimo.setInteresseOrigem(interesse);
        emprestimo.setValorPrincipal(valorPrincipal);
        emprestimo.setJurosAplicados(juros);
        emprestimo.setValorTotal(valorTotal);
        emprestimo.setNumeroParcelas(numeroParcelas);
        emprestimo.setParcelasPagas(0);
        emprestimo.setDataInicio(LocalDate.now());
        
        // Calcular data de vencimento
        LocalDate dataVencimento = CalculadoraEmprestimo.calcularDataVencimentoFinal(
            LocalDate.now(), numeroParcelas, proposta.getDiasAtePrimeiraCobranca()
        );
        emprestimo.setDataVencimento(dataVencimento);
        emprestimo.setStatus("EM_ANDAMENTO");

        // Criar parcelas
        List<LocalDate> datasParcelas = CalculadoraEmprestimo.calcularDatasParcelas(
            LocalDate.now(), numeroParcelas, proposta.getDiasAtePrimeiraCobranca()
        );

        List<Parcela> parcelas = new ArrayList<>();
        for (int i = 0; i < numeroParcelas; i++) {
            Parcela parcela = new Parcela();
            parcela.setEmprestimo(emprestimo);
            parcela.setNumeroParcela(i + 1);
            parcela.setValor(CalculadoraEmprestimo.arredondar(valorParcela));
            parcela.setDataVencimento(datasParcelas.get(i));
            parcela.setPaga(false);
            parcela.setAtrasada(false);
            parcelas.add(parcela);
        }

        emprestimo.setParcelas(parcelas);
        em.persist(emprestimo);

        // Atualizar saldo do credor
        credor.setSaldoDisponivel(credor.getSaldoDisponivel() - valorPrincipal);
        em.merge(credor);

        // Atualizar status da proposta
        proposta.setStatus("ACEITA");
        em.merge(proposta);

        // Notificar ambos
        NotificacaoService.notificarConfirmacao(em, "CREDOR", credor.getId(), emprestimo);
        NotificacaoService.notificarConfirmacao(em, "DEVEDOR", devedor.getId(), emprestimo);
    }

    /**
     * DTO para confirmação com número de parcelas
     */
    public static class ConfirmacaoDTO {
        public Integer numeroParcelas;
    }
}

// Made with Bob
