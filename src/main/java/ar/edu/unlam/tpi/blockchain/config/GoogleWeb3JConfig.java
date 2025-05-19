package ar.edu.unlam.tpi.blockchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;


@Configuration
public class GoogleWeb3JConfig {
    
    @Value("${google.cloud.web3.uri}")
    private String gcpRpcUrl;

    @Value("${private.key}")
    private String privateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(gcpRpcUrl));
    }

    @Bean(name = "privateKey")
    public String privateKey(){
        return privateKey;
    }
}
