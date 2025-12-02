package com.agilit.controller.credor;

import java.net.URI;
import java.util.List;

import com.agilit.config.JPAUtil;
import com.agilit.model.Credor;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * Controller para criar conta de Credor.
 *
 * AÇÕES:
 * - CREDOR: Criar nova conta com validação de email único
 */
@Path("/credor/criar-conta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CredorCriarContaController {
    

    @POST
    public Response criarConta(@Context UriInfo uriInfo, Credor novoCredor){
        EntityManager em = JPAUtil.getEntityManager();

        try{
            em.getTransaction().begin();

            // email obrigatório para validação
            if(novoCredor.getEmail() == null){
                return Response.status(Response.Status.BAD_REQUEST).entity("Email é obrigatório").build();
            }

            // Verifica se existe ja o email
            List<Credor> encontrados = em.createQuery(
                    "SELECT c FROM Credor c WHERE c.email = :email", Credor.class)
                    .setParameter("email", novoCredor.getEmail())
                    .getResultList();

            if (!encontradosEmpty(encontrados)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"erro\":\"Credor com este email já cadastrado\"}")
                        .build();
            }
            
            // Inicialização de saldo
            if (novoCredor.getSaldoDisponivel() == null) {
                novoCredor.setSaldoDisponivel(0.0);
            }

            em.persist(novoCredor);
            em.getTransaction().commit();

            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(novoCredor.getId())).build();
            return Response.created(uri).entity(novoCredor).build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return Response.serverError()
                    .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                    .build();
        } finally {
            em.close();
        }
    }

    private boolean encontradosEmpty(List<Credor> lista) {
        return lista == null || lista.isEmpty();
    }
}
