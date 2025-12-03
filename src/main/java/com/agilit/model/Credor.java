package com.agilit.model;

import com.agilit.config.PasswordUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "credor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credor implements Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "senha_hash")
    private String senhaHash;
    
    @Column
    private Double saldoDisponivel;

    @OneToMany(mappedBy = "credor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Devedor> clientes;

    @OneToMany(mappedBy = "credor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OfertaEmprestimo> ofertas;

    @OneToMany(mappedBy = "credor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PropostaEmprestimo> propostasCriadas;

    @OneToMany(mappedBy = "credor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Emprestimo> emprestimosRealizados;

    // Construtor padrão
    public Credor() {

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

    public List<OfertaEmprestimo> getOfertas() {
        return ofertas;
    }

    public void setOfertas(List<OfertaEmprestimo> ofertas) {
        this.ofertas = ofertas;
    }

    public List<PropostaEmprestimo> getPropostasCriadas() {
        return propostasCriadas;
    }

    public void setPropostasCriadas(List<PropostaEmprestimo> propostasCriadas) {
        this.propostasCriadas = propostasCriadas;
    }

    public List<Emprestimo> getEmprestimosRealizados() {
        return emprestimosRealizados;
    }

    public void setEmprestimosRealizados(List<Emprestimo> emprestimosRealizados) {
        this.emprestimosRealizados = emprestimosRealizados;
    }

    // Implementação dos métodos da interface Usuario
    
    @Override
    public void setSenha(String senhaPura) {
        this.senhaHash = PasswordUtil.hash(senhaPura);
    }
    
    @Override
    public boolean autenticar(String senhaPura) {
        return PasswordUtil.check(senhaPura, this.senhaHash);
    }
}
