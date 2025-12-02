package com.agilit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Sistema de notificações para Credores e Devedores.
 * Notifica sobre novos interesses, aprovações, confirmações, vencimentos e atrasos.
 */
@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_destinatario", nullable = false)
    private String tipoDestinatario; // CREDOR ou DEVEDOR

    @Column(name = "destinatario_id", nullable = false)
    private Long destinatarioId;

    @Column(nullable = false)
    private String tipo; // NOVO_INTERESSE, APROVACAO, CONFIRMACAO, VENCIMENTO, ATRASO

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String mensagem;

    @Column(nullable = false)
    private Boolean lida;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Column(length = 100)
    private String referencia; // ID da entidade relacionada (ex: ID do empréstimo)

    @Column(name = "tipo_referencia", length = 50)
    private String tipoReferencia; // Tipo da entidade (ex: EMPRESTIMO, PROPOSTA, PARCELA)

    // Construtor padrão
    public Notificacao() {
        super();
        this.dataCriacao = LocalDateTime.now();
        this.lida = false;
    }

    // Construtor completo
    public Notificacao(Long id, String tipoDestinatario, Long destinatarioId,
                      String tipo, String titulo, String mensagem, Boolean lida,
                      LocalDateTime dataCriacao, LocalDateTime dataLeitura,
                      String referencia, String tipoReferencia) {
        this.id = id;
        this.tipoDestinatario = tipoDestinatario;
        this.destinatarioId = destinatarioId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.lida = lida;
        this.dataCriacao = dataCriacao;
        this.dataLeitura = dataLeitura;
        this.referencia = referencia;
        this.tipoReferencia = tipoReferencia;
    }

    // Método auxiliar para marcar como lida
    public void marcarComoLida() {
        this.lida = true;
        this.dataLeitura = LocalDateTime.now();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(LocalDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipoReferencia() {
        return tipoReferencia;
    }

    public void setTipoReferencia(String tipoReferencia) {
        this.tipoReferencia = tipoReferencia;
    }
}

 
