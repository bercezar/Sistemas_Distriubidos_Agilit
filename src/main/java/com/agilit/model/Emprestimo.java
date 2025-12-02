package com.agilit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Empréstimo concedido por um Credor a um Devedor.
 * Gerado a partir de uma Proposta aceita e um Interesse confirmado.
 * Possui parcelas para controle de pagamento.
 */
@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "devedor_id", nullable = false)
    @JsonIgnore
    private Devedor devedor;

    @ManyToOne
    @JoinColumn(name = "credor_id", nullable = false)
    @JsonIgnore
    private Credor credor;

    @ManyToOne
    @JoinColumn(name = "proposta_origem_id")
    @JsonIgnore
    private PropostaEmprestimo propostaOrigem;

    @OneToOne
    @JoinColumn(name = "interesse_origem_id")
    @JsonIgnore
    private InteresseProposta interesseOrigem;

    @Column(name = "valor_principal", nullable = false)
    private Double valorPrincipal;

    @Column(name = "juros_aplicados", nullable = false)
    private Double jurosAplicados;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @Column(name = "numero_parcelas", nullable = false)
    private Integer numeroParcelas;

    @Column(name = "parcelas_pagas", nullable = false)
    private Integer parcelasPagas;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(nullable = false)
    private String status; // EM_ANDAMENTO, PAGO, ATRASADO

    @OneToMany(mappedBy = "emprestimo", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Parcela> parcelas;

    // Construtor padrão
    public Emprestimo() {
        super();
        this.status = "EM_ANDAMENTO";
        this.parcelasPagas = 0;
        this.dataInicio = LocalDate.now();
    }

    // Construtor completo
    public Emprestimo(Long id, Devedor devedor, Credor credor,
                     PropostaEmprestimo propostaOrigem, InteresseProposta interesseOrigem,
                     Double valorPrincipal, Double jurosAplicados, Double valorTotal,
                     Integer numeroParcelas, Integer parcelasPagas,
                     LocalDate dataInicio, LocalDate dataVencimento, String status,
                     List<Parcela> parcelas) {
        this.id = id;
        this.devedor = devedor;
        this.credor = credor;
        this.propostaOrigem = propostaOrigem;
        this.interesseOrigem = interesseOrigem;
        this.valorPrincipal = valorPrincipal;
        this.jurosAplicados = jurosAplicados;
        this.valorTotal = valorTotal;
        this.numeroParcelas = numeroParcelas;
        this.parcelasPagas = parcelasPagas;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.parcelas = parcelas;
    }

    // Métodos auxiliares

    /**
     * Verifica se todas as parcelas foram pagas
     */
    public boolean todasParcelasPagas() {
        return parcelasPagas != null && numeroParcelas != null
               && parcelasPagas.equals(numeroParcelas);
    }

    /**
     * Verifica se existe alguma parcela atrasada
     */
    public boolean temParcelaAtrasada() {
        if (parcelas == null || parcelas.isEmpty()) {
            return false;
        }
        return parcelas.stream().anyMatch(p -> p.getAtrasada() != null && p.getAtrasada());
    }

    /**
     * Atualiza o status do empréstimo baseado nas parcelas
     */
    public void atualizarStatus() {
        if (todasParcelasPagas()) {
            this.status = "PAGO";
        } else if (temParcelaAtrasada()) {
            this.status = "ATRASADO";
        } else {
            this.status = "EM_ANDAMENTO";
        }
    }

    // Getters e Setters

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

    public PropostaEmprestimo getPropostaOrigem() {
        return propostaOrigem;
    }

    public void setPropostaOrigem(PropostaEmprestimo propostaOrigem) {
        this.propostaOrigem = propostaOrigem;
    }

    public InteresseProposta getInteresseOrigem() {
        return interesseOrigem;
    }

    public void setInteresseOrigem(InteresseProposta interesseOrigem) {
        this.interesseOrigem = interesseOrigem;
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

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public Integer getParcelasPagas() {
        return parcelasPagas;
    }

    public void setParcelasPagas(Integer parcelasPagas) {
        this.parcelasPagas = parcelasPagas;
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

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }
}