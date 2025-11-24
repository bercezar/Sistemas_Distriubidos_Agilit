package com.agilit.controller;

import com.agilit.model.Credor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/credor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CredorController {


    private static final EntityManagerFactory entManagFact = Persistence.createEntityManagerFactory("agilitPU");


    @GET
    public List<Credor> getAllCredores() {
        EntityManager em = entManagFact.createEntityManager();
        List<Credor> credores = em.createQuery("SELECT c FROM Credor c", Credor.class).getResultList();
        em.close();
        return credores;
    }

    @POST
    public Response createCredor(Credor credor) {
        EntityManager em = entManagFact.createEntityManager();
        em.getTransaction().begin();
        em.persist(credor);
        em.getTransaction().commit();
        em.close();
        return Response.status(Response.Status.CREATED).entity(credor).build();
    }
}


// Mesma ideia para DevedorController, EmprestimoController, etc.
