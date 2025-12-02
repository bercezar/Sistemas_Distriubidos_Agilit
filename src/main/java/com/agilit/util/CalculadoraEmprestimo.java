package com.agilit.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário para cálculos relacionados a empréstimos.
 * Calcula juros, valores de parcelas e datas de vencimento.
 */
public class CalculadoraEmprestimo {

    /**
     * Calcula o valor de cada parcela com juros simples
     * 
     * @param valorPrincipal Valor principal do empréstimo
     * @param numeroParcelas Número de parcelas
     * @param taxaJuros Taxa de juros (percentual, ex: 2.5 para 2.5%)
     * @return Valor de cada parcela
     */
    public static double calcularValorParcela(double valorPrincipal, int numeroParcelas, double taxaJuros) {
        if (numeroParcelas <= 0) {
            throw new IllegalArgumentException("Número de parcelas deve ser maior que zero");
        }
        
        double valorTotal = calcularValorTotalComJuros(valorPrincipal, taxaJuros, numeroParcelas);
        return valorTotal / numeroParcelas;
    }

    /**
     * Calcula o valor total do empréstimo com juros simples
     * 
     * @param valorPrincipal Valor principal
     * @param taxaJuros Taxa de juros (percentual)
     * @param numeroParcelas Número de parcelas
     * @return Valor total com juros
     */
    public static double calcularValorTotalComJuros(double valorPrincipal, double taxaJuros, int numeroParcelas) {
        double juros = calcularJurosSimples(valorPrincipal, taxaJuros, numeroParcelas);
        return valorPrincipal + juros;
    }

    /**
     * Calcula juros simples
     * Fórmula: J = P * (i/100) * n
     * 
     * @param valorPrincipal Valor principal
     * @param taxaJuros Taxa de juros (percentual)
     * @param numeroParcelas Número de parcelas (período)
     * @return Valor dos juros
     */
    public static double calcularJurosSimples(double valorPrincipal, double taxaJuros, int numeroParcelas) {
        return valorPrincipal * (taxaJuros / 100.0) * numeroParcelas;
    }

    /**
     * Calcula juros compostos
     * Fórmula: M = P * (1 + i)^n, onde J = M - P
     * 
     * @param valorPrincipal Valor principal
     * @param taxaJuros Taxa de juros (percentual)
     * @param numeroParcelas Número de parcelas (período)
     * @return Valor dos juros
     */
    public static double calcularJurosCompostos(double valorPrincipal, double taxaJuros, int numeroParcelas) {
        double montante = valorPrincipal * Math.pow(1 + (taxaJuros / 100.0), numeroParcelas);
        return montante - valorPrincipal;
    }

    /**
     * Calcula as datas de vencimento de todas as parcelas
     * 
     * @param dataInicio Data de início do empréstimo
     * @param numeroParcelas Número de parcelas
     * @param diasAtePrimeiraCobranca Dias até a primeira cobrança
     * @return Lista com as datas de vencimento
     */
    public static List<LocalDate> calcularDatasParcelas(LocalDate dataInicio, int numeroParcelas, int diasAtePrimeiraCobranca) {
        List<LocalDate> datas = new ArrayList<>();
        
        // Primeira parcela
        LocalDate primeiraParcela = dataInicio.plusDays(diasAtePrimeiraCobranca);
        datas.add(primeiraParcela);
        
        // Demais parcelas (mensais)
        for (int i = 1; i < numeroParcelas; i++) {
            LocalDate proximaData = primeiraParcela.plusMonths(i);
            datas.add(proximaData);
        }
        
        return datas;
    }

    /**
     * Calcula a data da primeira parcela
     * 
     * @param dataInicio Data de início
     * @param diasAtePrimeiraCobranca Dias até primeira cobrança
     * @return Data da primeira parcela
     */
    public static LocalDate calcularDataPrimeiraParcela(LocalDate dataInicio, int diasAtePrimeiraCobranca) {
        return dataInicio.plusDays(diasAtePrimeiraCobranca);
    }

    /**
     * Calcula a data de vencimento final (última parcela)
     * 
     * @param dataInicio Data de início
     * @param numeroParcelas Número de parcelas
     * @param diasAtePrimeiraCobranca Dias até primeira cobrança
     * @return Data da última parcela
     */
    public static LocalDate calcularDataVencimentoFinal(LocalDate dataInicio, int numeroParcelas, int diasAtePrimeiraCobranca) {
        LocalDate primeiraParcela = calcularDataPrimeiraParcela(dataInicio, diasAtePrimeiraCobranca);
        return primeiraParcela.plusMonths(numeroParcelas - 1);
    }

    /**
     * Calcula o valor de cada parcela para diferentes opções de parcelamento
     * 
     * @param valorPrincipal Valor principal
     * @param parcelasMinimas Número mínimo de parcelas
     * @param parcelasMaximas Número máximo de parcelas
     * @param taxaJuros Taxa de juros
     * @return Lista com valores para cada opção de parcelamento
     */
    public static List<OpcaoParcela> calcularOpcoesParcelas(double valorPrincipal, int parcelasMinimas, 
                                                            int parcelasMaximas, double taxaJuros) {
        List<OpcaoParcela> opcoes = new ArrayList<>();
        
        for (int numParcelas = parcelasMinimas; numParcelas <= parcelasMaximas; numParcelas++) {
            double valorParcela = calcularValorParcela(valorPrincipal, numParcelas, taxaJuros);
            double valorTotal = calcularValorTotalComJuros(valorPrincipal, taxaJuros, numParcelas);
            double juros = valorTotal - valorPrincipal;
            
            opcoes.add(new OpcaoParcela(numParcelas, valorParcela, valorTotal, juros));
        }
        
        return opcoes;
    }

    /**
     * Classe auxiliar para representar uma opção de parcelamento
     */
    public static class OpcaoParcela {
        private int numeroParcelas;
        private double valorParcela;
        private double valorTotal;
        private double juros;

        public OpcaoParcela(int numeroParcelas, double valorParcela, double valorTotal, double juros) {
            this.numeroParcelas = numeroParcelas;
            this.valorParcela = valorParcela;
            this.valorTotal = valorTotal;
            this.juros = juros;
        }

        public int getNumeroParcelas() {
            return numeroParcelas;
        }

        public double getValorParcela() {
            return valorParcela;
        }

        public double getValorTotal() {
            return valorTotal;
        }

        public double getJuros() {
            return juros;
        }

        @Override
        public String toString() {
            return String.format("%dx de R$ %.2f (Total: R$ %.2f, Juros: R$ %.2f)", 
                               numeroParcelas, valorParcela, valorTotal, juros);
        }
    }

    /**
     * Arredonda valor para 2 casas decimais
     * 
     * @param valor Valor a arredondar
     * @return Valor arredondado
     */
    public static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}