
package com.infosys.springboard.banksimulation.repositoryImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.infosys.springboard.banksimulation.config.DBConfig;
import com.infosys.springboard.banksimulation.model.Customer;
import com.infosys.springboard.banksimulation.repository.CustomerRepository;

public class CustomerRepositoryImpl implements CustomerRepository{

    @Override
    public boolean addCustomer(Customer customer){

        String sql = "INSERT INTO customers (customer_id, name, phone_nuumber, email, address, customer_pin, aadhar_number, dob, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customer.customerId());
            stmt.setString(2, customer.name());
            stmt.setString(3, customer.phoneNumber());
            stmt.setString(4, customer.email());
            stmt.setString(5, customer.address());
            stmt.setString(6, customer.customerPin());
            stmt.setString(7, customer.aadharNumber());
            stmt.setString(8, customer.dob());
            stmt.setString(9, customer.status());

            return stmt.executeUpdate() > 0;
            //Used for INSERT, UPDATE, DELETE queries (anything that changes the DB).
            //Returns an int → number of rows affected.
        } catch (SQLException e) {
            e.printStackTrace(); //Later: log properly
            return false;
        }
    }

    @Override
    public Customer findById(long customerId){

        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DBConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery(); //Used for SELECT statements.
            if(rs.next()){
                return new Customer(
                    rs.getLong("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("customer_pin"),
                    rs.getString("aadhar_number"),
                    rs.getString("dob"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; //not found
    }

    @Override
    public List<Customer> findAll(){
        String sql = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
           
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getLong("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("customer_pin"),
                    rs.getString("aadhar_number"),
                    rs.getString("dob"),
                    rs.getString("status")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public boolean updateCustomer(Customer customer){
        String sql = "UPDATE customers SET name=?, phone_number=?, email=?, address=?, customer_pin=?, aadhar_number=?, dob=?, status=?, WHERE customer_id=?";        
        try (Connection conn = DBConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, customer.name());
                stmt.setString(2, customer.phoneNumber());
                stmt.setString(3, customer.email());
                stmt.setString(4, customer.address());
                stmt.setString(5, customer.customerPin());
                stmt.setString(6, customer.aadharNumber());
                stmt.setString(7, customer.dob());
                stmt.setString(8, customer.status());
                stmt.setLong(9, customer.customerId());
                return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(long customerId){
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DBConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
