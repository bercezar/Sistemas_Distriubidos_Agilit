package com.agilit.controller.auth;

import com.agilit.config.AppException;
import com.agilit.model.Usuario;
import com.agilit.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para autenticação (UC2 - Fazer Login).
 * Implementa login unificado para Credor e Devedor usando a interface Usuario.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private final AuthService authService = new AuthService();

    /**
     * Realiza login de usuário (Credor ou Devedor)
     * POST /api/auth/login
     * 
     * Body: {
     *   "email": "usuario@email.com",
     *   "senha": "senha123",
     *   "tipo": "CREDOR" ou "DEVEDOR"
     * }
     * 
     * Response: {
     *   "id": 1,
     *   "nome": "Nome do Usuário",
     *   "email": "usuario@email.com",
     *   "tipo": "CREDOR"
     * }
     */
    @POST
    @Path("/login")
    public Response login(LoginDTO dto) {
        // Validações
        if (dto.email == null || dto.email.trim().isEmpty()) {
            throw new AppException("Email é obrigatório", 400);
        }

        if (dto.senha == null || dto.senha.trim().isEmpty()) {
            throw new AppException("Senha é obrigatória", 400);
        }

        if (dto.tipo == null || dto.tipo.trim().isEmpty()) {
            throw new AppException("Tipo é obrigatório", 400);
        }

        if (!dto.tipo.matches("CREDOR|DEVEDOR")) {
            throw new AppException("Tipo deve ser CREDOR ou DEVEDOR", 400);
        }

        // Autenticar usando AuthService
        Usuario usuario = authService.autenticar(dto.email, dto.senha, dto.tipo);

        if (usuario == null) {
            throw new AppException("Email ou senha inválidos", 401);
        }

        // Montar resposta com dados do usuário
        Map<String, Object> response = new HashMap<>();
        
        // Usar reflexão para obter ID e Nome (já que Usuario não tem esses métodos)
        try {
            response.put("id", usuario.getClass().getMethod("getId").invoke(usuario));
            response.put("nome", usuario.getClass().getMethod("getNome").invoke(usuario));
        } catch (Exception e) {
            throw new AppException("Erro ao processar dados do usuário", 500);
        }
        
        response.put("email", usuario.getEmail());
        response.put("tipo", dto.tipo);
        response.put("mensagem", "Login realizado com sucesso");

        return Response.ok(response).build();
    }

    /**
     * Verifica se email está disponível para cadastro
     * GET /api/auth/verificar-email?email={email}&tipo={tipo}
     * 
     * Response: {
     *   "disponivel": true/false
     * }
     */
    @GET
    @Path("/verificar-email")
    public Response verificarEmail(
            @QueryParam("email") String email,
            @QueryParam("tipo") String tipo) {
        
        if (email == null || email.trim().isEmpty()) {
            throw new AppException("Email é obrigatório", 400);
        }

        if (tipo == null || !tipo.matches("CREDOR|DEVEDOR")) {
            throw new AppException("Tipo deve ser CREDOR ou DEVEDOR", 400);
        }

        boolean disponivel = authService.emailDisponivel(email, tipo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("disponivel", disponivel);
        response.put("email", email);
        response.put("tipo", tipo);
        
        return Response.ok(response).build();
    }

    /**
     * Verifica se CPF está disponível para cadastro
     * GET /api/auth/verificar-cpf?cpf={cpf}&tipo={tipo}
     * 
     * Response: {
     *   "disponivel": true/false
     * }
     */
    @GET
    @Path("/verificar-cpf")
    public Response verificarCpf(
            @QueryParam("cpf") String cpf,
            @QueryParam("tipo") String tipo) {
        
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new AppException("CPF é obrigatório", 400);
        }

        if (tipo == null || !tipo.matches("CREDOR|DEVEDOR")) {
            throw new AppException("Tipo deve ser CREDOR ou DEVEDOR", 400);
        }

        boolean disponivel = authService.cpfDisponivel(cpf, tipo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("disponivel", disponivel);
        response.put("cpf", cpf);
        response.put("tipo", tipo);
        
        return Response.ok(response).build();
    }

    /**
     * DTO para login
     */
    public static class LoginDTO {
        public String email;
        public String senha;
        public String tipo; // CREDOR ou DEVEDOR
    }
}

 
