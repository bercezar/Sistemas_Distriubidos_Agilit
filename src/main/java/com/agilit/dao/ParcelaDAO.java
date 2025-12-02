package com.agilit.dao;

import com.agilit.model.Parcela;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a Parcela.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class ParcelaDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public ParcelaDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar parcela por ID
     * @param id ID da parcela
     * @return Parcela ou null
     */
    public Parcela findById(Long id) {
        return em.find(Parcela.class, id);
    }

    /**
     * Buscar todas as parcelas
     * @return Lista de parcelas
     */
    public List<Parcela> findAll() {
        return em.createQuery("SELECT p FROM Parcela p ORDER BY p.dataVencimento ASC", 
                             Parcela.class)
                .getResultList();
    }

    /**
     * Buscar parcelas por empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Lista de parcelas ordenadas por número
     */
    public List<Parcela> findByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId ORDER BY p.numeroParcela ASC",
            Parcela.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getResultList();
    }

    /**
     * Buscar parcelas pagas de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Lista de parcelas pagas
     */
    public List<Parcela> findPagasByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = true ORDER BY p.numeroParcela ASC",
            Parcela.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getResultList();
    }

    /**
     * Buscar parcelas pendentes de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Lista de parcelas pendentes
     */
    public List<Parcela> findPendentesByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false ORDER BY p.numeroParcela ASC",
            Parcela.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getResultList();
    }

    /**
     * Buscar parcelas atrasadas de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Lista de parcelas atrasadas
     */
    public List<Parcela> findAtrasadasByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.atrasada = true ORDER BY p.numeroParcela ASC",
            Parcela.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getResultList();
    }

    /**
     * Buscar todas as parcelas vencidas (não pagas e com data de vencimento passada)
     * @return Lista de parcelas vencidas
     */
    public List<Parcela> findParcelasVencidas() {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.paga = false AND p.dataVencimento < :hoje ORDER BY p.dataVencimento ASC",
            Parcela.class
        )
        .setParameter("hoje", LocalDate.now())
        .getResultList();
    }

    /**
     * Buscar parcelas que vencem hoje
     * @return Lista de parcelas que vencem hoje
     */
    public List<Parcela> findParcelasVencemHoje() {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.paga = false AND p.dataVencimento = :hoje ORDER BY p.emprestimo.id",
            Parcela.class
        )
        .setParameter("hoje", LocalDate.now())
        .getResultList();
    }

    /**
     * Buscar parcelas que vencem nos próximos N dias
     * @param dias Número de dias
     * @return Lista de parcelas
     */
    public List<Parcela> findParcelasVencemEmDias(int dias) {
        LocalDate dataLimite = LocalDate.now().plusDays(dias);
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.paga = false AND p.dataVencimento BETWEEN :hoje AND :dataLimite ORDER BY p.dataVencimento ASC",
            Parcela.class
        )
        .setParameter("hoje", LocalDate.now())
        .setParameter("dataLimite", dataLimite)
        .getResultList();
    }

    /**
     * Buscar próxima parcela a vencer de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Próxima parcela ou null
     */
    public Parcela findProximaParcelaAVencer(Long emprestimoId) {
        List<Parcela> result = em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false ORDER BY p.numeroParcela ASC",
            Parcela.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .setMaxResults(1)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Salvar ou atualizar parcela
     * @param parcela Parcela a ser salva
     * @return Parcela persistida
     */
    public Parcela save(Parcela parcela) {
        if (parcela.getId() == null) {
            em.persist(parcela);
            return parcela;
        } else {
            return em.merge(parcela);
        }
    }

    /**
     * Salvar lista de parcelas
     * @param parcelas Lista de parcelas
     */
    public void saveAll(List<Parcela> parcelas) {
        for (Parcela parcela : parcelas) {
            save(parcela);
        }
    }

    /**
     * Deletar parcela
     * @param parcela Parcela a ser deletada
     */
    public void delete(Parcela parcela) {
        if (!em.contains(parcela)) {
            parcela = em.merge(parcela);
        }
        em.remove(parcela);
    }

    /**
     * Deletar parcela por ID
     * @param id ID da parcela
     */
    public void deleteById(Long id) {
        Parcela parcela = findById(id);
        if (parcela != null) {
            delete(parcela);
        }
    }

    /**
     * Contar parcelas de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Número total de parcelas
     */
    public Long countByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT COUNT(p) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId",
            Long.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getSingleResult();
    }

    /**
     * Contar parcelas pagas de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Número de parcelas pagas
     */
    public Long countPagasByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT COUNT(p) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = true",
            Long.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getSingleResult();
    }

    /**
     * Contar parcelas pendentes de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Número de parcelas pendentes
     */
    public Long countPendentesByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT COUNT(p) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false",
            Long.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getSingleResult();
    }

    /**
     * Contar parcelas atrasadas de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Número de parcelas atrasadas
     */
    public Long countAtrasadasByEmprestimoId(Long emprestimoId) {
        return em.createQuery(
            "SELECT COUNT(p) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.atrasada = true",
            Long.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getSingleResult();
    }

    /**
     * Calcular valor total pago de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Valor total pago
     */
    public Double calcularTotalPago(Long emprestimoId) {
        Double total = em.createQuery(
            "SELECT SUM(p.valor) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = true",
            Double.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getSingleResult();
        
        return total != null ? total : 0.0;
    }

    /**
     * Calcular valor total pendente de um empréstimo
     * @param emprestimoId ID do empréstimo
     * @return Valor total pendente
     */
    public Double calcularTotalPendente(Long emprestimoId) {
        Double total = em.createQuery(
            "SELECT SUM(p.valor) FROM Parcela p WHERE p.emprestimo.id = :emprestimoId AND p.paga = false",
            Double.class
        )
        .setParameter("emprestimoId", emprestimoId)
        .getSingleResult();
        
        return total != null ? total : 0.0;
    }

    /**
     * Verificar se todas as parcelas de um empréstimo estão pagas
     * @param emprestimoId ID do empréstimo
     * @return true se todas estão pagas
     */
    public boolean todasParcelasPagas(Long emprestimoId) {
        Long pendentes = countPendentesByEmprestimoId(emprestimoId);
        return pendentes == 0;
    }

    /**
     * Verificar se empréstimo tem parcelas atrasadas
     * @param emprestimoId ID do empréstimo
     * @return true se tem parcelas atrasadas
     */
    public boolean temParcelasAtrasadas(Long emprestimoId) {
        Long atrasadas = countAtrasadasByEmprestimoId(emprestimoId);
        return atrasadas > 0;
    }

    /**
     * Buscar parcelas por devedor (através do empréstimo)
     * @param devedorId ID do devedor
     * @return Lista de parcelas
     */
    public List<Parcela> findByDevedorId(Long devedorId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.devedor.id = :devedorId ORDER BY p.dataVencimento ASC",
            Parcela.class
        )
        .setParameter("devedorId", devedorId)
        .getResultList();
    }

    /**
     * Buscar parcelas pendentes por devedor
     * @param devedorId ID do devedor
     * @return Lista de parcelas pendentes
     */
    public List<Parcela> findPendentesByDevedorId(Long devedorId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.devedor.id = :devedorId AND p.paga = false ORDER BY p.dataVencimento ASC",
            Parcela.class
        )
        .setParameter("devedorId", devedorId)
        .getResultList();
    }

    /**
     * Buscar parcelas por credor (através do empréstimo)
     * @param credorId ID do credor
     * @return Lista de parcelas
     */
    public List<Parcela> findByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.credor.id = :credorId ORDER BY p.dataVencimento ASC",
            Parcela.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Buscar parcelas pendentes por credor
     * @param credorId ID do credor
     * @return Lista de parcelas pendentes
     */
    public List<Parcela> findPendentesByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT p FROM Parcela p WHERE p.emprestimo.credor.id = :credorId AND p.paga = false ORDER BY p.dataVencimento ASC",
            Parcela.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }
}

// Made with Bob
