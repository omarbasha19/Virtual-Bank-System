package com.VirtualBankingSystem.TransactionService.Client;

import com.VirtualBankingSystem.TransactionService.DTO.Request.AccountTransferRequestDTO;
import com.VirtualBankingSystem.TransactionService.DTO.Response.AccountResponseDTO;
import com.VirtualBankingSystem.TransactionService.DTO.Response.AccountTransferResponseDTO;
import com.VirtualBankingSystem.TransactionService.Exception.ServiceInternalErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AccountServiceClient {
    @Autowired
    @Qualifier("accountService")
    private WebClient webClient;

    public AccountResponseDTO getUserAccounts(UUID accountId) {
        return webClient.get()
                .uri("/accounts/{accountId}", accountId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.statusCode().value() == 400
                                ? Mono.error(new IllegalArgumentException("Bad request for account ID: " + accountId))
                                : response.statusCode().value() == 404
                                ? Mono.error(new IllegalArgumentException("Account not found with ID: " + accountId))
                                : Mono.error(new RuntimeException("Client error"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ServiceInternalErrorException("Account service server error"))
                )
                .bodyToMono(AccountResponseDTO.class)
                .block();
    }

    public AccountTransferResponseDTO updateAccountBalance(AccountTransferRequestDTO accountTransferRequestDTO) {
        return webClient.post()
                .uri("/accounts/transfer")
                .bodyValue(accountTransferRequestDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.statusCode().value() == 400
                                ? Mono.error(new IllegalArgumentException("Bad request for account transfer"))
                                : response.statusCode().value() == 404
                                ? Mono.error(new IllegalArgumentException("Account not found for transfer"))
                                : Mono.error(new RuntimeException("Client error"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ServiceInternalErrorException("Account service server error"))
                )
                .bodyToMono(AccountTransferResponseDTO.class)
                .block();
    }
}
