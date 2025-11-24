package com.agilit.config;

// Exceção genérica da aplicação.
//Pode ser lançada em regras de negócio, validações e fluxos gerais.

public class AppException extends RuntimeException {

    private int status;

    public AppException(String message) {
        super(message);
        this.status = 400; // padrão BAD REQUEST
    }

    public AppException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}


// como usar:

/*

if (credor == null) {
    throw new AppException("Credor não encontrado", 404);
}
    
*/
