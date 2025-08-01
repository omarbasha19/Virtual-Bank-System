package com.VirtualBankingSystem.TransactionService.Service;

import com.VirtualBankingSystem.TransactionService.Constants.TransactionStatusEnum;
import com.VirtualBankingSystem.TransactionService.Entity.Transaction;
import com.VirtualBankingSystem.TransactionService.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionStatusService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTransactionStatusToFailed(Transaction transaction) {
        transaction.setStatus(TransactionStatusEnum.FAILED);
        transactionRepository.save(transaction);
    }
}
