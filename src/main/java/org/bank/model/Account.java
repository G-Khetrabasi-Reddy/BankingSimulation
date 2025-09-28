package org.bank.model;

import java.time.LocalDateTime;

public record Account(
        long accountId,
        long customerId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        double balance,
        String accountType,
        String accountName,
        String accountNumber,
        String status
) {
    public Account withBalance(double newBalance) {
        return new Account(accountId, customerId, createdAt, LocalDateTime.now(), newBalance, accountType, accountName, accountNumber, status);
    }

    public Account withStatus(String newStatus) {
        return new Account(accountId, customerId, createdAt, LocalDateTime.now(), balance,accountType, accountName, accountNumber, newStatus);
    }
}
