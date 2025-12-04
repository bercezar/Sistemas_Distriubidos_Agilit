package com.agilit.controller.credor;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.Credor;
import com.agilit.model.OfertaEmprestimo;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;

/**
 * UC-C03: Criar Oferta de Empréstimo
 * Permite que o credor crie uma nova oferta privada de empréstimo
 * 
 * Endpoint: POST /api/credor/criar-oferta
 * 
 * Body exemplo:
 * {
 *   "credor": { "id": 1 },
 *   "valorDisponivel": 5000.00,
 *   "parcelasMinimas": 6,
 *   "parcelasMaximas": 24,
 *   "taxaJuros": 2.5,
 *   "diasAtePrimeiraCobranca": 30
 * }
 * 
 * Regras de Negócio:
 * - Credor deve existir
 * - Valor disponível deve ser maior que zero
 * - Taxa de juros deve ser >= 0
 * - Parcelas mínimas >= 1
 * - Parcelas máximas >= parcelas mínimas
 * - Credor deve ter saldo suficiente
 */
@Path("/credor/criar-oferta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CriarOfertaEmprestimoController {
    
    @POST
    public Response criarOferta(OfertaEmprestimo oferta) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Validação: Credor obrigatório
            if (oferta.getCredor() == null || oferta.getCredor().getId() == null) {
                throw new AppException("Credor é obrigatório", 400);
            }
            
            // Validação: Valor disponível
            if (oferta.getValorDisponivel() == null || oferta.getValorDisponivel() <= 0) {
                throw new AppException("Valor disponível deve ser maior que zero", 400);
            }
            
            // Validação: Parcelas mínimas
            if (oferta.getParcelasMinimas() == null || oferta.getParcelasMinimas() < 1) {
                throw new AppException("Parcelas mínimas deve ser pelo menos 1", 400);
            }
            
            // Validação: Parcelas máximas
            if (oferta.getParcelasMaximas() == null || 
                oferta.getParcelasMaximas() < oferta.getParcelasMinimas()) {
                throw new AppException("Parcelas máximas deve ser maior ou igual às mínimas", 400);
            }
            
            // Validação: Taxa de juros
            if (oferta.getTaxaJuros() == null || oferta.getTaxaJuros() < 0) {
                throw new AppException("Taxa de juros deve ser maior ou igual a zero", 400);
            }
            
            // Validação: Dias até primeira cobrança
            if (oferta.getDiasAtePrimeiraCobranca() == null || 
                oferta.getDiasAtePrimeiraCobranca() < 0) {
                throw new AppException("Dias até primeira cobrança deve ser maior ou igual a zero", 400);
            }
            
            // Buscar credor
            Credor credor = em.find(Credor.class, oferta.getCredor().getId());
            if (credor == null) {
                throw new AppException("Credor não encontrado", 404);
            }
            
            // Validação: Verificar saldo disponível
            if (credor.getSaldoDisponivel() < oferta.getValorDisponivel()) {
                throw new AppException(
                    String.format("Saldo insuficiente. Disponível: R$ %.2f, Necessário: R$ %.2f", 
                                 credor.getSaldoDisponivel(), oferta.getValorDisponivel()), 
                    400
                );
            }
            
            // Configurar oferta
            oferta.setCredor(credor);
            oferta.setDataCriacao(LocalDateTime.now());
            oferta.setAtiva(true);
            
            em.persist(oferta);
            em.getTransaction().commit();
            
            return Response.status(Response.Status.CREATED).entity(oferta).build();
            
        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao criar oferta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar ofertas do credor
     * GET /api/credor/criar-oferta/minhas/{credorId}
     */
    @GET
    @Path("/minhas/{credorId}")
    public Response listarMinhasOfertas(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var ofertas = em.createQuery(
                "SELECT o FROM OfertaEmprestimo o WHERE o.credor.id = :credorId ORDER BY o.dataCriacao DESC",
                OfertaEmprestimo.class
            )
            .setParameter("credorId", credorId)
            .getResultList();
            
            return Response.ok(ofertas).build();
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Listar apenas ofertas ativas do credor
     * GET /api/credor/criar-oferta/minhas/{credorId}/ativas
     */
    @GET
    @Path("/minhas/{credorId}/ativas")
    public Response listarMinhasOfertasAtivas(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            var ofertas = em.createQuery(
                "SELECT o FROM OfertaEmprestimo o WHERE o.credor.id = :credorId AND o.ativa = true ORDER BY o.dataCriacao DESC",
                OfertaEmprestimo.class
            )
            .setParameter("credorId", credorId)
            .getResultList();
            
            return Response.ok(ofertas).build();
            
        } finally {
            em.close();
        }
    }
}

 
