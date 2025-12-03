package com.agilit.controller.devedor;

import com.agilit.config.AppException;
import com.agilit.model.Devedor;
import com.agilit.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * UC-D02: Fazer Login (Devedor)
 * Autentica um devedor no sistema
 * 
 * Endpoint: POST /api/devedor/login
 * 
 * Body exemplo:
 * {
 *   "email": "maria@email.com",
 *   "senha": "senha123"
 * }
 * 
 * Response exemplo:
 * {
 *   "id": 1,
 *   "nome": "Maria Santos",
 *   "email": "maria@email.com",
 *   "cpf": "98765432100",
 *   "telefone": "(11) 91234-5678",
 *   "dadosCompletos": true,
 *   "tipo": "DEVEDOR",
 *   "mensagem": "Login realizado com sucesso"
 * }
 */
@Path("/devedor/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FazerLoginDevedorController {
    
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
        Devedor devedor = (Devedor) authService.autenticar(dto.email, dto.senha, "DEVEDOR");
        
        if (devedor == null) {
            throw new AppException("Email ou senha inválidos", 401);
        }
        
        // Verificar se dados cadastrais estão completos
        boolean dadosCompletos = devedor.getEndereco() != null && 
                                 devedor.getCidade() != null && 
                                 devedor.getEstado() != null && 
                                 devedor.getCep() != null;
        
        // Montar resposta com dados do devedor
        Map<String, Object> response = new HashMap<>();
        response.put("id", devedor.getId());
        response.put("nome", devedor.getNome());
        response.put("email", devedor.getEmail());
        response.put("cpf", devedor.getCpf());
        response.put("telefone", devedor.getTelefone());
        response.put("endereco", devedor.getEndereco());
        response.put("cidade", devedor.getCidade());
        response.put("estado", devedor.getEstado());
        response.put("cep", devedor.getCep());
        response.put("dadosCompletos", dadosCompletos);
        response.put("tipo", "DEVEDOR");
        response.put("mensagem", "Login realizado com sucesso");
        
        if (!dadosCompletos) {
            response.put("aviso", "Complete seus dados cadastrais para poder demonstrar interesse em propostas");
        }
        
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
