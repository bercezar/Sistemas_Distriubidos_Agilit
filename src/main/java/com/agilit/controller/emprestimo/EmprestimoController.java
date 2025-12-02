package com.agilit.controller.emprestimo;

import com.agilit.model.Emprestimo;
import com.agilit.config.JPAUtil;
import com.agilit.model.Credor;
import com.agilit.model.Devedor;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/emprestimo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmprestimoController {

    // =====================
    // LISTAR TODOS
    // =====================
    @GET
    public List<Emprestimo> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Emprestimo> lista = em.createQuery("SELECT e FROM Emprestimo e", Emprestimo.class)
                                   .getResultList();
        em.close();
        return lista;
    }


    // =====================
    // BUSCAR POR ID
    // =====================
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Emprestimo emprestimo = em.find(Emprestimo.class, id);
        em.close();

        if (emprestimo == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(emprestimo).build();
    }


    // =====================
    // CRIAR EMPRÉSTIMO
    // =====================
    @POST
    public Response create(Emprestimo entrada) {

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        // ---- Resolvendo Credor e Devedor ----
        Credor credor = em.find(Credor.class, entrada.getCredor().getId());
        Devedor devedor = em.find(Devedor.class, entrada.getDevedor().getId());

        if (credor == null || devedor == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Credor ou Devedor inexistente").build();
        }

        entrada.setCredor(credor);
        entrada.setDevedor(devedor);

        em.persist(entrada);
        em.getTransaction().commit();
        em.close();

        return Response.status(Response.Status.CREATED).entity(entrada).build();
    }


    // =====================
    // ATUALIZAR EMPRÉSTIMO
    // =====================
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Emprestimo entrada) {

        EntityManager em = JPAUtil.getEntityManager();
        Emprestimo existente = em.find(Emprestimo.class, id);

        if (existente == null) {
            em.close();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.getTransaction().begin();

        // Atualizar campos simples
        existente.setValorPrincipal(entrada.getValorPrincipal());
        existente.setJurosAplicados(entrada.getJurosAplicados());
        existente.setValorTotal(entrada.getValorTotal());
        existente.setDataInicio(entrada.getDataInicio());
        existente.setDataVencimento(entrada.getDataVencimento());
        existente.setStatus(entrada.getStatus());

        // Atualizar Credor/Devedor se vier no JSON
        if (entrada.getCredor() != null) {
            Credor credor = em.find(Credor.class, entrada.getCredor().getId());
            existente.setCredor(credor);
        }

        if (entrada.getDevedor() != null) {
            Devedor devedor = em.find(Devedor.class, entrada.getDevedor().getId());
            existente.setDevedor(devedor);
        }

        em.getTransaction().commit();
        em.close();

        return Response.ok(existente).build();
    }


    // =====================
    // EXCLUIR EMPRÉSTIMO
    // =====================
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Emprestimo emprestimo = em.find(Emprestimo.class, id);

        if (emprestimo == null) {
            em.close();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.getTransaction().begin();
        em.remove(emprestimo);
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build();
    }
}
