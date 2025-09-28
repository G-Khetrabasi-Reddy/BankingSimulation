package org.bank.repository;

import org.bank.model.Transaction;

import java.util.List;

public interface TransactionRepository {
    boolean addTransaction(Transaction transaction);
    Transaction findById(long transaction);
    List<Transaction> findByAccountId(long accountId);
    List<Transaction> findAll();
}

