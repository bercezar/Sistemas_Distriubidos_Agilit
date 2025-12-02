package com.agilit.util;

import com.agilit.model.*;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

/**
 * Serviço para criação e envio de notificações.
 * Centraliza a lógica de notificação para diferentes eventos do sistema.
 */
public class NotificacaoService {

    /**
     * Notifica o Credor sobre um novo interesse em sua proposta
     * 
     * @param em EntityManager (transação deve estar ativa)
     * @param credor Credor a ser notificado
     * @param proposta Proposta que recebeu interesse
     * @param devedor Devedor que demonstrou interesse
     */
    public static void notificarNovoInteresse(EntityManager em, Credor credor, 
                                              PropostaEmprestimo proposta, Devedor devedor) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario("CREDOR");
        notificacao.setDestinatarioId(credor.getId());
        notificacao.setTipo("NOVO_INTERESSE");
        notificacao.setTitulo("Novo Interesse em sua Proposta");
        notificacao.setMensagem(
            String.format("O devedor %s demonstrou interesse na proposta %s no valor de R$ %.2f",
                         devedor.getNome(), proposta.getIdPublico(), proposta.getValorDisponivel())
        );
        notificacao.setReferencia(proposta.getId().toString());
        notificacao.setTipoReferencia("PROPOSTA");
        
        em.persist(notificacao);
    }

    /**
     * Notifica o Devedor sobre aprovação de seu interesse
     * 
     * @param em EntityManager
     * @param devedor Devedor a ser notificado
     * @param interesse Interesse aprovado
     */
    public static void notificarAprovacao(EntityManager em, Devedor devedor, 
                                         InteresseProposta interesse) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario("DEVEDOR");
        notificacao.setDestinatarioId(devedor.getId());
        notificacao.setTipo("APROVACAO");
        notificacao.setTitulo("Seu Interesse foi Aprovado");
        notificacao.setMensagem(
            String.format("O credor aprovou seu interesse na proposta %s. Aguardando sua confirmação.",
                         interesse.getProposta().getIdPublico())
        );
        notificacao.setReferencia(interesse.getId().toString());
        notificacao.setTipoReferencia("INTERESSE");
        
        em.persist(notificacao);
    }

    /**
     * Notifica sobre confirmação do empréstimo
     * 
     * @param em EntityManager
     * @param tipoDestinatario CREDOR ou DEVEDOR
     * @param destinatarioId ID do destinatário
     * @param emprestimo Empréstimo confirmado
     */
    public static void notificarConfirmacao(EntityManager em, String tipoDestinatario, 
                                           Long destinatarioId, Emprestimo emprestimo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario(tipoDestinatario);
        notificacao.setDestinatarioId(destinatarioId);
        notificacao.setTipo("CONFIRMACAO");
        notificacao.setTitulo("Empréstimo Confirmado");
        
        if ("CREDOR".equals(tipoDestinatario)) {
            notificacao.setMensagem(
                String.format("Empréstimo de R$ %.2f para %s foi confirmado e está ativo.",
                             emprestimo.getValorTotal(), emprestimo.getDevedor().getNome())
            );
        } else {
            notificacao.setMensagem(
                String.format("Seu empréstimo de R$ %.2f foi confirmado. Total de %d parcelas.",
                             emprestimo.getValorTotal(), emprestimo.getNumeroParcelas())
            );
        }
        
        notificacao.setReferencia(emprestimo.getId().toString());
        notificacao.setTipoReferencia("EMPRESTIMO");
        
        em.persist(notificacao);
    }

    /**
     * Notifica o Devedor sobre vencimento próximo de parcela
     * 
     * @param em EntityManager
     * @param devedor Devedor a ser notificado
     * @param parcela Parcela próxima do vencimento
     */
    public static void notificarVencimento(EntityManager em, Devedor devedor, Parcela parcela) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario("DEVEDOR");
        notificacao.setDestinatarioId(devedor.getId());
        notificacao.setTipo("VENCIMENTO");
        notificacao.setTitulo("Parcela Próxima do Vencimento");
        notificacao.setMensagem(
            String.format("A parcela %d/%d no valor de R$ %.2f vence em %s",
                         parcela.getNumeroParcela(), 
                         parcela.getEmprestimo().getNumeroParcelas(),
                         parcela.getValor(),
                         parcela.getDataVencimento())
        );
        notificacao.setReferencia(parcela.getId().toString());
        notificacao.setTipoReferencia("PARCELA");
        
        em.persist(notificacao);
    }

    /**
     * Notifica sobre parcela atrasada
     * 
     * @param em EntityManager
     * @param devedor Devedor a ser notificado
     * @param parcela Parcela atrasada
     */
    public static void notificarAtraso(EntityManager em, Devedor devedor, Parcela parcela) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario("DEVEDOR");
        notificacao.setDestinatarioId(devedor.getId());
        notificacao.setTipo("ATRASO");
        notificacao.setTitulo("Parcela em Atraso");
        notificacao.setMensagem(
            String.format("A parcela %d/%d no valor de R$ %.2f está atrasada desde %s. Por favor, regularize.",
                         parcela.getNumeroParcela(),
                         parcela.getEmprestimo().getNumeroParcelas(),
                         parcela.getValor(),
                         parcela.getDataVencimento())
        );
        notificacao.setReferencia(parcela.getId().toString());
        notificacao.setTipoReferencia("PARCELA");
        
        em.persist(notificacao);
    }

    /**
     * Notifica o Credor sobre pagamento de parcela
     * 
     * @param em EntityManager
     * @param credor Credor a ser notificado
     * @param parcela Parcela paga
     */
    public static void notificarPagamento(EntityManager em, Credor credor, Parcela parcela) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario("CREDOR");
        notificacao.setDestinatarioId(credor.getId());
        notificacao.setTipo("PAGAMENTO");
        notificacao.setTitulo("Parcela Paga");
        notificacao.setMensagem(
            String.format("O devedor %s pagou a parcela %d/%d no valor de R$ %.2f",
                         parcela.getEmprestimo().getDevedor().getNome(),
                         parcela.getNumeroParcela(),
                         parcela.getEmprestimo().getNumeroParcelas(),
                         parcela.getValor())
        );
        notificacao.setReferencia(parcela.getId().toString());
        notificacao.setTipoReferencia("PARCELA");
        
        em.persist(notificacao);
    }

    /**
     * Notifica sobre empréstimo quitado
     * 
     * @param em EntityManager
     * @param tipoDestinatario CREDOR ou DEVEDOR
     * @param destinatarioId ID do destinatário
     * @param emprestimo Empréstimo quitado
     */
    public static void notificarQuitacao(EntityManager em, String tipoDestinatario,
                                        Long destinatarioId, Emprestimo emprestimo) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario(tipoDestinatario);
        notificacao.setDestinatarioId(destinatarioId);
        notificacao.setTipo("QUITACAO");
        notificacao.setTitulo("Empréstimo Quitado");
        
        if ("CREDOR".equals(tipoDestinatario)) {
            notificacao.setMensagem(
                String.format("O empréstimo de %s no valor de R$ %.2f foi totalmente quitado.",
                             emprestimo.getDevedor().getNome(), emprestimo.getValorTotal())
            );
        } else {
            notificacao.setMensagem(
                String.format("Parabéns! Seu empréstimo de R$ %.2f foi totalmente quitado.",
                             emprestimo.getValorTotal())
            );
        }
        
        notificacao.setReferencia(emprestimo.getId().toString());
        notificacao.setTipoReferencia("EMPRESTIMO");
        
        em.persist(notificacao);
    }

    /**
     * Notifica sobre rejeição de interesse
     * 
     * @param em EntityManager
     * @param devedor Devedor a ser notificado
     * @param interesse Interesse rejeitado
     */
    public static void notificarRejeicao(EntityManager em, Devedor devedor,
                                        InteresseProposta interesse) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario("DEVEDOR");
        notificacao.setDestinatarioId(devedor.getId());
        notificacao.setTipo("REJEICAO");
        notificacao.setTitulo("Interesse Não Aprovado");
        notificacao.setMensagem(
            String.format("Seu interesse na proposta %s não foi aprovado pelo credor.",
                         interesse.getProposta().getIdPublico())
        );
        notificacao.setReferencia(interesse.getId().toString());
        notificacao.setTipoReferencia("INTERESSE");
        
        em.persist(notificacao);
    }

    /**
     * Cria uma notificação genérica
     * 
     * @param em EntityManager
     * @param tipoDestinatario CREDOR ou DEVEDOR
     * @param destinatarioId ID do destinatário
     * @param tipo Tipo da notificação
     * @param titulo Título
     * @param mensagem Mensagem
     */
    public static void criarNotificacao(EntityManager em, String tipoDestinatario,
                                       Long destinatarioId, String tipo,
                                       String titulo, String mensagem) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipoDestinatario(tipoDestinatario);
        notificacao.setDestinatarioId(destinatarioId);
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setDataCriacao(LocalDateTime.now());
        notificacao.setLida(false);
        
        em.persist(notificacao);
    }
}

// Made with Bob
