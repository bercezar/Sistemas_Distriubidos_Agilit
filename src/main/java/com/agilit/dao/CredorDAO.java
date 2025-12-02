package com.agilit.dao;

import com.agilit.model.Credor;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a Credor.
 * Recebe EntityManager no construtor e NUNCA gerencia transações.
 * Transações são gerenciadas apenas pelos Controllers.
 */
public class CredorDAO {

    private final EntityManager em;

    /**
     * Construtor que recebe o EntityManager
     * @param em EntityManager gerenciado pelo Controller
     */
    public CredorDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Buscar credor por ID
     * @param id ID do credor
     * @return Credor ou null
     */
    public Credor findById(Long id) {
        return em.find(Credor.class, id);
    }

    /**
     * Buscar todos os credores
     * @return Lista de credores
     */
    public List<Credor> findAll() {
        return em.createQuery("SELECT c FROM Credor c ORDER BY c.nome", 
                             Credor.class)
                .getResultList();
    }

    /**
     * Buscar credor por email
     * @param email Email do credor
     * @return Credor ou null
     */
    public Credor findByEmail(String email) {
        List<Credor> result = em.createQuery(
            "SELECT c FROM Credor c WHERE c.email = :email",
            Credor.class
        )
        .setParameter("email", email)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Buscar credor por CPF
     * @param cpf CPF do credor
     * @return Credor ou null
     */
    public Credor findByCpf(String cpf) {
        List<Credor> result = em.createQuery(
            "SELECT c FROM Credor c WHERE c.cpf = :cpf",
            Credor.class
        )
        .setParameter("cpf", cpf)
        .getResultList();
        
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Verificar se email já existe
     * @param email Email a verificar
     * @return true se existe
     */
    public boolean existsByEmail(String email) {
        Long count = em.createQuery(
            "SELECT COUNT(c) FROM Credor c WHERE c.email = :email",
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
            "SELECT COUNT(c) FROM Credor c WHERE c.cpf = :cpf",
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
            "SELECT COUNT(c) FROM Credor c WHERE c.email = :email AND c.id != :id",
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
            "SELECT COUNT(c) FROM Credor c WHERE c.cpf = :cpf AND c.id != :id",
            Long.class
        )
        .setParameter("cpf", cpf)
        .setParameter("id", excludeId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Buscar credores por nome (busca parcial)
     * @param nome Nome ou parte do nome
     * @return Lista de credores
     */
    public List<Credor> findByNomeContaining(String nome) {
        return em.createQuery(
            "SELECT c FROM Credor c WHERE LOWER(c.nome) LIKE LOWER(:nome) ORDER BY c.nome",
            Credor.class
        )
        .setParameter("nome", "%" + nome + "%")
        .getResultList();
    }

    /**
     * Salvar ou atualizar credor
     * @param credor Credor a ser salvo
     * @return Credor persistido
     */
    public Credor save(Credor credor) {
        if (credor.getId() == null) {
            em.persist(credor);
            return credor;
        } else {
            return em.merge(credor);
        }
    }

    /**
     * Deletar credor
     * @param credor Credor a ser deletado
     */
    public void delete(Credor credor) {
        if (!em.contains(credor)) {
            credor = em.merge(credor);
        }
        em.remove(credor);
    }

    /**
     * Deletar credor por ID
     * @param id ID do credor
     */
    public void deleteById(Long id) {
        Credor credor = findById(id);
        if (credor != null) {
            delete(credor);
        }
    }

    /**
     * Contar total de credores
     * @return Número de credores
     */
    public Long count() {
        return em.createQuery(
            "SELECT COUNT(c) FROM Credor c",
            Long.class
        )
        .getSingleResult();
    }

    /**
     * Verificar se credor tem ofertas
     * @param credorId ID do credor
     * @return true se tem ofertas
     */
    public boolean temOfertas(Long credorId) {
        Long count = em.createQuery(
            "SELECT COUNT(o) FROM OfertaEmprestimo o WHERE o.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se credor tem propostas
     * @param credorId ID do credor
     * @return true se tem propostas
     */
    public boolean temPropostas(Long credorId) {
        Long count = em.createQuery(
            "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se credor tem empréstimos
     * @param credorId ID do credor
     * @return true se tem empréstimos
     */
    public boolean temEmprestimos(Long credorId) {
        Long count = em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.credor.id = :credorId",
            Long.class
        )
        .setParameter("credorId", credorId)
        .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verificar se credor pode ser deletado (não tem vínculos)
     * @param credorId ID do credor
     * @return true se pode ser deletado
     */
    public boolean podeDeletar(Long credorId) {
        return !temOfertas(credorId) && !temPropostas(credorId) && !temEmprestimos(credorId);
    }
}

 
