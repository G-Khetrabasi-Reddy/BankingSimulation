package com.infosys.springboard.banksimulation.repository;

import java.util.List;
import com.infosys.springboard.banksimulation.model.Transaction;

public interface TransactionRepository {
    boolean addTransaction(Transaction transaction);
    Transaction findById(long transaction);
    List<Transaction> findByAccountId(long accountId);
    List<Transaction> findAll();
}
