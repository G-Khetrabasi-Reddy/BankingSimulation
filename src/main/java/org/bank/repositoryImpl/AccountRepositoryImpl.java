package org.bank.repositoryImpl;

import org.bank.config.DBConfig;
import org.bank.model.Account;
import org.bank.repository.AccountRepository;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public boolean addAccount(Account account) {
        String sql = "INSERT INTO account (account_id, customer_id, created_at, modified_at, balance, account_type, account_name, account_number, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, account.accountId());
            stmt.setLong(2, account.customerId());
            stmt.setTimestamp(3, Timestamp.valueOf(account.createdAt()));
            stmt.setTimestamp(4, Timestamp.valueOf(account.modifiedAt()));
            stmt.setDouble(5, account.balance());
            stmt.setString(6, account.accountType());
            stmt.setString(7, account.accountName());
            stmt.setString(8, account.accountNumber());
            stmt.setString(9, account.status());

            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Account findById(long accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return mapToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM account";
        List<Account> accounts = new ArrayList<>();

        try(Connection conn = DBConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(mapToAccount(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public boolean updateAccount(Account account) {
        String sql = "UPDATE account SET modified_at = ?, balance = ?, account_type = ?, account_name = ?, account_number = ?, status = ? WHERE account_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(account.modifiedAt()));
            stmt.setDouble(2, account.balance());
            stmt.setString(3, account.accountType());
            stmt.setString(4, account.accountName());
            stmt.setString(5, account.accountNumber());
            stmt.setString(6, account.status());
            stmt.setLong(7, account.accountId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAccount(long accountId) {
        String sql = "DELETE FROM account WHERE account_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Account mapToAccount(ResultSet rs)throws SQLException{
        return new Account(
                rs.getLong("account_id"),
                rs.getLong("customer_id"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("modified_at").toLocalDateTime(),
                rs.getDouble("balance"),
                rs.getString("account_type"),
                rs.getString("account_name"),
                rs.getString("account_number"),
                rs.getString("status")
        );
    }

}
