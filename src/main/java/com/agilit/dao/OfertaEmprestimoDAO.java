package com.agilit.dao;

import com.agilit.model.OfertaEmprestimo;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a OfertaEmprestimo.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class OfertaEmprestimoDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public OfertaEmprestimoDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar oferta por ID
     * @param id ID da oferta
     * @return OfertaEmprestimo ou null
     */
    public OfertaEmprestimo findById(Long id) {
        return em.find(OfertaEmprestimo.class, id);
    }

    /**
     * Buscar todas as ofertas
     * @return Lista de ofertas
     */
    public List<OfertaEmprestimo> findAll() {
        return em.createQuery("SELECT o FROM OfertaEmprestimo o ORDER BY o.dataCriacao DESC", 
                             OfertaEmprestimo.class)
                .getResultList();
    }

    /**
     * Buscar ofertas por credor
     * @param credorId ID do credor
     * @return Lista de ofertas do credor
     */
    public List<OfertaEmprestimo> findByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT o FROM OfertaEmprestimo o WHERE o.credor.id = :credorId ORDER BY o.dataCriacao DESC",
            OfertaEmprestimo.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Buscar ofertas ativas por credor
     * @param credorId ID do credor
     * @return Lista de ofertas ativas
     */
    public List<OfertaEmprestimo> findAtivasByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT o FROM OfertaEmprestimo o WHERE o.credor.id = :credorId AND o.ativa = true ORDER BY o.dataCriacao DESC",
            OfertaEmprestimo.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Buscar ofertas por status (ativa/inativa)
     * @param ativa true para ativas, false para inativas
     * @return Lista de ofertas
     */
    public List<OfertaEmprestimo> findByAtiva(Boolean ativa) {
        return em.createQuery(
            "SELECT o FROM OfertaEmprestimo o WHERE o.ativa = :ativa ORDER BY o.dataCriacao DESC",
            OfertaEmprestimo.class
        )
        .setParameter("ativa", ativa)
        .getResultList();
    }

    /**
     * Salvar ou atualizar oferta
     * @param oferta Oferta a ser salva
     * @return Oferta persistida
     */
    public OfertaEmprestimo save(OfertaEmprestimo oferta) {
        if (oferta.getId() == null) {
            em.persist(oferta);
            return oferta;
        } else {
            return em.merge(oferta);
        }
    }

    /**
     * Deletar oferta
     * @param oferta Oferta a ser deletada
     */
    public void delete(OfertaEmprestimo oferta) {
        if (!em.contains(oferta)) {
            oferta = em.merge(oferta);
        }
        em.remove(oferta);
    }

    /**
     * Deletar oferta por ID
     * @param id ID da oferta
     */
    public void deleteById(Long id) {
        OfertaEmprestimo oferta = findById(id);
        if (oferta != null) {
            delete(oferta);
        }
    }

    /**
     * Contar ofertas de um credor
     * @param credorId ID do credor
     * @return Número de ofertas
     */
    public Long countByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT COUNT(o) FROM OfertaEmprestimo o WHERE o.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
    }

    /**
     * Verificar se oferta tem propostas associadas
     * @param ofertaId ID da oferta
     * @return true se tem propostas
     */
    public boolean temPropostas(Long ofertaId) {
        Long count = em.createQuery(
            "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.ofertaOrigem.id = :ofertaId",
            Long.class
        )
        .setParameter("ofertaId", ofertaId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Buscar ofertas por faixa de valor
     * @param valorMin Valor mínimo
     * @param valorMax Valor máximo
     * @return Lista de ofertas
     */
    public List<OfertaEmprestimo> findByValorRange(Double valorMin, Double valorMax) {
        return em.createQuery(
            "SELECT o FROM OfertaEmprestimo o WHERE o.valorDisponivel BETWEEN :min AND :max ORDER BY o.valorDisponivel",
            OfertaEmprestimo.class
        )
        .setParameter("min", valorMin)
        .setParameter("max", valorMax)
        .getResultList();
    }
}