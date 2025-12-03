package com.agilit.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a Interface Usuario e suas implementações.
 * Valida comportamento de autenticação em Credor e Devedor.
 */
class UsuarioTest {

    @Test
    @DisplayName("Credor deve implementar interface Usuario")
    void credorDeveImplementarUsuario() {
        Credor credor = new Credor();
        assertTrue(credor instanceof Usuario, "Credor deve implementar Usuario");
    }

    @Test
    @DisplayName("Devedor deve implementar interface Usuario")
    void devedorDeveImplementarUsuario() {
        Devedor devedor = new Devedor();
        assertTrue(devedor instanceof Usuario, "Devedor deve implementar Usuario");
    }

    @Test
    @DisplayName("Credor.setSenha() deve hashear senha automaticamente")
    void credorSetSenhaDeveHashear() {
        Credor credor = new Credor();
        String senhaOriginal = "senha123";
        
        credor.setSenha(senhaOriginal);
        
        assertNotNull(credor.getSenhaHash(), "Hash não deve ser null");
        assertNotEquals(senhaOriginal, credor.getSenhaHash(), "Hash deve ser diferente da senha original");
        assertTrue(credor.getSenhaHash().startsWith("$2a$"), "Hash deve usar BCrypt");
    }

    @Test
    @DisplayName("Devedor.setSenha() deve hashear senha automaticamente")
    void devedorSetSenhaDeveHashear() {
        Devedor devedor = new Devedor();
        String senhaOriginal = "senha123";
        
        devedor.setSenha(senhaOriginal);
        
        assertNotNull(devedor.getSenhaHash(), "Hash não deve ser null");
        assertNotEquals(senhaOriginal, devedor.getSenhaHash(), "Hash deve ser diferente da senha original");
        assertTrue(devedor.getSenhaHash().startsWith("$2a$"), "Hash deve usar BCrypt");
    }

    @Test
    @DisplayName("Credor.autenticar() deve validar senha correta")
    void credorAutenticarSenhaCorreta() {
        Credor credor = new Credor();
        String senha = "senha123";
        
        credor.setSenha(senha);
        
        assertTrue(credor.autenticar(senha), "Deve autenticar com senha correta");
    }

    @Test
    @DisplayName("Credor.autenticar() deve rejeitar senha incorreta")
    void credorAutenticarSenhaIncorreta() {
        Credor credor = new Credor();
        
        credor.setSenha("senha123");
        
        assertFalse(credor.autenticar("senhaErrada"), "Deve rejeitar senha incorreta");
    }

    @Test
    @DisplayName("Devedor.autenticar() deve validar senha correta")
    void devedorAutenticarSenhaCorreta() {
        Devedor devedor = new Devedor();
        String senha = "senha123";
        
        devedor.setSenha(senha);
        
        assertTrue(devedor.autenticar(senha), "Deve autenticar com senha correta");
    }

    @Test
    @DisplayName("Devedor.autenticar() deve rejeitar senha incorreta")
    void devedorAutenticarSenhaIncorreta() {
        Devedor devedor = new Devedor();
        
        devedor.setSenha("senha123");
        
        assertFalse(devedor.autenticar("senhaErrada"), "Deve rejeitar senha incorreta");
    }

    @Test
    @DisplayName("Polimorfismo: Usuario deve funcionar com Credor")
    void polimorfismoComCredor() {
        Usuario usuario = new Credor();
        usuario.setSenha("senha123");
        
        assertTrue(usuario.autenticar("senha123"), "Polimorfismo deve funcionar com Credor");
    }

    @Test
    @DisplayName("Polimorfismo: Usuario deve funcionar com Devedor")
    void polimorfismoComDevedor() {
        Usuario usuario = new Devedor();
        usuario.setSenha("senha123");
        
        assertTrue(usuario.autenticar("senha123"), "Polimorfismo deve funcionar com Devedor");
    }

    @Test
    @DisplayName("Credor deve ter email e senhaHash")
    void credorDeveTeremailESenhaHash() {
        Credor credor = new Credor();
        credor.setEmail("credor@test.com");
        credor.setSenha("senha123");
        
        assertEquals("credor@test.com", credor.getEmail());
        assertNotNull(credor.getSenhaHash());
    }

    @Test
    @DisplayName("Devedor deve ter email e senhaHash")
    void devedorDeveTeremailESenhaHash() {
        Devedor devedor = new Devedor();
        devedor.setEmail("devedor@test.com");
        devedor.setSenha("senha123");
        
        assertEquals("devedor@test.com", devedor.getEmail());
        assertNotNull(devedor.getSenhaHash());
    }

    @Test
    @DisplayName("Hash deve ser diferente para mesma senha em instâncias diferentes")
    void hashDiferenteParaMesmaSenha() {
        Credor credor1 = new Credor();
        Credor credor2 = new Credor();
        
        credor1.setSenha("senha123");
        credor2.setSenha("senha123");
        
        assertNotEquals(credor1.getSenhaHash(), credor2.getSenhaHash(), 
            "Hash deve ser diferente devido ao salt do BCrypt");
    }

    @Test
    @DisplayName("Autenticação deve funcionar mesmo com hashes diferentes")
    void autenticacaoComHashesDiferentes() {
        Credor credor1 = new Credor();
        Credor credor2 = new Credor();
        String senha = "senha123";
        
        credor1.setSenha(senha);
        credor2.setSenha(senha);
        
        assertTrue(credor1.autenticar(senha), "Credor1 deve autenticar");
        assertTrue(credor2.autenticar(senha), "Credor2 deve autenticar");
    }
}

 
