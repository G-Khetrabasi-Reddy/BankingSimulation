package com.infosys.springboard.banksimulation.repository;

import java.util.List;
import java.util.Optional;

import com.infosys.springboard.banksimulation.model.Account;

public interface AccountRepository {
    boolean addAccount(Account account);
    Optional<Account> findById(long accountId);
    List<Account> findAll();
    boolean updateAccount(Account account);
    boolean deleteAccount(long accountId);
    
}
