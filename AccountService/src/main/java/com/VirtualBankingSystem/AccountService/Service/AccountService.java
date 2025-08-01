package com.VirtualBankingSystem.AccountService.Service;

import com.VirtualBankingSystem.AccountService.Clients.UserServiceClient;
import com.VirtualBankingSystem.AccountService.Constants.AccountStatus;
import com.VirtualBankingSystem.AccountService.DTO.Response.AccountDetailsResponseDTO;
import com.VirtualBankingSystem.AccountService.Entity.Account;
import com.VirtualBankingSystem.AccountService.Exceptions.AccountNotFoundException;
import com.VirtualBankingSystem.AccountService.Exceptions.InsufficientBalanceException;
import com.VirtualBankingSystem.AccountService.Repository.AccountRepositoryI;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepositoryI accountRepository;

    @Autowired
    @Qualifier("userServiceClient")
    private UserServiceClient userServiceClient;

    public Account getAccountById(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found");
        }
        return account;
    }

    @Transactional
    public void createAccount(Account account) throws BadRequestException {
        userServiceClient.getUserProfile(account.getUserId());

        account.setAccountNumber(this.generateAccountNumber());
        account.setStatus(AccountStatus.ACTIVE);
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        accountRepository.save(account);
    }

    public List<AccountDetailsResponseDTO> getUserAccounts(UUID userId) throws BadRequestException {
        List<AccountDetailsResponseDTO> userAccounts = accountRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (userAccounts == null || userAccounts.isEmpty()) {
            return new ArrayList<>();
        }
        return userAccounts;
    }

    @Transactional
    public void transferBalance(UUID fromAccountId, UUID toAccountId, BigDecimal amount) throws BadRequestException {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new AccountNotFoundException("One or both accounts do not exist");
        }

        if (fromAccount.getBalance().doubleValue() < amount.doubleValue()) {
            throw new InsufficientBalanceException("Insufficient balance in the source account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        fromAccount.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        toAccount.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        fromAccount.setStatus(AccountStatus.ACTIVE);
        toAccount.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Scheduled(fixedRate = 60 * 60 * 1_000) // Runs every 1 hour
    protected void inactivateAccounts() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(1);
//        LocalDateTime cutoffDate = LocalDateTime.now().minusMinutes(1); // For testing purposes
        accountRepository.updateAccountsStatus(cutoffDate);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = UtilityService.generateRandom20DigitNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
