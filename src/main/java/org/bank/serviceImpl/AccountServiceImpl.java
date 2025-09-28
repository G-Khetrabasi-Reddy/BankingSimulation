package org.bank.serviceImpl;

import org.bank.model.Account;
import org.bank.repository.AccountRepository;
import org.bank.repositoryImpl.AccountRepositoryImpl;
import org.bank.service.AccountService;

import java.util.regex.Pattern;

public class AccountServiceImpl implements AccountService {

    private  final AccountRepository repository = new AccountRepositoryImpl();

    private static final double MIN_VALUE = 0.0;

    @Override
    public boolean openAccount(Account account) {
        return false;
    }

    @Override
    public boolean closeAccount(long accountId) {
        return false;
    }

    @Override
    public boolean deposit(long accountId, double amount) {
        return false;
    }

    @Override
    public boolean withdraw(long accountId, double amount) {
        return false;
    }

    @Override
    public Account getAccountDetails(long accountId) {
        return null;
    }


    //Validation methods
    private boolean isValidAccount(Account account){
        return true;
    }

    private boolean isNotNull(Account account){
        return account != null;
    }

    private boolean isValidBalance(long balance){
        return balance >= 0.0;
    }

    private boolean isNotBlank(String str){
        return str != null && !str.trim().isEmpty();
    }
}
