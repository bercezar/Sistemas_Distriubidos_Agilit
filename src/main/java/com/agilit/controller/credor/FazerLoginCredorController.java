package com.agilit.controller.credor;

import com.agilit.config.AppException;
import com.agilit.model.Credor;
import com.agilit.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * UC-C02: Fazer Login (Credor)
 * Autentica um credor no sistema
 * 
 * Endpoint: POST /api/credor/login
 * 
 * Body exemplo:
 * {
 *   "email": "joao@email.com",
 *   "senha": "senha123"
 * }
 * 
 * Response exemplo:
 * {
 *   "id": 1,
 *   "nome": "João Silva",
 *   "email": "joao@email.com",
 *   "saldoDisponivel": 10000.00,
 *   "tipo": "CREDOR",
 *   "mensagem": "Login realizado com sucesso"
 * }
 */
@Path("/credor/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FazerLoginCredorController {
    
    private final AuthService authService = new AuthService();
    
    @POST
    public Response login(LoginDTO dto) {
        // Validações
        if (dto.email == null || dto.email.trim().isEmpty()) {
            throw new AppException("Email é obrigatório", 400);
        }
        
        if (dto.senha == null || dto.senha.trim().isEmpty()) {
            throw new AppException("Senha é obrigatória", 400);
        }
        
        // Autenticar usando AuthService
        Credor credor = (Credor) authService.autenticar(dto.email, dto.senha, "CREDOR");
        
        if (credor == null) {
            throw new AppException("Email ou senha inválidos", 401);
        }
        
        // Montar resposta com dados do credor
        Map<String, Object> response = new HashMap<>();
        response.put("id", credor.getId());
        response.put("nome", credor.getNome());
        response.put("email", credor.getEmail());
        response.put("cpf", credor.getCpf());
        response.put("telefone", credor.getTelefone());
        response.put("saldoDisponivel", credor.getSaldoDisponivel());
        response.put("tipo", "CREDOR");
        response.put("mensagem", "Login realizado com sucesso");
        
        return Response.ok(response).build();
    }
    
    /**
     * DTO para login
     */
    public static class LoginDTO {
        public String email;
        public String senha;
    }
}

// Made with Bob
