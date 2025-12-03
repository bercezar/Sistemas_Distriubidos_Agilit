package com.agilit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Registro de interesse de um Devedor em uma Proposta de Empréstimo.
 * Gerencia o fluxo de confirmação bilateral (Credor + Devedor).
 */
@Entity
@Table(name = "interesse_proposta", uniqueConstraints = @UniqueConstraint(columnNames = {"proposta_id", "devedor_id"}))
public class InteresseProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proposta_id", nullable = false)
    @JsonIgnore
    private PropostaEmprestimo proposta;

    @ManyToOne
    @JoinColumn(name = "devedor_id", nullable = false)
    @JsonIgnore
    private Devedor devedor;

    @Column(name = "data_interesse", nullable = false)
    private LocalDateTime dataInteresse;

    @Column(nullable = false)
    private String status; // PENDENTE, APROVADO, REJEITADO, CANCELADO

    @Column(length = 500)
    private String mensagem; // Mensagem opcional do devedor

    @Column(name = "confirmacao_credor", nullable = false)
    private Boolean confirmacaoCredor;

    @Column(name = "confirmacao_devedor", nullable = false)
    private Boolean confirmacaoDevedor;

    @Column(name = "data_confirmacao_credor")
    private LocalDateTime dataConfirmacaoCredor;

    @Column(name = "data_confirmacao_devedor")
    private LocalDateTime dataConfirmacaoDevedor;

    @OneToOne(mappedBy = "interesseOrigem")
    @JsonIgnore
    private Emprestimo emprestimo;

    // Construtor padrão
    public InteresseProposta() {

    }

    // Construtor completo
    public InteresseProposta(Long id, PropostaEmprestimo proposta, Devedor devedor,
                            LocalDateTime dataInteresse, String status, String mensagem,
                            Boolean confirmacaoCredor, Boolean confirmacaoDevedor,
                            LocalDateTime dataConfirmacaoCredor,
                            LocalDateTime dataConfirmacaoDevedor,
                            Emprestimo emprestimo) {
        this.id = id;
        this.proposta = proposta;
        this.devedor = devedor;
        this.dataInteresse = LocalDateTime.now();
        this.status = "PENDENTE";
        this.mensagem = mensagem;
        this.confirmacaoCredor = false;
        this.confirmacaoDevedor = false;
        this.dataConfirmacaoCredor = dataConfirmacaoCredor;
        this.dataConfirmacaoDevedor = dataConfirmacaoDevedor;
        this.emprestimo = emprestimo;
    }


    // Método auxiliar para verificar se ambos confirmaram
    public boolean ambosConfirmaram() {
        return confirmacaoCredor && confirmacaoDevedor;
    }

    
    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropostaEmprestimo getProposta() {
        return proposta;
    }

    public void setProposta(PropostaEmprestimo proposta) {
        this.proposta = proposta;
    }

    public Devedor getDevedor() {
        return devedor;
    }

    public void setDevedor(Devedor devedor) {
        this.devedor = devedor;
    }

    public LocalDateTime getDataInteresse() {
        return dataInteresse;
    }

    public void setDataInteresse(LocalDateTime dataInteresse) {
        this.dataInteresse = dataInteresse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getConfirmacaoCredor() {
        return confirmacaoCredor;
    }

    public void setConfirmacaoCredor(Boolean confirmacaoCredor) {
        this.confirmacaoCredor = confirmacaoCredor;
    }

    public Boolean getConfirmacaoDevedor() {
        return confirmacaoDevedor;
    }

    public void setConfirmacaoDevedor(Boolean confirmacaoDevedor) {
        this.confirmacaoDevedor = confirmacaoDevedor;
    }

    public LocalDateTime getDataConfirmacaoCredor() {
        return dataConfirmacaoCredor;
    }

    public void setDataConfirmacaoCredor(LocalDateTime dataConfirmacaoCredor) {
        this.dataConfirmacaoCredor = dataConfirmacaoCredor;
    }

    public LocalDateTime getDataConfirmacaoDevedor() {
        return dataConfirmacaoDevedor;
    }

    public void setDataConfirmacaoDevedor(LocalDateTime dataConfirmacaoDevedor) {
        this.dataConfirmacaoDevedor = dataConfirmacaoDevedor;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }
}

 
