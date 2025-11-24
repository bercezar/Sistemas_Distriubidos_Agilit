package com.agilit.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "devedor")
public class Devedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String telefone;
    
    private String email;

    @Column(name = "senha_hash")
    private String senhaHash;

    @ManyToOne
    @JoinColumn(name = "credor_id")
    private Credor credor;

    @OneToMany(mappedBy = "devedor", cascade = CascadeType.ALL)
    private List<Emprestimo> emprestimos;

    public Devedor() {
        super();
    }

    public Devedor(Long id, String nome, String cpf, String telefone,
                   String email, String senhaHash, Credor credor,
                   List<Emprestimo> emprestimos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senhaHash = senhaHash;
        this.credor = credor;
        this.emprestimos = emprestimos;
    }

    // GETTERS E SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Credor getCredor() { return credor; }
    public void setCredor(Credor credor) { this.credor = credor; }

    public List<Emprestimo> getEmprestimos() { return emprestimos; }
    public void setEmprestimos(List<Emprestimo> emprestimos) { this.emprestimos = emprestimos; }
}
