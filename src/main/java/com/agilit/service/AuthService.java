package com.agilit.service;

import com.agilit.config.JPAUtil;
import com.agilit.model.Usuario;
import com.agilit.model.dao.CredorDAO;
import com.agilit.model.dao.DevedorDAO;
import jakarta.persistence.EntityManager;

/**
 * Serviço de autenticação unificado.
 * Implementa UC2 - Fazer Login.
 * Utiliza a interface Usuario para polimorfismo.
 */
public class AuthService {

    /**
     * Autentica usuário (Credor ou Devedor) usando a interface Usuario.
     * 
     * @param email Email do usuário
     * @param senha Senha em texto plano
     * @param tipo "CREDOR" ou "DEVEDOR"
     * @return Usuario autenticado ou null se credenciais inválidas
     */
    public Usuario autenticar(String email, String senha, String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Usuario usuario = null;

            if ("CREDOR".equals(tipo)) {
                CredorDAO credorDAO = new CredorDAO(em);
                usuario = credorDAO.findByEmail(email);
            } else if ("DEVEDOR".equals(tipo)) {
                DevedorDAO devedorDAO = new DevedorDAO(em);
                usuario = devedorDAO.findByEmail(email);
            }

            // Usa o método autenticar() da interface Usuario
            if (usuario != null && usuario.autenticar(senha)) {
                return usuario;
            }

            return null;
            
        } finally {
            em.close();
        }
    }

    /**
     * Verifica se email está disponível para cadastro.
     * 
     * @param email Email a verificar
     * @param tipo "CREDOR" ou "DEVEDOR"
     * @return true se email está disponível
     */
    public boolean emailDisponivel(String email, String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            if ("CREDOR".equals(tipo)) {
                CredorDAO credorDAO = new CredorDAO(em);
                return !credorDAO.existsByEmail(email);
            } else if ("DEVEDOR".equals(tipo)) {
                DevedorDAO devedorDAO = new DevedorDAO(em);
                return !devedorDAO.existsByEmail(email);
            }
            return false;
            
        } finally {
            em.close();
        }
    }

    /**
     * Verifica se CPF está disponível para cadastro.
     * 
     * @param cpf CPF a verificar
     * @param tipo "CREDOR" ou "DEVEDOR"
     * @return true se CPF está disponível
     */
    public boolean cpfDisponivel(String cpf, String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            if ("CREDOR".equals(tipo)) {
                CredorDAO credorDAO = new CredorDAO(em);
                return !credorDAO.existsByCpf(cpf);
            } else if ("DEVEDOR".equals(tipo)) {
                DevedorDAO devedorDAO = new DevedorDAO(em);
                return !devedorDAO.existsByCpf(cpf);
            }
            return false;
            
        } finally {
            em.close();
        }
    }

    /**
     * Busca usuário por email (sem autenticação).
     * Útil para recuperação de senha, etc.
     * 
     * @param email Email do usuário
     * @param tipo "CREDOR" ou "DEVEDOR"
     * @return Usuario ou null
     */
    public Usuario buscarPorEmail(String email, String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            if ("CREDOR".equals(tipo)) {
                CredorDAO credorDAO = new CredorDAO(em);
                return credorDAO.findByEmail(email);
            } else if ("DEVEDOR".equals(tipo)) {
                DevedorDAO devedorDAO = new DevedorDAO(em);
                return devedorDAO.findByEmail(email);
            }
            return null;
            
        } finally {
            em.close();
        }
    }
}

 
