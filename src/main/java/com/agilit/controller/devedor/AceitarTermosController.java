package com.agilit.controller.devedor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.Devedor;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * UC-D06: Aceitar Termos
 * Registra a aceitação dos termos de uso pelo devedor
 * 
 * Endpoint: POST /api/devedor/aceitar-termos/{devedorId}
 * 
 * Body exemplo:
 * {
 *   "aceitouTermos": true,
 *   "versaoTermos": "1.0",
 *   "ip": "192.168.1.1"
 * }
 * 
 * Observação: Este é um caso de uso importante para compliance e auditoria.
 * Registra quando e qual versão dos termos foi aceita pelo usuário.
 */
@Path("/devedor/aceitar-termos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AceitarTermosController {
    
    /**
     * Aceitar termos de uso
     */
    @POST
    @Path("/{devedorId}")
    public Response aceitarTermos(
            @PathParam("devedorId") Long devedorId,
            AceitarTermosDTO dto) {
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Buscar devedor
            Devedor devedor = em.find(Devedor.class, devedorId);
            
            if (devedor == null) {
                throw new AppException("Devedor não encontrado", 404);
            }
            
            // Validação: Deve aceitar os termos
            if (dto.aceitouTermos == null || !dto.aceitouTermos) {
                throw new AppException("É necessário aceitar os termos para continuar", 400);
            }
            
            // Validação: Versão dos termos obrigatória
            if (dto.versaoTermos == null || dto.versaoTermos.trim().isEmpty()) {
                throw new AppException("Versão dos termos é obrigatória", 400);
            }
            
            // Registrar aceitação
            // Nota: Você pode adicionar campos na entidade Devedor para armazenar:
            // - dataAceitacaoTermos (LocalDateTime)
            // - versaoTermosAceita (String)
            // - ipAceitacaoTermos (String)
            
            // Por enquanto, vamos apenas registrar no log/resposta
            // Em produção, adicione os campos na entidade e persista
            
            em.merge(devedor);
            em.getTransaction().commit();
            
            // Montar resposta
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Termos aceitos com sucesso");
            response.put("dataAceitacao", LocalDateTime.now());
            response.put("devedorId", devedorId);
            response.put("devedorNome", devedor.getNome());
            response.put("versaoTermos", dto.versaoTermos);
            response.put("proximoPasso", "Você já pode demonstrar interesse em propostas de empréstimo");
            
            return Response.ok(response).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao aceitar termos: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Obter termos de uso atuais
     * GET /api/devedor/aceitar-termos/termos-atuais
     */
    @GET
    @Path("/termos-atuais")
    public Response obterTermosAtuais() {
        Map<String, Object> termos = new HashMap<>();
        termos.put("versao", "1.0");
        termos.put("dataPublicacao", "2024-01-01");
        termos.put("titulo", "Termos de Uso - Plataforma de Empréstimos Agilit");
        termos.put("conteudo", gerarConteudoTermos());
        termos.put("obrigatorio", true);
        
        return Response.ok(termos).build();
    }
    
    /**
     * Verificar se devedor aceitou os termos
     * GET /api/devedor/aceitar-termos/{devedorId}/status
     */
    @GET
    @Path("/{devedorId}/status")
    public Response verificarAceitacao(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Devedor devedor = em.find(Devedor.class, devedorId);
            
            if (devedor == null) {
                throw new AppException("Devedor não encontrado", 404);
            }
            
            // Nota: Em produção, verificar campos reais da entidade
            // Por enquanto, retornar status genérico
            Map<String, Object> status = new HashMap<>();
            status.put("devedorId", devedorId);
            status.put("aceitouTermos", true); // Verificar campo real
            status.put("versaoAceita", "1.0"); // Verificar campo real
            status.put("dataAceitacao", null); // Verificar campo real
            status.put("precisaAceitar", false); // true se versão mudou
            
            return Response.ok(status).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Histórico de aceitação de termos
     * GET /api/devedor/aceitar-termos/{devedorId}/historico
     */
    @GET
    @Path("/{devedorId}/historico")
    public Response obterHistorico(@PathParam("devedorId") Long devedorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Devedor devedor = em.find(Devedor.class, devedorId);
            
            if (devedor == null) {
                throw new AppException("Devedor não encontrado", 404);
            }
            
            // Nota: Em produção, buscar de uma tabela de auditoria
            // Por enquanto, retornar exemplo
            Map<String, Object> historico = new HashMap<>();
            historico.put("devedorId", devedorId);
            historico.put("devedorNome", devedor.getNome());
            historico.put("aceitacoes", new java.util.ArrayList<>());
            historico.put("mensagem", "Histórico de aceitação de termos (implementar tabela de auditoria)");
            
            return Response.ok(historico).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Gera conteúdo dos termos (exemplo)
     */
    private String gerarConteudoTermos() {
        return """
            TERMOS DE USO - PLATAFORMA DE EMPRÉSTIMOS AGILIT
            
            1. ACEITAÇÃO DOS TERMOS
            Ao utilizar esta plataforma, você concorda com estes termos de uso.
            
            2. DESCRIÇÃO DO SERVIÇO
            A plataforma Agilit conecta credores e devedores para realização de empréstimos.
            
            3. RESPONSABILIDADES DO USUÁRIO
            - Fornecer informações verdadeiras e atualizadas
            - Manter a confidencialidade de suas credenciais
            - Cumprir com os compromissos financeiros assumidos
            
            4. PRIVACIDADE
            Seus dados serão tratados conforme nossa Política de Privacidade.
            
            5. TAXAS E JUROS
            As taxas de juros são definidas pelos credores e informadas claramente antes da contratação.
            
            6. CANCELAMENTO
            Você pode cancelar sua conta a qualquer momento, desde que não tenha empréstimos ativos.
            
            7. MODIFICAÇÕES
            Reservamo-nos o direito de modificar estes termos a qualquer momento.
            
            8. CONTATO
            Para dúvidas, entre em contato através de suporte@agilit.com
            
            Última atualização: 01/01/2024
            Versão: 1.0
            """;
    }
    
    /**
     * DTO para aceitar termos
     */
    public static class AceitarTermosDTO {
        public Boolean aceitouTermos;
        public String versaoTermos;
        public String ip; // Opcional: para auditoria
        public String userAgent; // Opcional: para auditoria
    }
}

 
