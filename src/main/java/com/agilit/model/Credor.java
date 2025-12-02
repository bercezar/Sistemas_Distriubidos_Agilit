package com.agilit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "credor")
public class Credor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    @Column(nullable = false, unique = true)
    private String cpf;
    
    private String telefone;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "senha_hash")
    private String senhaHash;
    
    private Double saldoDisponivel;

    @OneToMany(mappedBy = "credor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Devedor> clientes;

    // Construtor padrão
    public Credor() {
        super();
        this.saldoDisponivel = 0.0;
    }

    // Construtor completo
    public Credor(Long id, String nome, String cpf, String telefone,
                  String email, String senhaHash, Double saldoDisponivel,
                  List<Devedor> clientes) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senhaHash = senhaHash;
        this.saldoDisponivel = saldoDisponivel;
        this.clientes = clientes;
    }

    // Getters e Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Double getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public void setSaldoDisponivel(Double saldoDisponivel) {
        this.saldoDisponivel = saldoDisponivel;
    }

    public List<Devedor> getClientes() {
        return clientes;
    }

    public void setClientes(List<Devedor> clientes) {
        this.clientes = clientes;
    }
}
