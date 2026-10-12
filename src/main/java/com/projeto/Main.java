package com.projeto;

import com.projeto.model.Fornecedor;
import com.projeto.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        // 1. Cria o EntityManagerFactory com base no persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meu-projeto-pu");
        EntityManager em = emf.createEntityManager();

        try {
            // 2. Inicia uma transação para salvar dados
            em.getTransaction().begin();

            // Criando um Fornecedor
            Fornecedor fornecedor = new Fornecedor("Distribuidora Tech Ltda", "12.345.678/0001-99");
            
            // Criando um Produto associado ao fornecedor
            Produto produto = new Produto("Teclado Mecânico RGB", 250.00, 15, fornecedor);

            // Persistindo os dados (o CascadeType.ALL vai salvar o fornecedor junto com o produto)
            em.persist(produto);

            // Confirma a transação
            em.getTransaction().commit();
            System.out.println(">>> Dados salvos com sucesso no banco H2!");

            // 3. Realizando uma consulta (SELECT) para testar a leitura
            System.out.println("\n--- Consultando o produto cadastrado ---");
            Produto produtoBuscado = em.find(Produto.class, 1L);
            
            if (produtoBuscado != null) {
                System.out.println("Produto: " + produtoBuscado.getNome());
                System.out.println("Preço: R$ " + produtoBuscado.getPreco());
                System.out.println("Estoque: " + produtoBuscado.getQuantidadeEstoque());
                System.out.println("Fornecedor: " + produtoBuscado.getFornecedor().getNome());
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Fecha as conexões
            em.close();
            emf.close();
        }
    }
}
