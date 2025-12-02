package com.agilit.controller.credor;

import com.agilit.dto.CredorDTO;
import com.agilit.model.Credor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gerenciar Credores.
 *
 * IMPORTANTE: Retorna DTOs ao invés de entidades JPA para evitar LazyInitializationException.
 * A sessão Hibernate é fechada antes de retornar, então não podemos retornar entidades
 * com relacionamentos LAZY diretamente.
 */
@Path("/credor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CredorController {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("agilitPU");

    /**
     * Lista todos os credores
     *
     * @return Lista de CredorDTO (sem relacionamentos LAZY)
     */
    @GET
    public List<CredorDTO> getAllCredores() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Credor> credores = em.createQuery("SELECT c FROM Credor c", Credor.class)
                                      .getResultList();
            
            // Converte List<Credor> para List<CredorDTO>
            return credores.stream()
                          .map(CredorDTO::fromEntity)
                          .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    /**
     * Busca credor por ID
     *
     * @param id ID do credor
     * @return CredorDTO
     */
    @GET
    @Path("/{id}")
    public Response getCredorById(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Credor credor = em.find(Credor.class, id);
            
            if (credor == null) {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity("{\"erro\":\"Credor não encontrado\"}")
                              .build();
            }
            
            // Converte para DTO antes de retornar
            CredorDTO dto = CredorDTO.fromEntity(credor);
            return Response.ok(dto).build();
            
        } finally {
            em.close();
        }
    }

    /**
     * Cria novo credor
     *
     * @param credor Dados do credor (recebe entidade, retorna DTO)
     * @return CredorDTO do credor criado
     */
    @POST
    public Response createCredor(Credor credor) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(credor);
            em.getTransaction().commit();
            
            // Converte para DTO antes de retornar
            CredorDTO dto = CredorDTO.fromEntity(credor);
            return Response.status(Response.Status.CREATED).entity(dto).build();
            
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
     * @return CredorDTO atualizado
     */
    @PUT
    @Path("/{id}")
    public Response updateCredor(@PathParam("id") Long id, Credor credorAtualizado) {
        EntityManager em = emf.createEntityManager();
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
            
            // Converte para DTO antes de retornar
            CredorDTO dto = CredorDTO.fromEntity(credor);
            return Response.ok(dto).build();
            
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
        EntityManager em = emf.createEntityManager();
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
