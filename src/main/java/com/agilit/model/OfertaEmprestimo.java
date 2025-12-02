package com.agilit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Oferta de Empréstimo criada pelo Credor.
 * É privada (apenas o Credor que criou pode ver).
 * Serve como template para criar Propostas públicas.
 */
@Entity
@Table(name = "oferta_emprestimo")
public class OfertaEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credor_id", nullable = false)
    @JsonIgnore
    private Credor credor;

    @Column(name = "valor_disponivel", nullable = false)
    private Double valorDisponivel;

    @Column(name = "parcelas_minimas", nullable = false)
    private Integer parcelasMinimas;

    @Column(name = "parcelas_maximas", nullable = false)
    private Integer parcelasMaximas;

    @Column(name = "dias_ate_primeira_cobranca", nullable = false)
    private Integer diasAtePrimeiraCobranca;

    @Column(name = "taxa_juros", nullable = false)
    private Double taxaJuros; // Percentual (ex: 2.5 para 2.5%)

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Boolean ativa;

    @OneToMany(mappedBy = "ofertaOrigem", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PropostaEmprestimo> propostas;

    // Construtor padrão
    public OfertaEmprestimo() {
        super();
        this.dataCriacao = LocalDateTime.now();
        this.ativa = true;
    }

    // Construtor completo
    public OfertaEmprestimo(Long id, Credor credor, Double valorDisponivel,
                           Integer parcelasMinimas, Integer parcelasMaximas,
                           Integer diasAtePrimeiraCobranca, Double taxaJuros,
                           LocalDateTime dataCriacao, Boolean ativa,
                           List<PropostaEmprestimo> propostas) {
        this.id = id;
        this.credor = credor;
        this.valorDisponivel = valorDisponivel;
        this.parcelasMinimas = parcelasMinimas;
        this.parcelasMaximas = parcelasMaximas;
        this.diasAtePrimeiraCobranca = diasAtePrimeiraCobranca;
        this.taxaJuros = taxaJuros;
        this.dataCriacao = dataCriacao;
        this.ativa = ativa;
        this.propostas = propostas;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Credor getCredor() {
        return credor;
    }

    public void setCredor(Credor credor) {
        this.credor = credor;
    }

    public Double getValorDisponivel() {
        return valorDisponivel;
    }

    public void setValorDisponivel(Double valorDisponivel) {
        this.valorDisponivel = valorDisponivel;
    }

    public Integer getParcelasMinimas() {
        return parcelasMinimas;
    }

    public void setParcelasMinimas(Integer parcelasMinimas) {
        this.parcelasMinimas = parcelasMinimas;
    }

    public Integer getParcelasMaximas() {
        return parcelasMaximas;
    }

    public void setParcelasMaximas(Integer parcelasMaximas) {
        this.parcelasMaximas = parcelasMaximas;
    }

    public Integer getDiasAtePrimeiraCobranca() {
        return diasAtePrimeiraCobranca;
    }

    public void setDiasAtePrimeiraCobranca(Integer diasAtePrimeiraCobranca) {
        this.diasAtePrimeiraCobranca = diasAtePrimeiraCobranca;
    }

    public Double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(Double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public List<PropostaEmprestimo> getPropostas() {
        return propostas;
    }

    public void setPropostas(List<PropostaEmprestimo> propostas) {
        this.propostas = propostas;
    }
}

// Made with Bob
