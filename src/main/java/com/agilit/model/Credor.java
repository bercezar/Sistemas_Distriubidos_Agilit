package com.agilit.model;

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
    private String email;
    private Double saldoDisponivel;

    @OneToMany(mappedBy = "credor", cascade = CascadeType.ALL)
    private List<Devedor> clientes;


    
    public Credor (){
        super();
        this.saldoDisponivel = 0.0;

    }

    public Credor(Long id, String nome, String email, Double saldoDisponivel, List<Devedor> clientes) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.saldoDisponivel = saldoDisponivel;
        this.clientes = clientes;
    }



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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
