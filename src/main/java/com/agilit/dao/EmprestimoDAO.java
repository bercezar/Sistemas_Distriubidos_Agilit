package com.agilit.dao;

import com.agilit.model.Emprestimo;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a Emprestimo.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class EmprestimoDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public EmprestimoDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar empréstimo por ID
     * @param id ID do empréstimo
     * @return Emprestimo ou null
     */
    public Emprestimo findById(Long id) {
        return em.find(Emprestimo.class, id);
    }

    /**
     * Buscar todos os empréstimos
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findAll() {
        return em.createQuery("SELECT e FROM Emprestimo e ORDER BY e.dataInicio DESC", 
                             Emprestimo.class)
                .getResultList();
    }

    /**
     * Buscar empréstimos por devedor
     * @param devedorId ID do devedor
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findByDevedorId(Long devedorId) {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.devedor.id = :devedorId ORDER BY e.dataInicio DESC",
            Emprestimo.class
        )
        .setParameter("devedorId", devedorId)
        .getResultList();
    }

    /**
     * Buscar empréstimos por credor
     * @param credorId ID do credor
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.credor.id = :credorId ORDER BY e.dataInicio DESC",
            Emprestimo.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Buscar empréstimos por status
     * @param status Status do empréstimo
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findByStatus(String status) {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.status = :status ORDER BY e.dataInicio DESC",
            Emprestimo.class
        )
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar empréstimos por devedor e status
     * @param devedorId ID do devedor
     * @param status Status do empréstimo
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findByDevedorIdAndStatus(Long devedorId, String status) {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.devedor.id = :devedorId AND e.status = :status ORDER BY e.dataInicio DESC",
            Emprestimo.class
        )
        .setParameter("devedorId", devedorId)
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar empréstimos por credor e status
     * @param credorId ID do credor
     * @param status Status do empréstimo
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findByCredorIdAndStatus(Long credorId, String status) {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.credor.id = :credorId AND e.status = :status ORDER BY e.dataInicio DESC",
            Emprestimo.class
        )
        .setParameter("credorId", credorId)
        .setParameter("status", status)
        .getResultList();
    }

    /**
     * Buscar empréstimos ativos (Em andamento)
     * @return Lista de empréstimos ativos
     */
    public List<Emprestimo> findAtivos() {
        return findByStatus("Em andamento");
    }

    /**
     * Buscar empréstimos atrasados
     * @return Lista de empréstimos atrasados
     */
    public List<Emprestimo> findAtrasados() {
        return findByStatus("Atrasado");
    }

    /**
     * Buscar empréstimos pagos
     * @return Lista de empréstimos pagos
     */
    public List<Emprestimo> findPagos() {
        return findByStatus("Pago");
    }

    /**
     * Buscar empréstimos que vencem em breve (próximos N dias)
     * @param dias Número de dias
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findVencendoEm(int dias) {
        LocalDate dataLimite = LocalDate.now().plusDays(dias);
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.dataVencimento BETWEEN :hoje AND :dataLimite AND e.status = 'Em andamento' ORDER BY e.dataVencimento ASC",
            Emprestimo.class
        )
        .setParameter("hoje", LocalDate.now())
        .setParameter("dataLimite", dataLimite)
        .getResultList();
    }

    /**
     * Buscar empréstimos vencidos (não pagos e com data de vencimento passada)
     * @return Lista de empréstimos vencidos
     */
    public List<Emprestimo> findVencidos() {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.dataVencimento < :hoje AND e.status != 'Pago' ORDER BY e.dataVencimento ASC",
            Emprestimo.class
        )
        .setParameter("hoje", LocalDate.now())
        .getResultList();
    }

    /**
     * Buscar empréstimos por proposta origem
     * @param propostaId ID da proposta
     * @return Emprestimo ou null
     */
    public Emprestimo findByPropostaOrigem(Long propostaId) {
        List<Emprestimo> result = em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.propostaOrigem.id = :propostaId",
            Emprestimo.class
        )
        .setParameter("propostaId", propostaId)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Buscar empréstimos por interesse origem
     * @param interesseId ID do interesse
     * @return Emprestimo ou null
     */
    public Emprestimo findByInteresseOrigem(Long interesseId) {
        List<Emprestimo> result = em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.interesseOrigem.id = :interesseId",
            Emprestimo.class
        )
        .setParameter("interesseId", interesseId)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Salvar ou atualizar empréstimo
     * @param emprestimo Empréstimo a ser salvo
     * @return Empréstimo persistido
     */
    public Emprestimo save(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            em.persist(emprestimo);
            return emprestimo;
        } else {
            return em.merge(emprestimo);
        }
    }

    /**
     * Deletar empréstimo
     * @param emprestimo Empréstimo a ser deletado
     */
    public void delete(Emprestimo emprestimo) {
        if (!em.contains(emprestimo)) {
            emprestimo = em.merge(emprestimo);
        }
        em.remove(emprestimo);
    }

    /**
     * Deletar empréstimo por ID
     * @param id ID do empréstimo
     */
    public void deleteById(Long id) {
        Emprestimo emprestimo = findById(id);
        if (emprestimo != null) {
            delete(emprestimo);
        }
    }

    /**
     * Contar total de empréstimos
     * @return Número de empréstimos
     */
    public Long count() {
        return em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e",
            Long.class
        )
        .getSingleResult();
    }

    /**
     * Contar empréstimos por devedor
     * @param devedorId ID do devedor
     * @return Número de empréstimos
     */
    public Long countByDevedorId(Long devedorId) {
        return em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.devedor.id = :devedorId",
            Long.class
        )
        .setParameter("devedorId", devedorId)
        .getSingleResult();
    }

    /**
     * Contar empréstimos por credor
     * @param credorId ID do credor
     * @return Número de empréstimos
     */
    public Long countByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
    }

    /**
     * Contar empréstimos por status
     * @param status Status do empréstimo
     * @return Número de empréstimos
     */
    public Long countByStatus(String status) {
        return em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.status = :status",
            Long.class
        )
        .setParameter("status", status)
        .getSingleResult();
    }

    /**
     * Calcular valor total emprestado por credor
     * @param credorId ID do credor
     * @return Valor total
     */
    public Double calcularTotalEmprestadoPorCredor(Long credorId) {
        Double total = em.createQuery(
            "SELECT SUM(e.valorPrincipal) FROM Emprestimo e WHERE e.credor.id = :credorId",
            Double.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
        
        return total != null ? total : 0.0;
    }

    /**
     * Calcular valor total devido por devedor
     * @param devedorId ID do devedor
     * @return Valor total
     */
    public Double calcularTotalDevidoPorDevedor(Long devedorId) {
        Double total = em.createQuery(
            "SELECT SUM(e.valorTotal) FROM Emprestimo e WHERE e.devedor.id = :devedorId AND e.status != 'Pago'",
            Double.class
        )
        .setParameter("devedorId", devedorId)
        .getSingleResult();
        
        return total != null ? total : 0.0;
    }

    /**
     * Calcular valor total a receber por credor
     * @param credorId ID do credor
     * @return Valor total
     */
    public Double calcularTotalAReceberPorCredor(Long credorId) {
        Double total = em.createQuery(
            "SELECT SUM(e.valorTotal) FROM Emprestimo e WHERE e.credor.id = :credorId AND e.status != 'Pago'",
            Double.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
        
        return total != null ? total : 0.0;
    }

    /**
     * Verificar se devedor tem empréstimos ativos
     * @param devedorId ID do devedor
     * @return true se tem empréstimos ativos
     */
    public boolean temEmprestimosAtivos(Long devedorId) {
        Long count = em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.devedor.id = :devedorId AND e.status = 'Em andamento'",
            Long.class
        )
        .setParameter("devedorId", devedorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se devedor tem empréstimos atrasados
     * @param devedorId ID do devedor
     * @return true se tem empréstimos atrasados
     */
    public boolean temEmprestimosAtrasados(Long devedorId) {
        Long count = em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.devedor.id = :devedorId AND e.status = 'Atrasado'",
            Long.class
        )
        .setParameter("devedorId", devedorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Buscar empréstimos por faixa de valor
     * @param valorMin Valor mínimo
     * @param valorMax Valor máximo
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findByValorRange(Double valorMin, Double valorMax) {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.valorPrincipal BETWEEN :min AND :max ORDER BY e.valorPrincipal",
            Emprestimo.class
        )
        .setParameter("min", valorMin)
        .setParameter("max", valorMax)
        .getResultList();
    }

    /**
     * Buscar empréstimos que precisam atualização de status
     * Útil para jobs batch que verificam atrasos
     * @return Lista de empréstimos
     */
    public List<Emprestimo> findParaAtualizacaoStatus() {
        return em.createQuery(
            "SELECT e FROM Emprestimo e WHERE e.status IN ('Em andamento', 'Atrasado') ORDER BY e.dataVencimento",
            Emprestimo.class
        )
        .getResultList();
    }
}

// Made with Bob
