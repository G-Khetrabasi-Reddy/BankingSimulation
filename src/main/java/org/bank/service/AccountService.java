package org.bank.service;

import org.bank.model.Account;

public interface AccountService {
    boolean openAccount(Account account);     // Business action
    boolean closeAccount(long accountId);     // Business action
    boolean deposit(long accountId, double amount);
    boolean withdraw(long accountId, double amount);
    Account getAccountDetails(long accountId);
}
