package com.agilit.util;

import com.agilit.config.JPAUtil;
import com.agilit.model.Emprestimo;
import com.agilit.model.Parcela;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

/**
 * Utilitário para verificar e atualizar status de empréstimos e parcelas.
 * Implementa a lógica de transição entre estados: EM_ANDAMENTO, PAGO, ATRASADO
 */
public class VerificadorStatusEmprestimo {

    /**
     * Verifica e atualiza o status de todas as parcelas de um empréstimo
     * 
     * @param emprestimo Empréstimo a verificar
     */
    public static void verificarParcelas(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getParcelas() == null) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        
        for (Parcela parcela : emprestimo.getParcelas()) {
            if (!parcela.getPaga()) {
                // Verifica se está atrasada
                if (parcela.getDataVencimento().isBefore(hoje)) {
                    parcela.setAtrasada(true);
                }
            }
        }
    }

    /**
     * Atualiza o status do empréstimo baseado nas parcelas
     * 
     * @param emprestimo Empréstimo a atualizar
     */
    public static void atualizarStatusEmprestimo(Emprestimo emprestimo) {
        if (emprestimo == null) {
            return;
        }

        // Verifica parcelas primeiro
        verificarParcelas(emprestimo);

        // Conta parcelas pagas
        long parcelasPagas = emprestimo.getParcelas().stream()
                .filter(p -> p.getPaga() != null && p.getPaga())
                .count();
        
        emprestimo.setParcelasPagas((int) parcelasPagas);

        // Atualiza status
        if (emprestimo.todasParcelasPagas()) {
            emprestimo.setStatus("PAGO");
        } else if (emprestimo.temParcelaAtrasada()) {
            emprestimo.setStatus("ATRASADO");
        } else {
            emprestimo.setStatus("EM_ANDAMENTO");
        }
    }

    /**
     * Verifica todos os empréstimos ativos e atualiza status
     * Deve ser executado periodicamente (job diário)
     */
    public static void verificarTodosEmprestimos() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Busca todos os empréstimos não pagos
            List<Emprestimo> emprestimos = em.createQuery(
                "SELECT e FROM Emprestimo e WHERE e.status != 'PAGO'", 
                Emprestimo.class
            ).getResultList();
            
            for (Emprestimo emprestimo : emprestimos) {
                atualizarStatusEmprestimo(emprestimo);
                em.merge(emprestimo);
            }
            
            em.getTransaction().commit();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao verificar empréstimos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Verifica parcelas vencidas e não pagas
     * 
     * @return Lista de parcelas vencidas
     */
    public static List<Parcela> buscarParcelasVencidas() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            LocalDate hoje = LocalDate.now();
            
            return em.createQuery(
                "SELECT p FROM Parcela p WHERE p.paga = false AND p.dataVencimento < :hoje",
                Parcela.class
            )
            .setParameter("hoje", hoje)
            .getResultList();
            
        } finally {
            em.close();
        }
    }

    /**
     * Marca uma parcela como paga e atualiza o empréstimo
     * 
     * @param parcela Parcela a marcar como paga
     * @param em EntityManager (transação deve estar ativa)
     */
    public static void marcarParcelaPaga(Parcela parcela, EntityManager em) {
        if (parcela == null) {
            throw new IllegalArgumentException("Parcela não pode ser nula");
        }

        parcela.setPaga(true);
        parcela.setAtrasada(false);
        parcela.setDataPagamento(LocalDate.now());
        
        em.merge(parcela);
        
        // Atualiza status do empréstimo
        Emprestimo emprestimo = parcela.getEmprestimo();
        if (emprestimo != null) {
            atualizarStatusEmprestimo(emprestimo);
            em.merge(emprestimo);
        }
    }

    /**
     * Verifica se um empréstimo pode ser considerado quitado
     * 
     * @param emprestimo Empréstimo a verificar
     * @return true se todas as parcelas estão pagas
     */
    public static boolean isQuitado(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getParcelas() == null) {
            return false;
        }

        return emprestimo.getParcelas().stream()
                .allMatch(p -> p.getPaga() != null && p.getPaga());
    }

    /**
     * Calcula o total já pago do empréstimo
     * 
     * @param emprestimo Empréstimo
     * @return Valor total pago
     */
    public static double calcularTotalPago(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getParcelas() == null) {
            return 0.0;
        }

        return emprestimo.getParcelas().stream()
                .filter(p -> p.getPaga() != null && p.getPaga())
                .mapToDouble(Parcela::getValor)
                .sum();
    }

    /**
     * Calcula o total pendente do empréstimo
     * 
     * @param emprestimo Empréstimo
     * @return Valor total pendente
     */
    public static double calcularTotalPendente(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getParcelas() == null) {
            return 0.0;
        }

        return emprestimo.getParcelas().stream()
                .filter(p -> p.getPaga() == null || !p.getPaga())
                .mapToDouble(Parcela::getValor)
                .sum();
    }

    /**
     * Retorna o número de parcelas atrasadas
     * 
     * @param emprestimo Empréstimo
     * @return Número de parcelas atrasadas
     */
    public static long contarParcelasAtrasadas(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getParcelas() == null) {
            return 0;
        }

        verificarParcelas(emprestimo);
        
        return emprestimo.getParcelas().stream()
                .filter(p -> p.getAtrasada() != null && p.getAtrasada())
                .count();
    }
}

 
