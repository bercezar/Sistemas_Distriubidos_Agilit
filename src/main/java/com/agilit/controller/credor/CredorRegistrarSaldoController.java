package com.agilit.controller.credor;

import com.agilit.config.JPAUtil;
import com.agilit.model.Credor;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/credor/saldo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CredorRegistrarSaldoController {


    // DTO para aquisição do valor somente e não do objeto inteiro
    public static class SaldoDTO {
        public Double valor;
    }


    @PUT
    @Path("/{id}")
    public Response registrarSaldo(@PathParam("id") Long id, SaldoDTO dto) {

        if (dto == null || dto.valor == null || dto.valor <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\":\"Valor de depósito inválido\"}")
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Credor credor = em.find(Credor.class, id);

        if (credor == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erro\":\"Credor não encontrado\"}")
                    .build();
        }

        // Soma o valor ao saldo atual
        double novoSaldo = (credor.getSaldoDisponivel() == null ? 0 : credor.getSaldoDisponivel()) 
                           + dto.valor;

        credor.setSaldoDisponivel(novoSaldo);
        em.merge(credor);

        em.getTransaction().commit();
        em.close();

        return Response.ok(credor).build();
    }
}
