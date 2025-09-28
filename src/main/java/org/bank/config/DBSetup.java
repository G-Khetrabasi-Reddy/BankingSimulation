package org.bank.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSetup {

    public static void createTables() {
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // ---------------- Customers Table ----------------
            String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customerId BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    phoneNumber VARCHAR(15) NOT NULL,
                    email VARCHAR(100),
                    address VARCHAR(255),
                    customerPin VARCHAR(10) NOT NULL,
                    aadharNumber VARCHAR(20) UNIQUE NOT NULL,
                    dob DATE NOT NULL,
                    status VARCHAR(20) NOT NULL
                );
                """;

            stmt.executeUpdate(createCustomersTable);


            // ---------------- Accounts Table ----------------
            String createAccountsTable = """
                CREATE TABLE IF NOT EXISTS accounts (
                    accountId BIGINT PRIMARY KEY AUTO_INCREMENT,
                    customerId BIGINT NOT NULL,
                    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    modifiedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    balance DECIMAL(15,2) NOT NULL DEFAULT 0.0,
                    accountType VARCHAR(50) NOT NULL,
                    accountName VARCHAR(100),
                    accountNumber VARCHAR(30) UNIQUE NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    FOREIGN KEY (customerId) REFERENCES customers(customerId)
                );
                """;
            stmt.executeUpdate(createAccountsTable);

            // ---------------- Transactions Table ----------------
            String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    transactionId BIGINT PRIMARY KEY AUTO_INCREMENT,
                    accountId BIGINT NOT NULL,
                    amount DECIMAL(15,2) NOT NULL,
                    transactionType VARCHAR(20) NOT NULL, -- debited / credited
                    transactionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    transactionMode VARCHAR(30), -- debit, upi, credit card
                    receiver_account_id BIGINT,
                    sender_account_id BIGINT,
                    FOREIGN KEY (accountId) REFERENCES accounts(accountId),
                    FOREIGN KEY (receiver_account_id) REFERENCES accounts(accountId),
                    FOREIGN KEY (sender_account_id) REFERENCES accounts(accountId)
                );
                """;
            stmt.executeUpdate(createTransactionsTable);

            System.out.println("✅ Database setup complete: customers, accounts, transactions tables are ready.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to set up database: " + e.getMessage());
        }
    }

    public static void setCustomerAutoIncrement() {
        String setAutoIncrement = "ALTER TABLE customers AUTO_INCREMENT = 100001;";

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(setAutoIncrement);
            System.out.println("✅ Customer AUTO_INCREMENT set to 100001");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to set AUTO_INCREMENT: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        createTables();
        setCustomerAutoIncrement();
    }
}
