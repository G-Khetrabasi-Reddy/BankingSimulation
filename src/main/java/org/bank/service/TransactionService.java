package org.bank.service;

import org.bank.model.Transaction;

import java.util.List;

public interface TransactionService {
    boolean makeTransaction(Transaction transaction); // Credit/Debit
    Transaction findById(long transactionId);         // Find transaction
    List<Transaction> findByAccountId(long accountId);// Transactions for account
    List<Transaction> findAll();                      // All transactions
}