package org.bank.repositoryImpl;

import org.bank.config.DBConfig;
import org.bank.model.Transaction;
import org.bank.repository.TransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions " +
                "(transaction_id, account_id, amount, transaction_type, transaction_time, transaction_mode, receiver_details, sender_details) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, transaction.transactionId());
            stmt.setLong(2, transaction.accountId());
            stmt.setDouble(3, transaction.amount());
            stmt.setString(4, transaction.transactionType());
            stmt.setTimestamp(5, Timestamp.valueOf(transaction.transactionTime()));
            stmt.setString(6, transaction.transactionMode());
            stmt.setLong(7, transaction.receiverAccountId());
            stmt.setLong(8, transaction.senderAccountId());

            return stmt.executeUpdate() > 0;  // true if row inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Transaction findById(long transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTransaction(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // return null if not found
    }

    @Override
    public List<Transaction> findByAccountId(long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // 🔹 Helper method to convert ResultSet → Transaction object
    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getLong("transaction_id"),
                rs.getLong("account_id"),
                rs.getDouble("amount"),
                rs.getString("transaction_type"),
                rs.getTimestamp("transaction_time").toLocalDateTime(),
                rs.getString("transaction_mode"),
                rs.getLong("receiver_account_id"),
                rs.getLong("sender_account_id")
        );
    }
}

