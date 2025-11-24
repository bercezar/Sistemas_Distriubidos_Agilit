package com.agilit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectTest {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("agilitPU");
            EntityManager em = emf.createEntityManager();

            System.out.println("\n====================================");
            System.out.println(" Conexão com NEON realizada com sucesso! ");
            System.out.println("====================================\n");

            em.close();
            emf.close();

        } catch (Exception e) {
            System.err.println("\n========== ERRO NA CONEXÃO ==========");
            e.printStackTrace();
            System.out.println("=====================================\n");
        }
    }
}
