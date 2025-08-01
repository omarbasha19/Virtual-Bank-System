package com.VirtualBankingSystem.TransactionService.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferRequestDTO {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
}
