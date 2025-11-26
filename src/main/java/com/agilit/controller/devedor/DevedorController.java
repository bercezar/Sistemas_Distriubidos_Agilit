package com.agilit.controller.devedor;

import com.agilit.model.Devedor;
import com.agilit.model.Credor;
import com.agilit.config.PasswordUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/devedor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DevedorController {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("agilitPU");

    // ================================
    // LISTAR TODOS
    // ================================
    @GET
    public List<Devedor> getAll() {
        EntityManager em = emf.createEntityManager();
        List<Devedor> lista =
                em.createQuery("SELECT d FROM Devedor d", Devedor.class).getResultList();
        em.close();
        return lista;
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        Devedor devedor = em.find(Devedor.class, id);
        em.close();

        if (devedor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(devedor).build();
    }

    // ================================
    // CRIAR DEVEDOR
    // ================================
    @POST
    public Response create(Devedor devedorEntrada) {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // 1 — HASH DA SENHA
        if (devedorEntrada.getSenhaHash() != null) {
            String hash = PasswordUtil.hash(devedorEntrada.getSenhaHash());
            devedorEntrada.setSenhaHash(hash);
        }

        // 2 — Validar credor associado
        if (devedorEntrada.getCredor() != null &&
            devedorEntrada.getCredor().getId() != null) {

            Credor credor = em.find(Credor.class, devedorEntrada.getCredor().getId());

            if (credor == null) {
                em.getTransaction().rollback();
                em.close();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Credor informado não existe").build();
            }

            devedorEntrada.setCredor(credor);
        }

        // Persistência
        em.persist(devedorEntrada);
        em.getTransaction().commit();
        em.close();

        // NÃO retornar senha
        devedorEntrada.setSenhaHash(null);

        return Response.status(Response.Status.CREATED).entity(devedorEntrada).build();
    }
}
