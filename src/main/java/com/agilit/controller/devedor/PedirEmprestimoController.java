package com.agilit.controller.devedor;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UC-D05: Pedir Empréstimo
 * <<include>> Registrar Empréstimo
 * 
 * Confirma o pedido de empréstimo pelo devedor após aprovação do credor
 * 
 * Endpoint: POST /api/devedor/pedir-emprestimo/{interesseId}/confirmar
 * 
 * Body exemplo:
 * {
 *   "numeroParcelas": 12
 * }
 * 
 * Regras de Negócio:
 * - Interesse deve estar aprovado pelo credor
 * - Devedor não pode ter confirmado ainda
 * - Número de parcelas deve estar dentro do range da proposta
 * - Se ambos confirmaram, cria o empréstimo automaticamente
 */
@Path("/devedor/pedir-emprestimo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedirEmprestimoController {
    
    /**
     * Confirmar pedido de empréstimo pelo devedor
     */
    @POST
    @Path("/{interesseId}/confirmar")
    public Response confirmarPedido(
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
                throw new AppException("Interesse deve estar aprovado pelo credor para confirmação", 400);
            }
            
            // Validação: Devedor não pode ter confirmado ainda
            if (interesse.getConfirmacaoDevedor()) {
                throw new AppException("Você já confirmou este pedido de empréstimo", 400);
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
            
            // Registrar confirmação do devedor
            interesse.setConfirmacaoDevedor(true);
            interesse.setDataConfirmacaoDevedor(LocalDateTime.now());
            em.merge(interesse);
            
            // Se ambos confirmaram, criar empréstimo
            if (interesse.getConfirmacaoCredor()) {
                Emprestimo emprestimo = criarEmprestimo(em, interesse, dto.numeroParcelas);
                em.getTransaction().commit();
                
                return Response.ok()
                    .entity(new ConfirmacaoResponse(
                        "Empréstimo criado com sucesso! O valor será disponibilizado em breve.",
                        interesse,
                        emprestimo
                    ))
                    .build();
            } else {
                // Notificar credor para confirmar
                NotificacaoService.criarNotificacao(
                    em, "CREDOR", proposta.getCredor().getId(),
                    "CONFIRMACAO_PENDENTE",
                    "Aguardando sua Confirmação",
                    String.format("O devedor %s confirmou interesse na proposta %s", 
                                 interesse.getDevedor().getNome(), proposta.getIdPublico())
                );
                
                em.getTransaction().commit();
                
                return Response.ok()
                    .entity(new ConfirmacaoResponse(
                        "Confirmação registrada. Aguardando confirmação do credor.",
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
            throw new AppException("Erro ao confirmar pedido: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar interesses aprovados aguardando confirmação do devedor
     * GET /api/devedor/pedir-emprestimo/pendentes/{devedorId}
     */
    @GET
    @Path("/pendentes/{devedorId}")
    public Response listarPendentes(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var interesses = em.createQuery(
                "SELECT i FROM InteresseProposta i " +
                "WHERE i.devedor.id = :devedorId " +
                "AND i.status = 'APROVADO' " +
                "AND i.confirmacaoDevedor = false " +
                "ORDER BY i.dataInteresse DESC",
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
     * Listar meus empréstimos
     * GET /api/devedor/pedir-emprestimo/meus/{devedorId}
     */
    @GET
    @Path("/meus/{devedorId}")
    public Response listarMeusEmprestimos(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var emprestimos = em.createQuery(
                "SELECT e FROM Emprestimo e WHERE e.devedor.id = :devedorId ORDER BY e.dataInicio DESC",
                Emprestimo.class
            )
            .setParameter("devedorId", devedorId)
            .getResultList();
            
            return Response.ok(emprestimos).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Obter detalhes de um empréstimo específico
     * GET /api/devedor/pedir-emprestimo/emprestimo/{emprestimoId}
     */
    @GET
    @Path("/emprestimo/{emprestimoId}")
    public Response obterDetalhesEmprestimo(@PathParam("emprestimoId") Long emprestimoId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Emprestimo emprestimo = em.find(Emprestimo.class, emprestimoId);
            
            if (emprestimo == null) {
                throw new AppException("Empréstimo não encontrado", 404);
            }
            
            // Calcular estatísticas
            int parcelasRestantes = emprestimo.getNumeroParcelas() - emprestimo.getParcelasPagas();
            double valorRestante = emprestimo.getValorTotal() - 
                (emprestimo.getValorTotal() / emprestimo.getNumeroParcelas() * emprestimo.getParcelasPagas());
            
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("emprestimo", emprestimo);
            detalhes.put("parcelasRestantes", parcelasRestantes);
            detalhes.put("valorRestante", CalculadoraEmprestimo.arredondar(valorRestante));
            detalhes.put("percentualPago", (emprestimo.getParcelasPagas() * 100.0) / emprestimo.getNumeroParcelas());
            
            return Response.ok(detalhes).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Simular parcelas antes de confirmar
     * POST /api/devedor/pedir-emprestimo/{interesseId}/simular
     */
    @POST
    @Path("/{interesseId}/simular")
    public Response simularParcelas(
            @PathParam("interesseId") Long interesseId,
            SimulacaoDTO dto) {
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            InteresseProposta interesse = em.find(InteresseProposta.class, interesseId);
            
            if (interesse == null) {
                throw new AppException("Interesse não encontrado", 404);
            }
            
            if (dto.numeroParcelas == null) {
                throw new AppException("Número de parcelas é obrigatório", 400);
            }
            
            PropostaEmprestimo proposta = interesse.getProposta();
            
            // Validar número de parcelas
            if (dto.numeroParcelas < proposta.getParcelasMinimas() || 
                dto.numeroParcelas > proposta.getParcelasMaximas()) {
                throw new AppException(
                    String.format("Número de parcelas deve estar entre %d e %d", 
                        proposta.getParcelasMinimas(), proposta.getParcelasMaximas()),
                    400
                );
            }
            
            // Calcular valores
            double valorPrincipal = proposta.getValorDisponivel();
            double juros = CalculadoraEmprestimo.calcularJurosSimples(
                valorPrincipal, proposta.getTaxaJuros(), dto.numeroParcelas
            );
            double valorTotal = valorPrincipal + juros;
            double valorParcela = valorTotal / dto.numeroParcelas;
            
            // Calcular datas
            List<LocalDate> datasParcelas = CalculadoraEmprestimo.calcularDatasParcelas(
                LocalDate.now(), dto.numeroParcelas, proposta.getDiasAtePrimeiraCobranca()
            );
            
            Map<String, Object> simulacao = new HashMap<>();
            simulacao.put("valorPrincipal", valorPrincipal);
            simulacao.put("juros", CalculadoraEmprestimo.arredondar(juros));
            simulacao.put("valorTotal", CalculadoraEmprestimo.arredondar(valorTotal));
            simulacao.put("numeroParcelas", dto.numeroParcelas);
            simulacao.put("valorParcela", CalculadoraEmprestimo.arredondar(valorParcela));
            simulacao.put("taxaJuros", proposta.getTaxaJuros());
            simulacao.put("primeiraParcela", datasParcelas.get(0));
            simulacao.put("ultimaParcela", datasParcelas.get(datasParcelas.size() - 1));
            
            return Response.ok(simulacao).build();
            
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
     * DTO para simulação
     */
    public static class SimulacaoDTO {
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

 
