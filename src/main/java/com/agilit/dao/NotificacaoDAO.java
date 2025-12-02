package com.agilit.dao;

import com.agilit.model.Notificacao;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a Notificacao.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class NotificacaoDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public NotificacaoDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar notificação por ID
     * @param id ID da notificação
     * @return Notificacao ou null
     */
    public Notificacao findById(Long id) {
        return em.find(Notificacao.class, id);
    }

    /**
     * Buscar todas as notificações
     * @return Lista de notificações
     */
    public List<Notificacao> findAll() {
        return em.createQuery("SELECT n FROM Notificacao n ORDER BY n.dataCriacao DESC", 
                             Notificacao.class)
                .getResultList();
    }

    /**
     * Buscar notificações por destinatário
     * @param tipoDestinatario Tipo do destinatário (CREDOR ou DEVEDOR)
     * @param destinatarioId ID do destinatário
     * @return Lista de notificações
     */
    public List<Notificacao> findByDestinatario(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .getResultList();
    }

    /**
     * Buscar notificações não lidas por destinatário
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return Lista de notificações não lidas
     */
    public List<Notificacao> findNaoLidasByDestinatario(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = false ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .getResultList();
    }

    /**
     * Buscar notificações lidas por destinatário
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return Lista de notificações lidas
     */
    public List<Notificacao> findLidasByDestinatario(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = true ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .getResultList();
    }

    /**
     * Buscar notificações por tipo
     * @param tipo Tipo da notificação (INTERESSE, APROVACAO, CONFIRMACAO, etc.)
     * @return Lista de notificações
     */
    public List<Notificacao> findByTipo(String tipo) {
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipo = :tipo ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipo", tipo)
        .getResultList();
    }

    /**
     * Buscar notificações por destinatário e tipo
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @param tipoNotificacao Tipo da notificação
     * @return Lista de notificações
     */
    public List<Notificacao> findByDestinatarioAndTipo(String tipoDestinatario, Long destinatarioId, String tipoNotificacao) {
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipoDestinatario AND n.destinatarioId = :id AND n.tipo = :tipoNotificacao ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipoDestinatario", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .setParameter("tipoNotificacao", tipoNotificacao)
        .getResultList();
    }

    /**
     * Buscar notificações recentes (últimas N horas)
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @param horas Número de horas
     * @return Lista de notificações recentes
     */
    public List<Notificacao> findRecentesByDestinatario(String tipoDestinatario, Long destinatarioId, int horas) {
        LocalDateTime dataLimite = LocalDateTime.now().minusHours(horas);
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.dataCriacao >= :dataLimite ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .setParameter("dataLimite", dataLimite)
        .getResultList();
    }

    /**
     * Salvar ou atualizar notificação
     * @param notificacao Notificação a ser salva
     * @return Notificação persistida
     */
    public Notificacao save(Notificacao notificacao) {
        if (notificacao.getId() == null) {
            em.persist(notificacao);
            return notificacao;
        } else {
            return em.merge(notificacao);
        }
    }

    /**
     * Salvar lista de notificações
     * @param notificacoes Lista de notificações
     */
    public void saveAll(List<Notificacao> notificacoes) {
        for (Notificacao notificacao : notificacoes) {
            save(notificacao);
        }
    }

    /**
     * Deletar notificação
     * @param notificacao Notificação a ser deletada
     */
    public void delete(Notificacao notificacao) {
        if (!em.contains(notificacao)) {
            notificacao = em.merge(notificacao);
        }
        em.remove(notificacao);
    }

    /**
     * Deletar notificação por ID
     * @param id ID da notificação
     */
    public void deleteById(Long id) {
        Notificacao notificacao = findById(id);
        if (notificacao != null) {
            delete(notificacao);
        }
    }

    /**
     * Deletar todas as notificações lidas de um destinatário
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return Número de notificações deletadas
     */
    public int deleteLidasByDestinatario(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "DELETE FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = true"
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .executeUpdate();
    }

    /**
     * Deletar notificações antigas (mais de N dias)
     * @param dias Número de dias
     * @return Número de notificações deletadas
     */
    public int deleteAntigas(int dias) {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(dias);
        return em.createQuery(
            "DELETE FROM Notificacao n WHERE n.dataCriacao < :dataLimite AND n.lida = true"
        )
        .setParameter("dataLimite", dataLimite)
        .executeUpdate();
    }

    /**
     * Marcar notificação como lida
     * @param id ID da notificação
     * @return true se foi atualizada
     */
    public boolean marcarComoLida(Long id) {
        Notificacao notificacao = findById(id);
        if (notificacao != null && !notificacao.getLida()) {
            notificacao.marcarComoLida();
            save(notificacao);
            return true;
        }
        return false;
    }

    /**
     * Marcar todas as notificações de um destinatário como lidas
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return Número de notificações marcadas
     */
    public int marcarTodasComoLidas(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "UPDATE Notificacao n SET n.lida = true WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = false"
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .executeUpdate();
    }

    /**
     * Contar notificações de um destinatário
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return Número de notificações
     */
    public Long countByDestinatario(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "SELECT COUNT(n) FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id",
            Long.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .getSingleResult();
    }

    /**
     * Contar notificações não lidas de um destinatário
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return Número de notificações não lidas
     */
    public Long countNaoLidasByDestinatario(String tipoDestinatario, Long destinatarioId) {
        return em.createQuery(
            "SELECT COUNT(n) FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id AND n.lida = false",
            Long.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .getSingleResult();
    }

    /**
     * Contar notificações por tipo
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @param tipoNotificacao Tipo da notificação
     * @return Número de notificações
     */
    public Long countByDestinatarioAndTipo(String tipoDestinatario, Long destinatarioId, String tipoNotificacao) {
        return em.createQuery(
            "SELECT COUNT(n) FROM Notificacao n WHERE n.tipoDestinatario = :tipoDestinatario AND n.destinatarioId = :id AND n.tipo = :tipoNotificacao",
            Long.class
        )
        .setParameter("tipoDestinatario", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .setParameter("tipoNotificacao", tipoNotificacao)
        .getSingleResult();
    }

    /**
     * Verificar se destinatário tem notificações não lidas
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @return true se tem notificações não lidas
     */
    public boolean temNotificacaoNaoLida(String tipoDestinatario, Long destinatarioId) {
        Long count = countNaoLidasByDestinatario(tipoDestinatario, destinatarioId);
        return count > 0;
    }

    /**
     * Buscar últimas N notificações de um destinatário
     * @param tipoDestinatario Tipo do destinatário
     * @param destinatarioId ID do destinatário
     * @param limite Número máximo de notificações
     * @return Lista de notificações
     */
    public List<Notificacao> findUltimasNotificacoes(String tipoDestinatario, Long destinatarioId, int limite) {
        return em.createQuery(
            "SELECT n FROM Notificacao n WHERE n.tipoDestinatario = :tipo AND n.destinatarioId = :id ORDER BY n.dataCriacao DESC",
            Notificacao.class
        )
        .setParameter("tipo", tipoDestinatario)
        .setParameter("id", destinatarioId)
        .setMaxResults(limite)
        .getResultList();
    }
}

 
