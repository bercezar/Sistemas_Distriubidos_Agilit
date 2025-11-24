package com.agilit.config;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Converte AppException em resposta JSON automaticamente.
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException ex) {

        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());
        erro.put("status", ex.getStatus());

        return Response.status(ex.getStatus())
                .entity(erro)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}


// Sempre que usar: 
/*

throw new AppException("Credor não existe", 404);


ele retorna um Json assim:

{
  "erro": "Credor não existe",
  "status": 404
}

*/