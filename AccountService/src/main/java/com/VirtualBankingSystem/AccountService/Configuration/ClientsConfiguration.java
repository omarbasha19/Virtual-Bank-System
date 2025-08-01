package com.VirtualBankingSystem.AccountService.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsConfiguration {
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("userService")
    public WebClient userServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://user-service")
                .build();
    }
}
