package com.agilit.controller.credor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.controller.emprestimo.StatusEmprestimo;
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
 * UC-C05: Registrar Empréstimo
 * Confirma a criação do empréstimo pelo credor após aprovação bilateral
 * 
 * Endpoint: POST /api/credor/registrar-emprestimo/{interesseId}/confirmar
 * 
 * Body exemplo:
 * {
 *   "numeroParcelas": 12
 * }
 * 
 * Regras de Negócio:
 * - Interesse deve estar aprovado
 * - Credor não pode ter confirmado ainda
 * - Número de parcelas deve estar dentro do range da proposta
 * - Credor deve ter saldo suficiente
 * - Se ambos confirmaram, cria o empréstimo automaticamente
 */
@Path("/credor/registrar-emprestimo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrarEmprestimoController {
    
    /**
     * Confirmar empréstimo pelo credor
     */
    @POST
    @Path("/{interesseId}/confirmar")
    public Response confirmarEmprestimo(
            @PathParam("interesseId") Long interesseId,
            ConfirmacaoDTO dto) {
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Buscar interesse
            InteresseProposta interesse = em.find(InteresseProposta.class, interesseId);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }
            
            // Validação: Interesse deve estar aprovado
            if (!"APROVADO".equals(interesse.getStatus())) {
                throw new AppException("Interesse deve estar aprovado para confirmação", 400);
            }
            
            // Validação: Credor não pode ter confirmado ainda
            if (interesse.getConfirmacaoCredor()) {
                throw new AppException("Credor já confirmou este empréstimo", 400);
            }
            
            // Validação: Número de parcelas obrigatório
            if (dto.numeroParcelas == null) {
                throw new AppException("Número de parcelas é obrigatório", 400);
            }
            
            // Validação: Número de parcelas dentro do range
            PropostaEmprestimo proposta = interesse.getProposta();
            if (dto.numeroParcelas < proposta.getParcelasMinimas() || 
                dto.numeroParcelas > proposta.getParcelasMaximas()) {
                throw new AppException(
                    String.format("Número de parcelas deve estar entre %d e %d", 
                                 proposta.getParcelasMinimas(), proposta.getParcelasMaximas()),
                    400
                );
            }
            
            // Validação: Verificar saldo do credor
            Credor credor = proposta.getCredor();
            if (credor.getSaldoDisponivel() < proposta.getValorDisponivel()) {
                throw new AppException(
                    String.format("Saldo insuficiente. Disponível: R$ %.2f, Necessário: R$ %.2f",
                                 credor.getSaldoDisponivel(), proposta.getValorDisponivel()),
                    400
                );
            }
            
            // Registrar confirmação do credor
            interesse.setConfirmacaoCredor(true);
            interesse.setDataConfirmacaoCredor(LocalDateTime.now());
            em.merge(interesse);
            
            // Se ambos confirmaram, criar empréstimo
            if (interesse.getConfirmacaoDevedor()) {
                Emprestimo emprestimo = criarEmprestimo(em, interesse, dto.numeroParcelas);
                em.getTransaction().commit();
                
                return Response.ok()
                    .entity(new ConfirmacaoResponse(
                        "Empréstimo criado com sucesso",
                        interesse,
                        emprestimo
                    ))
                    .build();
            } else {
                // Notificar devedor para confirmar
                NotificacaoService.criarNotificacao(
                    em, "DEVEDOR", interesse.getDevedor().getId(),
                    "CONFIRMACAO_PENDENTE",
                    "Aguardando sua Confirmação",
                    "O credor confirmou o empréstimo. Confirme para finalizar."
                );
                
                em.getTransaction().commit();
                
                return Response.ok()
                    .entity(new ConfirmacaoResponse(
                        "Confirmação registrada. Aguardando confirmação do devedor.",
                        interesse,
                        null
                    ))
                    .build();
            }
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao registrar empréstimo: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar interesses aprovados aguardando confirmação do credor
     * GET /api/credor/registrar-emprestimo/pendentes/{credorId}
     */
    @GET
    @Path("/pendentes/{credorId}")
    public Response listarPendentes(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var interesses = em.createQuery(
                "SELECT i FROM InteresseProposta i " +
                "WHERE i.proposta.credor.id = :credorId " +
                "AND i.status = 'APROVADO' " +
                "AND i.confirmacaoCredor = false " +
                "ORDER BY i.dataInteresse DESC",
                InteresseProposta.class
            )
            .setParameter("credorId", credorId)
            .getResultList();
            
            return Response.ok(interesses).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar empréstimos do credor
     * GET /api/credor/registrar-emprestimo/meus/{credorId}
     */
    @GET
    @Path("/meus/{credorId}")
    public Response listarMeusEmprestimos(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var emprestimos = em.createQuery(
                "SELECT e FROM Emprestimo e WHERE e.credor.id = :credorId ORDER BY e.dataInicio DESC",
                Emprestimo.class
            )
            .setParameter("credorId", credorId)
            .getResultList();
            
            return Response.ok(emprestimos).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Método privado para criar empréstimo após confirmação bilateral
     */
    private Emprestimo criarEmprestimo(EntityManager em, InteresseProposta interesse, Integer numeroParcelas) {
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
        emprestimo.setStatus(StatusEmprestimo.EM_ANDAMENTO);
        
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
        
        return emprestimo;
    }
    
    /**
     * DTO para confirmação com número de parcelas
     */
    public static class ConfirmacaoDTO {
        public Integer numeroParcelas;
    }
    
    /**
     * Response para confirmação
     */
    public static class ConfirmacaoResponse {
        public String mensagem;
        public InteresseProposta interesse;
        public Emprestimo emprestimo;
        
        public ConfirmacaoResponse(String mensagem, InteresseProposta interesse, Emprestimo emprestimo) {
            this.mensagem = mensagem;
            this.interesse = interesse;
            this.emprestimo = emprestimo;
        }
    }
}

// Made with Bob
