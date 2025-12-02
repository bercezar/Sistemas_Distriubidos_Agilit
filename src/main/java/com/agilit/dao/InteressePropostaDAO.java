package com.agilit.dao;

import com.agilit.model.InteresseProposta;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a InteresseProposta.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class InteressePropostaDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public InteressePropostaDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar interesse por ID
     * @param id ID do interesse
     * @return InteresseProposta ou null
     */
    public InteresseProposta findById(Long id) {
        return em.find(InteresseProposta.class, id);
    }

    /**
     * Buscar todos os interesses
     * @return Lista de interesses
     */
    public List<InteresseProposta> findAll() {
        return em.createQuery("SELECT i FROM InteresseProposta i ORDER BY i.dataInteresse DESC", 
                             InteresseProposta.class)
                .getResultList();
    }

    /**
     * Buscar interesses por proposta
     * @param propostaId ID da proposta
     * @return Lista de interesses
     */
    public List<InteresseProposta> findByPropostaId(Long propostaId) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.id = :propostaId ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .setParameter("propostaId", propostaId)
        .getResultList();
    }

    /**
     * Buscar interesses por devedor
     * @param devedorId ID do devedor
     * @return Lista de interesses
     */
    public List<InteresseProposta> findByDevedorId(Long devedorId) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.devedor.id = :devedorId ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .setParameter("devedorId", devedorId)
        .getResultList();
    }

    /**
     * Buscar interesses por status
     * @param status Status do interesse (PENDENTE, APROVADO, REJEITADO, CANCELADO)
     * @return Lista de interesses
     */
    public List<InteresseProposta> findByStatus(String status) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.status = :status ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar interesses por proposta e status
     * @param propostaId ID da proposta
     * @param status Status do interesse
     * @return Lista de interesses
     */
    public List<InteresseProposta> findByPropostaIdAndStatus(Long propostaId, String status) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = :status ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .setParameter("propostaId", propostaId)
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar interesses por devedor e status
     * @param devedorId ID do devedor
     * @param status Status do interesse
     * @return Lista de interesses
     */
    public List<InteresseProposta> findByDevedorIdAndStatus(Long devedorId, String status) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.devedor.id = :devedorId AND i.status = :status ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .setParameter("devedorId", devedorId)
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Verificar se já existe interesse do devedor na proposta
     * @param propostaId ID da proposta
     * @param devedorId ID do devedor
     * @return true se já existe interesse
     */
    public boolean existsByPropostaAndDevedor(Long propostaId, Long devedorId) {
        Long count = em.createQuery(
            "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.devedor.id = :devedorId",
            Long.class
        )
        .setParameter("propostaId", propostaId)
        .setParameter("devedorId", devedorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Buscar interesse específico por proposta e devedor
     * @param propostaId ID da proposta
     * @param devedorId ID do devedor
     * @return InteresseProposta ou null
     */
    public InteresseProposta findByPropostaAndDevedor(Long propostaId, Long devedorId) {
        List<InteresseProposta> result = em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.devedor.id = :devedorId",
            InteresseProposta.class
        )
        .setParameter("propostaId", propostaId)
        .setParameter("devedorId", devedorId)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Buscar interesses pendentes de aprovação (aguardando credor)
     * @param propostaId ID da proposta
     * @return Lista de interesses pendentes
     */
    public List<InteresseProposta> findPendentesAprovacao(Long propostaId) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'PENDENTE' ORDER BY i.dataInteresse ASC",
            InteresseProposta.class
        )
        .setParameter("propostaId", propostaId)
        .getResultList();
    }

    /**
     * Buscar interesses aprovados aguardando confirmação
     * @param propostaId ID da proposta
     * @return Lista de interesses aprovados
     */
    public List<InteresseProposta> findAprovadosAguardandoConfirmacao(Long propostaId) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'APROVADO' AND (i.confirmacaoCredor = false OR i.confirmacaoDevedor = false) ORDER BY i.dataInteresse ASC",
            InteresseProposta.class
        )
        .setParameter("propostaId", propostaId)
        .getResultList();
    }

    /**
     * Buscar interesses com ambas confirmações
     * @return Lista de interesses confirmados
     */
    public List<InteresseProposta> findComAmbasConfirmacoes() {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.confirmacaoCredor = true AND i.confirmacaoDevedor = true ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .getResultList();
    }

    /**
     * Salvar ou atualizar interesse
     * @param interesse Interesse a ser salvo
     * @return Interesse persistido
     */
    public InteresseProposta save(InteresseProposta interesse) {
        if (interesse.getId() == null) {
            em.persist(interesse);
            return interesse;
        } else {
            return em.merge(interesse);
        }
    }

    /**
     * Deletar interesse
     * @param interesse Interesse a ser deletado
     */
    public void delete(InteresseProposta interesse) {
        if (!em.contains(interesse)) {
            interesse = em.merge(interesse);
        }
        em.remove(interesse);
    }

    /**
     * Deletar interesse por ID
     * @param id ID do interesse
     */
    public void deleteById(Long id) {
        InteresseProposta interesse = findById(id);
        if (interesse != null) {
            delete(interesse);
        }
    }

    /**
     * Contar interesses por proposta
     * @param propostaId ID da proposta
     * @return Número de interesses
     */
    public Long countByPropostaId(Long propostaId) {
        return em.createQuery(
            "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId",
            Long.class
        )
        .setParameter("propostaId", propostaId)
        .getSingleResult();
    }

    /**
     * Contar interesses aprovados por proposta
     * @param propostaId ID da proposta
     * @return Número de interesses aprovados
     */
    public Long countAprovadosByPropostaId(Long propostaId) {
        return em.createQuery(
            "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'APROVADO'",
            Long.class
        )
        .setParameter("propostaId", propostaId)
        .getSingleResult();
    }

    /**
     * Contar interesses pendentes por proposta
     * @param propostaId ID da proposta
     * @return Número de interesses pendentes
     */
    public Long countPendentesByPropostaId(Long propostaId) {
        return em.createQuery(
            "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId AND i.status = 'PENDENTE'",
            Long.class
        )
        .setParameter("propostaId", propostaId)
        .getSingleResult();
    }

    /**
     * Buscar interesses por credor (através da proposta)
     * @param credorId ID do credor
     * @return Lista de interesses
     */
    public List<InteresseProposta> findByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.credor.id = :credorId ORDER BY i.dataInteresse DESC",
            InteresseProposta.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Buscar interesses pendentes de um credor
     * @param credorId ID do credor
     * @return Lista de interesses pendentes
     */
    public List<InteresseProposta> findPendentesByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT i FROM InteresseProposta i WHERE i.proposta.credor.id = :credorId AND i.status = 'PENDENTE' ORDER BY i.dataInteresse ASC",
            InteresseProposta.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }
}

// Made with Bob
