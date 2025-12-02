package com.agilit.dao;

import com.agilit.model.PropostaEmprestimo;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a PropostaEmprestimo.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class PropostaEmprestimoDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public PropostaEmprestimoDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar proposta por ID
     * @param id ID da proposta
     * @return PropostaEmprestimo ou null
     */
    public PropostaEmprestimo findById(Long id) {
        return em.find(PropostaEmprestimo.class, id);
    }

    /**
     * Buscar proposta por ID público (#ABC123)
     * @param idPublico ID público da proposta
     * @return PropostaEmprestimo ou null
     */
    public PropostaEmprestimo findByIdPublico(String idPublico) {
        List<PropostaEmprestimo> result = em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.idPublico = :idPublico",
            PropostaEmprestimo.class
        )
        .setParameter("idPublico", idPublico)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Buscar todas as propostas
     * @return Lista de propostas
     */
    public List<PropostaEmprestimo> findAll() {
        return em.createQuery("SELECT p FROM PropostaEmprestimo p ORDER BY p.dataCriacao DESC", 
                             PropostaEmprestimo.class)
                .getResultList();
    }

    /**
     * Buscar propostas por status
     * @param status Status da proposta (ATIVA, CANCELADA, ACEITA)
     * @return Lista de propostas
     */
    public List<PropostaEmprestimo> findByStatus(String status) {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.status = :status ORDER BY p.dataCriacao DESC",
            PropostaEmprestimo.class
        )
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar propostas ativas públicas
     * @return Lista de propostas ativas
     */
    public List<PropostaEmprestimo> findAtivasPublicas() {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA' ORDER BY p.dataCriacao DESC",
            PropostaEmprestimo.class
        )
        .getResultList();
    }

    /**
     * Buscar propostas por credor
     * @param credorId ID do credor
     * @return Lista de propostas do credor
     */
    public List<PropostaEmprestimo> findByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.credor.id = :credorId ORDER BY p.dataCriacao DESC",
            PropostaEmprestimo.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Buscar propostas por credor e status
     * @param credorId ID do credor
     * @param status Status da proposta
     * @return Lista de propostas
     */
    public List<PropostaEmprestimo> findByCredorIdAndStatus(Long credorId, String status) {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.credor.id = :credorId AND p.status = :status ORDER BY p.dataCriacao DESC",
            PropostaEmprestimo.class
        )
        .setParameter("credorId", credorId)
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar propostas por oferta origem
     * @param ofertaId ID da oferta
     * @return Lista de propostas
     */
    public List<PropostaEmprestimo> findByOfertaId(Long ofertaId) {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.ofertaOrigem.id = :ofertaId ORDER BY p.dataCriacao DESC",
            PropostaEmprestimo.class
        )
        .setParameter("ofertaId", ofertaId)
        .getResultList();
    }

    /**
     * Salvar ou atualizar proposta
     * @param proposta Proposta a ser salva
     * @return Proposta persistida
     */
    public PropostaEmprestimo save(PropostaEmprestimo proposta) {
        if (proposta.getId() == null) {
            em.persist(proposta);
            return proposta;
        } else {
            return em.merge(proposta);
        }
    }

    /**
     * Deletar proposta
     * @param proposta Proposta a ser deletada
     */
    public void delete(PropostaEmprestimo proposta) {
        if (!em.contains(proposta)) {
            proposta = em.merge(proposta);
        }
        em.remove(proposta);
    }

    /**
     * Deletar proposta por ID
     * @param id ID da proposta
     */
    public void deleteById(Long id) {
        PropostaEmprestimo proposta = findById(id);
        if (proposta != null) {
            delete(proposta);
        }
    }

    /**
     * Contar propostas por credor
     * @param credorId ID do credor
     * @return Número de propostas
     */
    public Long countByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
    }

    /**
     * Contar propostas ativas
     * @return Número de propostas ativas
     */
    public Long countAtivas() {
        return em.createQuery(
            "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.status = 'ATIVA'",
            Long.class
        )
        .getSingleResult();
    }

    /**
     * Verificar se proposta tem interesses
     * @param propostaId ID da proposta
     * @return true se tem interesses
     */
    public boolean temInteresses(Long propostaId) {
        Long count = em.createQuery(
            "SELECT COUNT(i) FROM InteresseProposta i WHERE i.proposta.id = :propostaId",
            Long.class
        )
        .setParameter("propostaId", propostaId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Buscar propostas por faixa de valor
     * @param valorMin Valor mínimo
     * @param valorMax Valor máximo
     * @return Lista de propostas
     */
    public List<PropostaEmprestimo> findByValorRange(Double valorMin, Double valorMax) {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.valorSolicitado BETWEEN :min AND :max AND p.status = 'ATIVA' ORDER BY p.valorSolicitado",
            PropostaEmprestimo.class
        )
        .setParameter("min", valorMin)
        .setParameter("max", valorMax)
        .getResultList();
    }

    /**
     * Buscar propostas por número de parcelas
     * @param numeroParcelas Número de parcelas desejado
     * @return Lista de propostas
     */
    public List<PropostaEmprestimo> findByNumeroParcelas(Integer numeroParcelas) {
        return em.createQuery(
            "SELECT p FROM PropostaEmprestimo p WHERE p.numeroParcelas = :parcelas AND p.status = 'ATIVA' ORDER BY p.dataCriacao DESC",
            PropostaEmprestimo.class
        )
        .setParameter("parcelas", numeroParcelas)
        .getResultList();
    }

    /**
     * Buscar propostas com filtros múltiplos
     * @param valorMin Valor mínimo (opcional)
     * @param valorMax Valor máximo (opcional)
     * @param parcelasMin Parcelas mínimas (opcional)
     * @param parcelasMax Parcelas máximas (opcional)
     * @return Lista de propostas filtradas
     */
    public List<PropostaEmprestimo> findComFiltros(Double valorMin, Double valorMax, 
                                                    Integer parcelasMin, Integer parcelasMax) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM PropostaEmprestimo p WHERE p.status = 'ATIVA'");
        
        if (valorMin != null) {
            jpql.append(" AND p.valorSolicitado >= :valorMin");
        }
        if (valorMax != null) {
            jpql.append(" AND p.valorSolicitado <= :valorMax");
        }
        if (parcelasMin != null) {
            jpql.append(" AND p.numeroParcelas >= :parcelasMin");
        }
        if (parcelasMax != null) {
            jpql.append(" AND p.numeroParcelas <= :parcelasMax");
        }
        
        jpql.append(" ORDER BY p.dataCriacao DESC");
        
        var query = em.createQuery(jpql.toString(), PropostaEmprestimo.class);
        
        if (valorMin != null) query.setParameter("valorMin", valorMin);
        if (valorMax != null) query.setParameter("valorMax", valorMax);
        if (parcelasMin != null) query.setParameter("parcelasMin", parcelasMin);
        if (parcelasMax != null) query.setParameter("parcelasMax", parcelasMax);
        
        return query.getResultList();
    }
}

 
