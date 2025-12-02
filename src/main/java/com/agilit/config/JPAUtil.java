package com.agilit.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("agilitPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
