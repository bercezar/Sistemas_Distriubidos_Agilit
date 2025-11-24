package com.agilit.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "devedor_id")
    private Devedor devedor;

    @ManyToOne
    @JoinColumn(name = "credor_id")
    private Credor credor;

    private Double valorPrincipal;
    private Double jurosAplicados;
    private Double valorTotal;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;
    private String status; // Em Andamento / Pago / Atrasado

    public Emprestimo (){
        super();
    }

    public Emprestimo(Long id, Devedor devedor, Credor credor, Double valorPrincipal, Double jurosAplicados,
            Double valorTotal, LocalDate dataInicio, LocalDate dataVencimento, String status) {
        this.id = id;
        this.devedor = devedor;
        this.credor = credor;
        this.valorPrincipal = valorPrincipal;
        this.jurosAplicados = jurosAplicados;
        this.valorTotal = valorTotal;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }


        
    // Getters e setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Devedor getDevedor() {
        return devedor;
    }

    public void setDevedor(Devedor devedor) {
        this.devedor = devedor;
    }

    public Credor getCredor() {
        return credor;
    }

    public void setCredor(Credor credor) {
        this.credor = credor;
    }

    public Double getValorPrincipal() {
        return valorPrincipal;
    }

    public void setValorPrincipal(Double valorPrincipal) {
        this.valorPrincipal = valorPrincipal;
    }

    public Double getJurosAplicados() {
        return jurosAplicados;
    }

    public void setJurosAplicados(Double jurosAplicados) {
        this.jurosAplicados = jurosAplicados;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}


// Continue criando OfertaEmprestimo, Notificacao, Pagamento seguindo a mesma lógica.