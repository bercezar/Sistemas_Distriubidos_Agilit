package com.agilit.controller.credor;

import com.agilit.config.JPAUtil;
import com.agilit.model.Credor;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Controller para gerenciar Credores.
 *
 * AÇÕES:
 * - CREDOR: Criar conta, atualizar dados, visualizar perfil, deletar conta
 *
 * NOTA: Relacionamentos LAZY protegidos com @JsonIgnore nas entidades.
 * Retorna entidades diretamente sem necessidade de DTOs.
 */
@Path("/credor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CredorController {



    /**
     * Lista todos os credores
     *
     * @return Lista de Credor
     */
    @GET
    public List<Credor> getAllCredores() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Credor c", Credor.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca credor por ID
     *
     * @param id ID do credor
     * @return Credor
     */
    @GET
    @Path("/{id}")
    public Response getCredorById(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Credor credor = em.find(Credor.class, id);
            
            if (credor == null) {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity("{\"erro\":\"Credor não encontrado\"}")
                              .build();
            }
            
            return Response.ok(credor).build();
            
        } finally {
            em.close();
        }
    }

    /**
     * Cria novo credor
     *
     * @param credor Dados do credor
     * @return Credor criado
     */
    @POST
    public Response createCredor(Credor credor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(credor);
            em.getTransaction().commit();

            return Response.status(Response.Status.CREATED).entity(credor).build();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                        .build();

        } finally {
            em.close();
        }
    }

    /**
     * Atualiza credor existente
     *
     * @param id ID do credor
     * @param credorAtualizado Dados atualizados
     * @return Credor atualizado
     */
    @PUT
    @Path("/{id}")
    public Response updateCredor(@PathParam("id") Long id, Credor credorAtualizado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Credor credor = em.find(Credor.class, id);
            if (credor == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                              .entity("{\"erro\":\"Credor não encontrado\"}")
                              .build();
            }
            
            // Atualiza campos
            credor.setNome(credorAtualizado.getNome());
            credor.setCpf(credorAtualizado.getCpf());
            credor.setTelefone(credorAtualizado.getTelefone());
            credor.setEmail(credorAtualizado.getEmail());
            if (credorAtualizado.getSaldoDisponivel() != null) {
                credor.setSaldoDisponivel(credorAtualizado.getSaldoDisponivel());
            }
            
            em.merge(credor);
            em.getTransaction().commit();
            
            return Response.ok(credor).build();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                          .build();
        } finally {
            em.close();
        }
    }

    /**
     * Deleta credor
     *
     * @param id ID do credor
     * @return Status da operação
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCredor(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Credor credor = em.find(Credor.class, id);
            if (credor == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.NOT_FOUND)
                              .entity("{\"erro\":\"Credor não encontrado\"}")
                              .build();
            }
            
            em.remove(credor);
            em.getTransaction().commit();
            
            return Response.noContent().build();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                          .build();
        } finally {
            em.close();
        }
    }
}
