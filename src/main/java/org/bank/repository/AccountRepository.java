package org.bank.repository;

import org.bank.model.Account;

import java.util.List;

public interface AccountRepository {
    boolean addAccount(Account account);
    Account findById(long accountId);
    List<Account> findAll();
    boolean updateAccount(Account account);
    boolean deleteAccount(long accountId);

}
