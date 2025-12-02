package com.agilit.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Utilitário para gerar IDs públicos únicos para Propostas de Empréstimo.
 * Formato: #ABC123 (# + 6 caracteres alfanuméricos maiúsculos)
 */
public class GeradorIdPublico {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TAMANHO_ID = 6;
    private static final Random random = new SecureRandom();

    /**
     * Gera um ID público único no formato #ABC123
     * 
     * @return String com o ID público gerado
     */
    public static String gerar() {
        StringBuilder id = new StringBuilder("#");
        
        for (int i = 0; i < TAMANHO_ID; i++) {
            int index = random.nextInt(CARACTERES.length());
            id.append(CARACTERES.charAt(index));
        }
        
        return id.toString();
    }

    /**
     * Valida se um ID público está no formato correto
     * 
     * @param idPublico ID a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean validar(String idPublico) {
        if (idPublico == null || idPublico.length() != (TAMANHO_ID + 1)) {
            return false;
        }
        
        if (!idPublico.startsWith("#")) {
            return false;
        }
        
        String parte = idPublico.substring(1);
        return parte.matches("[A-Z0-9]{" + TAMANHO_ID + "}");
    }

    /**
     * Gera múltiplos IDs públicos (útil para testes)
     * 
     * @param quantidade Número de IDs a gerar
     * @return Array com os IDs gerados
     */
    public static String[] gerarMultiplos(int quantidade) {
        String[] ids = new String[quantidade];
        for (int i = 0; i < quantidade; i++) {
            ids[i] = gerar();
        }
        return ids;
    }
}

 
