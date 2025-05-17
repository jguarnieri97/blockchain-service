package com.example.blockchainremito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BlockchainRemitoPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainRemitoPocApplication.class, args);
    }

    
}
