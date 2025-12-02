package com.agilit.dao;

import com.agilit.model.Devedor;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a Devedor.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class DevedorDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public DevedorDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar devedor por ID
     * @param id ID do devedor
     * @return Devedor ou null
     */
    public Devedor findById(Long id) {
        return em.find(Devedor.class, id);
    }

    /**
     * Buscar todos os devedores
     * @return Lista de devedores
     */
    public List<Devedor> findAll() {
        return em.createQuery("SELECT d FROM Devedor d ORDER BY d.nome", 
                             Devedor.class)
                .getResultList();
    }

    /**
     * Buscar devedor por email
     * @param email Email do devedor
     * @return Devedor ou null
     */
    public Devedor findByEmail(String email) {
        List<Devedor> result = em.createQuery(
            "SELECT d FROM Devedor d WHERE d.email = :email",
            Devedor.class
        )
        .setParameter("email", email)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Buscar devedor por CPF
     * @param cpf CPF do devedor
     * @return Devedor ou null
     */
    public Devedor findByCpf(String cpf) {
        List<Devedor> result = em.createQuery(
            "SELECT d FROM Devedor d WHERE d.cpf = :cpf",
            Devedor.class
        )
        .setParameter("cpf", cpf)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Buscar devedores por credor
     * @param credorId ID do credor
     * @return Lista de devedores
     */
    public List<Devedor> findByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT d FROM Devedor d WHERE d.credor.id = :credorId ORDER BY d.nome",
            Devedor.class
        )
        .setParameter("credorId", credorId)
        .getResultList();
    }

    /**
     * Verificar se email já existe
     * @param email Email a verificar
     * @return true se existe
     */
    public boolean existsByEmail(String email) {
        Long count = em.createQuery(
            "SELECT COUNT(d) FROM Devedor d WHERE d.email = :email",
            Long.class
        )
        .setParameter("email", email)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se CPF já existe
     * @param cpf CPF a verificar
     * @return true se existe
     */
    public boolean existsByCpf(String cpf) {
        Long count = em.createQuery(
            "SELECT COUNT(d) FROM Devedor d WHERE d.cpf = :cpf",
            Long.class
        )
        .setParameter("cpf", cpf)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se email já existe (excluindo um ID específico)
     * Útil para validação em updates
     * @param email Email a verificar
     * @param excludeId ID a excluir da verificação
     * @return true se existe
     */
    public boolean existsByEmailExcludingId(String email, Long excludeId) {
        Long count = em.createQuery(
            "SELECT COUNT(d) FROM Devedor d WHERE d.email = :email AND d.id != :id",
            Long.class
        )
        .setParameter("email", email)
        .setParameter("id", excludeId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se CPF já existe (excluindo um ID específico)
     * @param cpf CPF a verificar
     * @param excludeId ID a excluir da verificação
     * @return true se existe
     */
    public boolean existsByCpfExcludingId(String cpf, Long excludeId) {
        Long count = em.createQuery(
            "SELECT COUNT(d) FROM Devedor d WHERE d.cpf = :cpf AND d.id != :id",
            Long.class
        )
        .setParameter("cpf", cpf)
        .setParameter("id", excludeId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Buscar devedores por nome (busca parcial)
     * @param nome Nome ou parte do nome
     * @return Lista de devedores
     */
    public List<Devedor> findByNomeContaining(String nome) {
        return em.createQuery(
            "SELECT d FROM Devedor d WHERE LOWER(d.nome) LIKE LOWER(:nome) ORDER BY d.nome",
            Devedor.class
        )
        .setParameter("nome", "%" + nome + "%")
        .getResultList();
    }

    /**
     * Buscar devedores por cidade
     * @param cidade Cidade
     * @return Lista de devedores
     */
    public List<Devedor> findByCidade(String cidade) {
        return em.createQuery(
            "SELECT d FROM Devedor d WHERE LOWER(d.cidade) = LOWER(:cidade) ORDER BY d.nome",
            Devedor.class
        )
        .setParameter("cidade", cidade)
        .getResultList();
    }

    /**
     * Buscar devedores por estado
     * @param estado Estado (sigla)
     * @return Lista de devedores
     */
    public List<Devedor> findByEstado(String estado) {
        return em.createQuery(
            "SELECT d FROM Devedor d WHERE UPPER(d.estado) = UPPER(:estado) ORDER BY d.nome",
            Devedor.class
        )
        .setParameter("estado", estado)
        .getResultList();
    }

    /**
     * Salvar ou atualizar devedor
     * @param devedor Devedor a ser salvo
     * @return Devedor persistido
     */
    public Devedor save(Devedor devedor) {
        if (devedor.getId() == null) {
            em.persist(devedor);
            return devedor;
        } else {
            return em.merge(devedor);
        }
    }

    /**
     * Deletar devedor
     * @param devedor Devedor a ser deletado
     */
    public void delete(Devedor devedor) {
        if (!em.contains(devedor)) {
            devedor = em.merge(devedor);
        }
        em.remove(devedor);
    }

    /**
     * Deletar devedor por ID
     * @param id ID do devedor
     */
    public void deleteById(Long id) {
        Devedor devedor = findById(id);
        if (devedor != null) {
            delete(devedor);
        }
    }

    /**
     * Contar total de devedores
     * @return Número de devedores
     */
    public Long count() {
        return em.createQuery(
            "SELECT COUNT(d) FROM Devedor d",
            Long.class
        )
        .getSingleResult();
    }

    /**
     * Contar devedores por credor
     * @param credorId ID do credor
     * @return Número de devedores
     */
    public Long countByCredorId(Long credorId) {
        return em.createQuery(
            "SELECT COUNT(d) FROM Devedor d WHERE d.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
    }

    /**
     * Verificar se devedor tem interesses em propostas
     * @param devedorId ID do devedor
     * @return true se tem interesses
     */
    public boolean temInteresses(Long devedorId) {
        Long count = em.createQuery(
            "SELECT COUNT(i) FROM InteresseProposta i WHERE i.devedor.id = :devedorId",
            Long.class
        )
        .setParameter("devedorId", devedorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se devedor tem empréstimos
     * @param devedorId ID do devedor
     * @return true se tem empréstimos
     */
    public boolean temEmprestimos(Long devedorId) {
        Long count = em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.devedor.id = :devedorId",
            Long.class
        )
        .setParameter("devedorId", devedorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se devedor pode ser deletado (não tem vínculos)
     * @param devedorId ID do devedor
     * @return true se pode ser deletado
     */
    public boolean podeDeletar(Long devedorId) {
        return !temInteresses(devedorId) && !temEmprestimos(devedorId);
    }

    /**
     * Buscar devedores com empréstimos ativos
     * @return Lista de devedores
     */
    public List<Devedor> findComEmprestimosAtivos() {
        return em.createQuery(
            "SELECT DISTINCT d FROM Devedor d JOIN d.emprestimos e WHERE e.status = 'Em andamento' ORDER BY d.nome",
            Devedor.class
        )
        .getResultList();
    }

    /**
     * Buscar devedores com empréstimos atrasados
     * @return Lista de devedores
     */
    public List<Devedor> findComEmprestimosAtrasados() {
        return em.createQuery(
            "SELECT DISTINCT d FROM Devedor d JOIN d.emprestimos e WHERE e.status = 'Atrasado' ORDER BY d.nome",
            Devedor.class
        )
        .getResultList();
    }
}

 
