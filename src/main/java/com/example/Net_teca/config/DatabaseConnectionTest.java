package com.example.Net_teca.config;

import com.example.Net_teca.repository.ProdutoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest {

    @Autowired
    private ProdutoRepository repository;

    @PostConstruct
    public void testar() {
        System.out.println("Conexão com PostgreSQL OK!");
        System.out.println("Quantidade de produtos: " + repository.count());
    }
}