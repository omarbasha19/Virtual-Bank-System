package com.VirtualBankingSystem.TransactionService.Service;

import com.VirtualBankingSystem.TransactionService.Adapter.TransactionAdapter;
import com.VirtualBankingSystem.TransactionService.Client.AccountServiceClient;
import com.VirtualBankingSystem.TransactionService.Constants.TransactionStatusEnum;
import com.VirtualBankingSystem.TransactionService.DTO.Request.AccountTransferRequestDTO;
import com.VirtualBankingSystem.TransactionService.DTO.Request.TransactionExecutionRequestDTO;
import com.VirtualBankingSystem.TransactionService.DTO.Request.TransactionInitiationRequestDTO;
import com.VirtualBankingSystem.TransactionService.DTO.Response.TransactionResponseDTO;
import com.VirtualBankingSystem.TransactionService.DTO.Response.UserTransactionsResponseDTO;
import com.VirtualBankingSystem.TransactionService.Entity.Transaction;
import com.VirtualBankingSystem.TransactionService.Exception.NotEnoughBalanceException;
import com.VirtualBankingSystem.TransactionService.Exception.TransactionNotFoundException;
import com.VirtualBankingSystem.TransactionService.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionStatusService transactionStatusService;

    @Autowired
    @Qualifier("accountServiceClient")
    private AccountServiceClient accountServiceClient;

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO initiateTransaction(TransactionInitiationRequestDTO request) {
        this.validateTransactionRequest(request);
        Transaction transaction = TransactionAdapter.convertTransactionInitiationRequestDTOToTransaction(request);
        transaction.setStatus(TransactionStatusEnum.INITIATED);
        transactionRepository.save(transaction);

        return TransactionAdapter.convertTransactionToTransactionResponseDTO(transaction);
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO executeTransaction(TransactionExecutionRequestDTO request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId()).orElse(null);

        if (transaction == null) {
            throw new TransactionNotFoundException("Transaction " + request.getTransactionId() + " not found");
        }
        if (transaction.getStatus() != TransactionStatusEnum.INITIATED) {
            throw new IllegalStateException("Transaction cannot be executed in its current state");
        }

        try {
            AccountTransferRequestDTO accountTransferRequestDTO =
                    TransactionAdapter.convertTransactionToAccountTransferRequestDTO(transaction);

            accountServiceClient.updateAccountBalance(accountTransferRequestDTO);
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transactionRepository.save(transaction);
            return TransactionAdapter.convertTransactionToTransactionResponseDTO(transaction);
        } catch (Exception e) {
            transactionStatusService.updateTransactionStatusToFailed(transaction);
            throw e;
        }
    }

    public List<UserTransactionsResponseDTO> getAccountTransactions(UUID accountId) {
        List<Transaction> accountTransactions = transactionRepository.findAllByAccountIdOrderByTimestampDesc(accountId);

        if (accountTransactions.isEmpty()) {
            return new ArrayList<>();
        }

        for (Transaction transaction : accountTransactions) {
            if (transaction.getFromAccountId().equals(accountId)) {
                transaction.setAmount(transaction.getAmount().multiply(BigDecimal.valueOf(-1)));
            }
        }

        UUID accountIdForResponse;
        List<UserTransactionsResponseDTO> userTransactions = new ArrayList<>(accountTransactions.size());
        for (Transaction transaction : accountTransactions) {
            accountIdForResponse = transaction.getFromAccountId().equals(accountId) ? transaction.getToAccountId() : transaction.getFromAccountId();
            UserTransactionsResponseDTO userTransaction = TransactionAdapter.convertTransactionToUserTransactionsResponseDTO(transaction, accountIdForResponse);
            userTransactions.add(userTransaction);
        }

        return userTransactions;
    }

    private void validateTransactionRequest(TransactionInitiationRequestDTO request) {
        if (request.getToAccountId().equals(request.getFromAccountId())) {
            throw new IllegalArgumentException("Sender and receiver accounts cannot be the same");
        }
        accountServiceClient.getUserAccounts(request.getToAccountId());
        BigDecimal balance = accountServiceClient.getUserAccounts(request.getFromAccountId()).getBalance();
        if (balance.compareTo(request.getAmount()) < 0) {
            throw new NotEnoughBalanceException("Insufficient balance in the sender's account");
        }
    }


}
