package com.agilit.controller.devedor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.config.PasswordUtil;
import com.agilit.model.Devedor;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * UC-D01: Criar Conta de Devedor
 * Permite que um novo devedor se cadastre no sistema
 * 
 * Endpoint: POST /api/devedor/criar-conta
 * 
 * Body exemplo:
 * {
 *   "nome": "Maria Santos",
 *   "cpf": "98765432100",
 *   "email": "maria@email.com",
 *   "senhaHash": "senha123",
 *   "telefone": "(11) 91234-5678",
 *   "endereco": "Rua das Flores, 123",
 *   "cidade": "São Paulo",
 *   "estado": "SP",
 *   "cep": "01234-567"
 * }
 * 
 * Observação: Dados de endereço são opcionais no cadastro inicial,
 * mas serão obrigatórios para demonstrar interesse em propostas.
 */
@Path("/devedor/criar-conta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CriarContaDevedorController {
    
    @POST
    public Response criarConta(Devedor novoDevedor) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Validações de campos obrigatórios
            if (novoDevedor.getNome() == null || novoDevedor.getNome().trim().isEmpty()) {
                throw new AppException("Nome é obrigatório", 400);
            }
            
            if (novoDevedor.getEmail() == null || novoDevedor.getEmail().trim().isEmpty()) {
                throw new AppException("Email é obrigatório", 400);
            }
            
            if (novoDevedor.getSenhaHash() == null || novoDevedor.getSenhaHash().trim().isEmpty()) {
                throw new AppException("Senha é obrigatória", 400);
            }
            
            if (novoDevedor.getCpf() == null || novoDevedor.getCpf().trim().isEmpty()) {
                throw new AppException("CPF é obrigatório", 400);
            }
            
            // Verificar email único
            List<Devedor> devedoresComEmail = em.createQuery(
                "SELECT d FROM Devedor d WHERE d.email = :email", Devedor.class)
                .setParameter("email", novoDevedor.getEmail())
                .getResultList();
                
            if (!devedoresComEmail.isEmpty()) {
                throw new AppException("Email já cadastrado", 409);
            }
            
            // Verificar CPF único
            List<Devedor> devedoresComCpf = em.createQuery(
                "SELECT d FROM Devedor d WHERE d.cpf = :cpf", Devedor.class)
                .setParameter("cpf", novoDevedor.getCpf())
                .getResultList();
                
            if (!devedoresComCpf.isEmpty()) {
                throw new AppException("CPF já cadastrado", 409);
            }
            
            // Hash da senha
            String senhaHash = PasswordUtil.hash(novoDevedor.getSenhaHash());
            novoDevedor.setSenhaHash(senhaHash);
            
            em.persist(novoDevedor);
            em.getTransaction().commit();
            
            return Response.status(Response.Status.CREATED).entity(novoDevedor).build();
            
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
    
    /**
     * Completar dados cadastrais (endereço)
     * PUT /api/devedor/criar-conta/{devedorId}/completar-dados
     */
    @PUT
    @Path("/{devedorId}/completar-dados")
    public Response completarDados(
            @PathParam("devedorId") Long devedorId,
            DadosComplementaresDTO dto) {
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Devedor devedor = em.find(Devedor.class, devedorId);
            
            if (devedor == null) {
                throw new AppException("Devedor não encontrado", 404);
            }
            
            // Validar dados obrigatórios
            if (dto.endereco == null || dto.endereco.trim().isEmpty()) {
                throw new AppException("Endereço é obrigatório", 400);
            }
            
            if (dto.cidade == null || dto.cidade.trim().isEmpty()) {
                throw new AppException("Cidade é obrigatória", 400);
            }
            
            if (dto.estado == null || dto.estado.trim().isEmpty()) {
                throw new AppException("Estado é obrigatório", 400);
            }
            
            if (dto.cep == null || dto.cep.trim().isEmpty()) {
                throw new AppException("CEP é obrigatório", 400);
            }
            
            // Atualizar dados
            devedor.setEndereco(dto.endereco);
            devedor.setCidade(dto.cidade);
            devedor.setEstado(dto.estado);
            devedor.setCep(dto.cep);
            
            if (dto.telefone != null) {
                devedor.setTelefone(dto.telefone);
            }
            
            em.merge(devedor);
            em.getTransaction().commit();
            
            return Response.ok(devedor).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao completar dados: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * DTO para dados complementares
     */
    public static class DadosComplementaresDTO {
        public String endereco;
        public String cidade;
        public String estado;
        public String cep;
        public String telefone;
    }
}

// Made with Bob
