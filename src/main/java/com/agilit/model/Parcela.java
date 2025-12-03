package com.agilit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Parcela individual de um Empréstimo.
 * Controla pagamento, vencimento e atrasos.
 */
@Entity
@Table(name = "parcela")
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emprestimo_id", nullable = false)
    @JsonIgnore
    private Emprestimo emprestimo;

    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = false)
    private Boolean paga;

    @Column(nullable = false)
    private Boolean atrasada;

    // Construtor padrão
    public Parcela() {
        
    }

    // Construtor completo
    public Parcela(Long id, Emprestimo emprestimo, Integer numeroParcela,
                  Double valor, LocalDate dataVencimento, LocalDate dataPagamento,
                  Boolean paga, Boolean atrasada) {
        this.id = id;
        this.emprestimo = emprestimo;
        this.numeroParcela = numeroParcela;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.paga = false;
        this.atrasada = false;
    }

    // Método auxiliar para calcular dias de atraso
    public Integer getDiasAtraso() {
        if (paga || dataVencimento == null) {
            return 0;
        }
        
        LocalDate hoje = LocalDate.now();
        if (hoje.isAfter(dataVencimento)) {
            return (int) ChronoUnit.DAYS.between(dataVencimento, hoje);
        }
        
        return 0;
    }

    // Método para verificar se está atrasada
    public void verificarAtraso() {
        if (!paga && dataVencimento != null) {
            LocalDate hoje = LocalDate.now();
            this.atrasada = hoje.isAfter(dataVencimento);
        }
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Boolean getPaga() {
        return paga;
    }

    public void setPaga(Boolean paga) {
        this.paga = paga;
    }

    public Boolean getAtrasada() {
        return atrasada;
    }

    public void setAtrasada(Boolean atrasada) {
        this.atrasada = atrasada;
    }
}

 
