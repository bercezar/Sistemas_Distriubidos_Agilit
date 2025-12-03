package com.agilit.model;

import com.agilit.config.PasswordUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "devedor")
public class Devedor implements Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private String telefone;
    
    @Column(nullable = false)
    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;
    
    @Column
    private String endereco;
    @Column
    private String cidade;
    @Column
    private String estado;
    @Column
    private String cep;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    

    @ManyToOne
    @JoinColumn(name = "credor_id")
    @JsonIgnore
    private Credor credor;

    @OneToMany(mappedBy = "devedor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Emprestimo> emprestimosContratados;

    @OneToMany(mappedBy = "devedor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InteresseProposta> propostasDesejadas;

    // Construtor padrão
    public Devedor() {
    
    }

    // Construtor completo
    public Devedor(Long id, String nome, String cpf, String telefone,
                   String email, String senhaHash, String endereco,
                   String cidade, String estado, String cep,
                   LocalDate dataNascimento, Double renda,
                   Credor credor, List<Emprestimo> emprestimosContratados) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senhaHash = senhaHash;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.dataNascimento = dataNascimento;
        this.credor = credor;
        this.emprestimosContratados = emprestimosContratados;
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

    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Credor getCredor() {
        return credor;
    }
    
    public void setCredor(Credor credor) {
        this.credor = credor;
    }

    public List<Emprestimo> getEmprestimosContratados() {
        return emprestimosContratados;
    }
    
    public void setEmprestimosContratados(List<Emprestimo> emprestimosContratados) {
        this.emprestimosContratados = emprestimosContratados;
    }

    public List<InteresseProposta> getPropostasDesejadas() {
        return propostasDesejadas;
    }
    
    public void setPropostasDesejadas(List<InteresseProposta> propostasDesejadas) {
        this.propostasDesejadas = propostasDesejadas;
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
