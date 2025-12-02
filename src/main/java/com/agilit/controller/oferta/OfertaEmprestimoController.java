package com.agilit.controller.oferta;

import com.agilit.config.AppException;
import com.agilit.config.JPAUtil;
import com.agilit.model.Credor;
import com.agilit.model.OfertaEmprestimo;
import com.agilit.model.PropostaEmprestimo;
import com.agilit.util.CalculadoraEmprestimo;
import com.agilit.util.GeradorIdPublico;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller para gerenciar Ofertas de Empréstimo.
 * Ofertas são privadas (apenas o Credor que criou pode ver).
 * Operações: CREATE, READ, DELETE (sem UPDATE conforme especificação)
 */
@Path("/oferta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OfertaEmprestimoController {

    /**
     * Criar nova oferta de empréstimo
     * POST /api/oferta
     */
    @POST
    public Response criar(OfertaEmprestimo oferta) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            // Validações
            if (oferta.getCredor() == null || oferta.getCredor().getId() == null) {
                throw new AppException("Credor é obrigatório", 400);
            }

            if (oferta.getValorDisponivel() == null || oferta.getValorDisponivel() <= 0) {
                throw new AppException("Valor disponível deve ser maior que zero", 400);
            }

            if (oferta.getParcelasMinimas() == null || oferta.getParcelasMinimas() < 1) {
                throw new AppException("Parcelas mínimas deve ser pelo menos 1", 400);
            }

            if (oferta.getParcelasMaximas() == null || 
                oferta.getParcelasMaximas() < oferta.getParcelasMinimas()) {
                throw new AppException("Parcelas máximas deve ser maior ou igual às mínimas", 400);
            }

            if (oferta.getTaxaJuros() == null || oferta.getTaxaJuros() < 0) {
                throw new AppException("Taxa de juros deve ser maior ou igual a zero", 400);
            }

            if (oferta.getDiasAtePrimeiraCobranca() == null || 
                oferta.getDiasAtePrimeiraCobranca() < 0) {
                throw new AppException("Dias até primeira cobrança deve ser maior ou igual a zero", 400);
            }

            // Buscar credor
            Credor credor = em.find(Credor.class, oferta.getCredor().getId());
            if (credor == null) {
                throw new AppException("Credor não encontrado", 404);
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
     * Listar ofertas do credor logado
     * GET /api/oferta/credor/{credorId}
     */
    @GET
    @Path("/credor/{credorId}")
    public Response listarPorCredor(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<OfertaEmprestimo> ofertas = em.createQuery(
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
     * GET /api/oferta/credor/{credorId}/ativas
     */
    @GET
    @Path("/credor/{credorId}/ativas")
    public Response listarAtivasPorCredor(@PathParam("credorId") Long credorId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<OfertaEmprestimo> ofertas = em.createQuery(
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

    /**
     * Buscar oferta por ID
     * GET /api/oferta/{id}
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            OfertaEmprestimo oferta = em.find(OfertaEmprestimo.class, id);
            
            if (oferta == null) {
                throw new AppException("Oferta não encontrada", 404);
            }

            return Response.ok(oferta).build();

        } finally {
            em.close();
        }
    }

    /**
     * Deletar oferta
     * DELETE /api/oferta/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            OfertaEmprestimo oferta = em.find(OfertaEmprestimo.class, id);
            
            if (oferta == null) {
                throw new AppException("Oferta não encontrada", 404);
            }

            // Verificar se já tem propostas criadas
            Long countPropostas = em.createQuery(
                "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.ofertaOrigem.id = :ofertaId",
                Long.class
            )
            .setParameter("ofertaId", id)
            .getSingleResult();

            if (countPropostas > 0) {
                throw new AppException(
                    "Não é possível deletar oferta que já possui propostas criadas", 
                    400
                );
            }

            em.remove(oferta);
            em.getTransaction().commit();

            return Response.noContent().build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao deletar oferta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Criar proposta a partir da oferta
     * POST /api/oferta/{id}/criar-proposta
     */
    @POST
    @Path("/{id}/criar-proposta")
    public Response criarProposta(@PathParam("id") Long ofertaId) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();

            OfertaEmprestimo oferta = em.find(OfertaEmprestimo.class, ofertaId);
            
            if (oferta == null) {
                throw new AppException("Oferta não encontrada", 404);
            }

            if (!oferta.getAtiva()) {
                throw new AppException("Oferta não está ativa", 400);
            }

            // Criar proposta baseada na oferta
            PropostaEmprestimo proposta = new PropostaEmprestimo();
            
            // Gerar ID público único
            String idPublico;
            boolean idUnico = false;
            int tentativas = 0;
            
            do {
                idPublico = GeradorIdPublico.gerar();
                Long count = em.createQuery(
                    "SELECT COUNT(p) FROM PropostaEmprestimo p WHERE p.idPublico = :idPublico",
                    Long.class
                )
                .setParameter("idPublico", idPublico)
                .getSingleResult();
                
                idUnico = (count == 0);
                tentativas++;
                
                if (tentativas > 10) {
                    throw new AppException("Erro ao gerar ID público único", 500);
                }
            } while (!idUnico);

            proposta.setIdPublico(idPublico);
            proposta.setOfertaOrigem(oferta);
            proposta.setCredor(oferta.getCredor());
            proposta.setNomeCredor(oferta.getCredor().getNome());
            proposta.setValorDisponivel(oferta.getValorDisponivel());
            proposta.setParcelasMinimas(oferta.getParcelasMinimas());
            proposta.setParcelasMaximas(oferta.getParcelasMaximas());
            proposta.setDiasAtePrimeiraCobranca(oferta.getDiasAtePrimeiraCobranca());
            proposta.setTaxaJuros(oferta.getTaxaJuros());
            proposta.setDataCriacao(LocalDateTime.now());
            proposta.setStatus("ATIVA");

            em.persist(proposta);
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(proposta).build();

        } catch (AppException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new AppException("Erro ao criar proposta: " + e.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /**
     * Calcular opções de parcelamento para uma oferta
     * GET /api/oferta/{id}/opcoes-parcelas
     */
    @GET
    @Path("/{id}/opcoes-parcelas")
    public Response calcularOpcoesParcelas(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            OfertaEmprestimo oferta = em.find(OfertaEmprestimo.class, id);
            
            if (oferta == null) {
                throw new AppException("Oferta não encontrada", 404);
            }

            List<CalculadoraEmprestimo.OpcaoParcela> opcoes = 
                CalculadoraEmprestimo.calcularOpcoesParcelas(
                    oferta.getValorDisponivel(),
                    oferta.getParcelasMinimas(),
                    oferta.getParcelasMaximas(),
                    oferta.getTaxaJuros()
                );

            return Response.ok(opcoes).build();

        } finally {
            em.close();
        }
    }
}

// Made with Bob
