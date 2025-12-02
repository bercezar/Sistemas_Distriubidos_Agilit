package com.agilit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Proposta de Empréstimo pública derivada de uma Oferta.
 * Visível para todos os Devedores.
 * Possui ID público único para identificação.
 */
@Entity
@Table(name = "proposta_emprestimo")
public class PropostaEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_publico", nullable = false, unique = true)
    private String idPublico; // Formato: #ABC123

    @ManyToOne
    @JoinColumn(name = "oferta_origem_id", nullable = false)
    @JsonIgnore
    private OfertaEmprestimo ofertaOrigem;

    @ManyToOne
    @JoinColumn(name = "credor_id", nullable = false)
    @JsonIgnore
    private Credor credor;

    @Column(name = "nome_credor", nullable = false)
    private String nomeCredor; // Desnormalizado para performance

    @Column(name = "valor_disponivel", nullable = false)
    private Double valorDisponivel;

    @Column(name = "parcelas_minimas", nullable = false)
    private Integer parcelasMinimas;

    @Column(name = "parcelas_maximas", nullable = false)
    private Integer parcelasMaximas;

    @Column(name = "dias_ate_primeira_cobranca", nullable = false)
    private Integer diasAtePrimeiraCobranca;

    @Column(name = "taxa_juros", nullable = false)
    private Double taxaJuros;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private String status; // ATIVA, CANCELADA, ACEITA

    @OneToMany(mappedBy = "proposta", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InteresseProposta> interesses;

    @OneToMany(mappedBy = "propostaOrigem", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Emprestimo> emprestimos;

    // Construtor padrão
    public PropostaEmprestimo() {
        super();
        this.dataCriacao = LocalDateTime.now();
        this.status = "ATIVA";
    }

    // Construtor completo
    public PropostaEmprestimo(Long id, String idPublico, OfertaEmprestimo ofertaOrigem,
                             Credor credor, String nomeCredor, Double valorDisponivel,
                             Integer parcelasMinimas, Integer parcelasMaximas,
                             Integer diasAtePrimeiraCobranca, Double taxaJuros,
                             LocalDateTime dataCriacao, String status,
                             List<InteresseProposta> interesses,
                             List<Emprestimo> emprestimos) {
        this.id = id;
        this.idPublico = idPublico;
        this.ofertaOrigem = ofertaOrigem;
        this.credor = credor;
        this.nomeCredor = nomeCredor;
        this.valorDisponivel = valorDisponivel;
        this.parcelasMinimas = parcelasMinimas;
        this.parcelasMaximas = parcelasMaximas;
        this.diasAtePrimeiraCobranca = diasAtePrimeiraCobranca;
        this.taxaJuros = taxaJuros;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.interesses = interesses;
        this.emprestimos = emprestimos;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdPublico() {
        return idPublico;
    }

    public void setIdPublico(String idPublico) {
        this.idPublico = idPublico;
    }

    public OfertaEmprestimo getOfertaOrigem() {
        return ofertaOrigem;
    }

    public void setOfertaOrigem(OfertaEmprestimo ofertaOrigem) {
        this.ofertaOrigem = ofertaOrigem;
    }

    public Credor getCredor() {
        return credor;
    }

    public void setCredor(Credor credor) {
        this.credor = credor;
    }

    public String getNomeCredor() {
        return nomeCredor;
    }

    public void setNomeCredor(String nomeCredor) {
        this.nomeCredor = nomeCredor;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<InteresseProposta> getInteresses() {
        return interesses;
    }

    public void setInteresses(List<InteresseProposta> interesses) {
        this.interesses = interesses;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}

// Made with Bob
