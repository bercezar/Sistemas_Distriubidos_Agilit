package com.agilit;

import jakarta.persistence.EntityManager;
import com.agilit.config.JPAUtil;


public class ConnectTest {
    public static void main(String[] args) {
        try {
        
            EntityManager em = JPAUtil.getEntityManager();

            System.out.println("\n====================================");
            System.out.println(" Conexão com NEON realizada com sucesso! ");
            System.out.println("====================================\n");

            em.close();

        } catch (Exception e) {
            System.err.println("\n========== ERRO NA CONEXÃO ==========");
            e.printStackTrace();
            System.out.println("=====================================\n");
        }
    }
}
