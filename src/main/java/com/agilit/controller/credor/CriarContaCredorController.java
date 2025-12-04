package com.agilit.controller.credor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.config.PasswordUtil;
import com.agilit.model.Credor;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * UC-C01: Criar Conta de Credor
 * Permite que um novo credor se cadastre no sistema
 * 
 * Endpoint: POST /api/credor/criar-conta
 * 
 * Body exemplo:
 * {
 *   "nome": "João Silva",
 *   "cpf": "12345678900",
 *   "email": "joao@email.com",
 *   "senhaHash": "senha123",
 *   "telefone": "(11) 98765-4321",
 *   "saldoDisponivel": 10000.00
 * }
 */
@Path("/credor/criar-conta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CriarContaCredorController {
    
    @POST
    public Response criarConta(Credor novoCredor) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Validações de campos obrigatórios
            if (novoCredor.getNome() == null || novoCredor.getNome().trim().isEmpty()) {
                throw new AppException("Nome é obrigatório", 400);
            }
            
            if (novoCredor.getEmail() == null || novoCredor.getEmail().trim().isEmpty()) {
                throw new AppException("Email é obrigatório", 400);
            }
            
            if (novoCredor.getSenhaHash() == null || novoCredor.getSenhaHash().trim().isEmpty()) {
                throw new AppException("Senha é obrigatória", 400);
            }
            
            if (novoCredor.getCpf() == null || novoCredor.getCpf().trim().isEmpty()) {
                throw new AppException("CPF é obrigatório", 400);
            }
            
            // Verificar email único
            List<Credor> credoresComEmail = em.createQuery(
                "SELECT c FROM Credor c WHERE c.email = :email", Credor.class)
                .setParameter("email", novoCredor.getEmail())
                .getResultList();
                
            if (!credoresComEmail.isEmpty()) {
                throw new AppException("Email já cadastrado", 409);
            }
            
            // Verificar CPF único
            List<Credor> credoresComCpf = em.createQuery(
                "SELECT c FROM Credor c WHERE c.cpf = :cpf", Credor.class)
                .setParameter("cpf", novoCredor.getCpf())
                .getResultList();
                
            if (!credoresComCpf.isEmpty()) {
                throw new AppException("CPF já cadastrado", 409);
            }
            
            // Hash da senha
            String senhaHash = PasswordUtil.hash(novoCredor.getSenhaHash());
            novoCredor.setSenhaHash(senhaHash);
            
            // Inicializar saldo se não fornecido
            if (novoCredor.getSaldoDisponivel() == null) {
                novoCredor.setSaldoDisponivel(0.0);
            }
            
            em.persist(novoCredor);
            em.getTransaction().commit();
            
            return Response.status(Response.Status.CREATED).entity(novoCredor).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao criar conta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
}

 
