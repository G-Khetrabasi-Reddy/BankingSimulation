package org.bank.service;

import org.bank.config.DBConfig;
import org.bank.exception.AccountNotFoundException;
import org.bank.exception.InsufficientBalanceException;
import org.bank.exception.InvalidTransactionException;
import org.bank.exception.TransactionFailedException;
import org.bank.model.Account;
import org.bank.model.Customer;
import org.bank.model.Transaction;
import org.bank.repository.AccountRepository;
import org.bank.repository.CustomerRepository;
import org.bank.repository.TransactionRepository;
import org.bank.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private AccountRepository accountRepo;
    private TransactionRepository transactionRepo;
    private CustomerRepository customerRepo;
    private NotificationService notificationService;
    private TransactionServiceImpl service;
    private Connection conn;

    @BeforeEach
    void setUp(){
        accountRepo = mock(AccountRepository.class);
        transactionRepo = mock(TransactionRepository.class);
        customerRepo = mock(CustomerRepository.class);
        notificationService = mock(NotificationService.class);

        service = new TransactionServiceImpl(accountRepo, transactionRepo,notificationService, customerRepo);
        conn = mock(Connection.class);
    }

    //Test Case 1 : Successful transfer
    @Test
    void testTransferMoney_Successful() throws SQLException{
        Account senderAccount = new Account();
        senderAccount.setAccountId(1L);
        senderAccount.setAccountNumber("ACC1001");
        senderAccount.setBalance(10000.0);
        senderAccount.setStatus("ACTIVE");
        senderAccount.setCustomerId(11L);

        Account receiverAccount = new Account();
        receiverAccount.setAccountId(2L);
        receiverAccount.setAccountNumber("ACC1002");
        receiverAccount.setBalance(5000.0);
        receiverAccount.setStatus("ACTIVE");
        receiverAccount.setCustomerId(22L);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(101L);
        transaction.setAmount(1000.0);
        transaction.setTransactionTime(LocalDateTime.now());

        Customer senderCustomer = new Customer();
        senderCustomer.setCustomerId(11L);
        senderCustomer.setName("Khetra");
        senderCustomer.setEmail("khetra@gmail.com");

        Customer receiverCustomer = new Customer();
        receiverCustomer.setCustomerId(22L);
        receiverCustomer.setName("Reddy");
        receiverCustomer.setEmail("reddy@gmail.com");

        try(MockedStatic<DBConfig> dbMock = Mockito.mockStatic(DBConfig.class)){
            dbMock.when(DBConfig::getConnection).thenReturn(conn);

            when(accountRepo.findByAccountNumber(conn, "ACC1001")).thenReturn(Optional.of(senderAccount));
            when(accountRepo.findByAccountNumber(conn, "ACC1002")).thenReturn(Optional.of(receiverAccount));
            when(transactionRepo.saveTransaction(eq(conn), any(Transaction.class))).thenReturn(Optional.of(transaction));
            when(customerRepo.findById(11L)).thenReturn(Optional.of(senderCustomer));
            when(customerRepo.findById(22L)).thenReturn(Optional.of(receiverCustomer));



            Optional<Transaction> result = service.transferMoney("ACC1001", "ACC1002", 1000.0, "Test Transfer", "ONLINE");

            assertTrue(result.isPresent());
            assertEquals(1000, result.get().getAmount());

            verify(accountRepo).updateBalance(conn, "ACC1001", 9000);
            verify(accountRepo).updateBalance(conn, "ACC1002", 6000);
            verify(transactionRepo).saveTransaction(eq(conn), any(Transaction.class));

            verify(customerRepo).findById(11L);
            verify(customerRepo).findById(22L);
            verify(notificationService).emailAlert(
                    eq("khetra@gmail.com"), eq("Khetra"),
                    eq("reddy@gmail.com"), eq("Reddy"),
                    eq("ACC1001"), eq("ACC1002"), eq(1000.0)
            );
        }
    }

    //Test case 2 : Invalid Account Numbers
    @Test
    void testTransferMoney_InvalidAccountNumbers(){
        assertThrows(InvalidTransactionException.class,
                () -> service.transferMoney(null, "ACC1002", 1000, "Test", "ONLINE"));

        assertThrows(InvalidTransactionException.class,
                () -> service.transferMoney("ACC1001", null, 1000,"Test", "ONLINE"));

        assertThrows(InvalidTransactionException.class,
                () -> service.transferMoney("ACC1001", "ACC1001", 1000,"Test", "ONLINE"));
    }

    // Test case 3: Negative or Zero amount
    @Test
    void testTransferMoney_InvalidAmount() {
        assertThrows(InvalidTransactionException.class,
                () -> service.transferMoney("ACC1001", "ACC1002", -500, "Test", "ONLINE"));
        assertThrows(InvalidTransactionException.class,
                () -> service.transferMoney("ACC1001", "ACC1002", 0, "Test", "ONLINE"));
    }

    //Test case 4: Account Not Fouund
    @Test
    void testTransferMoney_AccountNotFound() throws SQLException {
        try (MockedStatic<DBConfig> dbMock = Mockito.mockStatic(DBConfig.class)) {
            dbMock.when(DBConfig::getConnection).thenReturn(conn);
            when(accountRepo.findByAccountNumber(conn, "ACC1001")).thenReturn(Optional.empty());

            assertThrows(AccountNotFoundException.class,
                    () -> service.transferMoney("ACC1001", "ACC1002", 1000, "Test", "ONLINE"));
        }
    }

    //Test case 5: Insufficient balance
    @Test
    void testTransferMoney_InsufficientBalance() throws SQLException {
        Account sender = new Account();
        sender.setAccountId(1L);
        sender.setAccountNumber("ACC1001");
        sender.setBalance(200);
        sender.setStatus("ACTIVE");

        Account receiver = new Account();
        receiver.setAccountId(2L);
        receiver.setAccountNumber("ACC1002");
        receiver.setBalance(5000);
        receiver.setStatus("ACTIVE");

        try (MockedStatic<DBConfig> dbMock = Mockito.mockStatic(DBConfig.class)) {
            dbMock.when(DBConfig::getConnection).thenReturn(conn);
            when(accountRepo.findByAccountNumber(conn, "ACC1001")).thenReturn(Optional.of(sender));
            when(accountRepo.findByAccountNumber(conn, "ACC1002")).thenReturn(Optional.of(receiver));

            assertThrows(InsufficientBalanceException.class,
                    () -> service.transferMoney("ACC1001", "ACC1002", 1000, "Test", "ONLINE"));
        }
    }

    //Test case 6: sender account is INACTIVE
    @Test
    void testTransferMoney_InactiveSender() throws SQLException {
        Account sender = new Account();
        sender.setAccountId(1L);
        sender.setAccountNumber("ACC1001");
        sender.setBalance(10000);
        sender.setStatus("INACTIVE");

        Account receiver = new Account();
        receiver.setAccountId(2L);
        receiver.setAccountNumber("ACC1002");
        receiver.setBalance(5000);
        receiver.setStatus("ACTIVE");

        try (MockedStatic<DBConfig> dbMock = Mockito.mockStatic(DBConfig.class)) {
            dbMock.when(DBConfig::getConnection).thenReturn(conn);
            when(accountRepo.findByAccountNumber(conn, "ACC1001")).thenReturn(Optional.of(sender));
            when(accountRepo.findByAccountNumber(conn, "ACC1002")).thenReturn(Optional.of(receiver));

            assertThrows(InvalidTransactionException.class,
                    () -> service.transferMoney("ACC1001", "ACC1002", 1000, "Test", "ONLINE"));
        }
    }

    //Test case 7: SQLException and rollback
    @Test
    void testTransferMoney_SQLExceptionThrown() throws SQLException {

        try (MockedStatic<DBConfig> dbMock = mockStatic(DBConfig.class)) {
            dbMock.when(DBConfig::getConnection).thenReturn(conn);

            Account sender = new Account();
            sender.setAccountId(1L);
            sender.setAccountNumber("ACC1001");
            sender.setBalance(5000.0);
            sender.setStatus("ACTIVE");

            Account receiver = new Account();
            receiver.setAccountId(2L);
            receiver.setAccountNumber("ACC10002");
            receiver.setBalance(3000.0);
            receiver.setStatus("ACTIVE");

            when(accountRepo.findByAccountNumber(conn, "ACC1001")).thenReturn(Optional.of(sender));
            when(accountRepo.findByAccountNumber(conn, "ACC1002")).thenReturn(Optional.of(receiver));

            // Simulate SQLException when updating balance
            doThrow(new SQLException("Simulated DB failure"))
                    .when(accountRepo)
                    .updateBalance(eq(conn), eq("ACC1001"), anyDouble());

            // Test & Assert
            assertThrows(TransactionFailedException.class, () ->
                    service.transferMoney("ACC1001", "ACC1002", 1000.0, "Testing SQL failure", "ONLINE")
            );

            // Verify rollback was called
            verify(conn).rollback();

            // Verify connection is closed
            verify(conn).close();
        }
    }


    //Test case 8: getTransactionById
    @Test
    void testGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(123);
        when(transactionRepo.findByTransactionId(123)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = service.getTransactionById(123);
        assertTrue(result.isPresent());
        assertEquals(123, result.get().getTransactionId());
    }

    //Test case 9: getTransactionsByAccountNumber(
    @Test
    void testGetTransactionsByAccountNumber() {
        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountNumber("ACC1001");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);

        when(accountRepo.findByAccountNumber("ACC1001")).thenReturn(Optional.of(account));
        when(transactionRepo.findByAccountId(1L)).thenReturn(List.of(transaction));

        List<Transaction> result = service.getTransactionsByAccountNumber("ACC1001");
        assertEquals(1, result.size());
    }

    //Test case 10: getAllTransactions
    @Test
    void testGetAllTransactions() {
        when(transactionRepo.findAll()).thenReturn(List.of(new Transaction(), new Transaction()));
        List<Transaction> result = service.getAllTransactions();
        assertEquals(2, result.size());
    }

    //Test case 11: getAccountNumberById
    @Test
    void testGetAccountNumberById() {
        when(accountRepo.findAccountNumberById(1L)).thenReturn(Optional.of("ACC1001"));
        String result = service.getAccountNumberById(1L);
        assertEquals("ACC1001", result);
    }

    @Test
    void testGetAccountNumberById_NotFound() {
        when(accountRepo.findAccountNumberById(99L)).thenReturn(Optional.empty());
        String result = service.getAccountNumberById(99L);
        assertEquals("UNKNOWN", result);
    }
}