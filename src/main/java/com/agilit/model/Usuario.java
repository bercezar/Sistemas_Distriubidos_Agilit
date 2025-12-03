package com.agilit.model;

/**
 * Interface base para usuários do sistema (Credor e Devedor).
 * Define contrato mínimo para autenticação conforme diagrama de classes.
 */
public interface Usuario {
    
    /**
     * Obtém o email do usuário
     * @return email
     */
    String getEmail();
    
    /**
     * Obtém o hash da senha
     * @return senha hash
     */
    String getSenhaHash();
    
    /**
     * Define a senha do usuário (será hasheada automaticamente)
     * @param senhaPura senha em texto plano
     */
    void setSenha(String senhaPura);
    
    /**
     * Autentica o usuário comparando senha fornecida com hash armazenado
     * @param senhaPura senha em texto plano
     * @return true se autenticado com sucesso
     */
    boolean autenticar(String senhaPura);
}

 
