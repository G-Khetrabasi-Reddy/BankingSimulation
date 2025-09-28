package org.bank.repository;

import org.bank.model.Customer;

import java.util.List;


public interface CustomerRepository {
    Customer addCustomer(Customer customer);
    Customer findById(long customerId);
    List<Customer> findAll();
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(long customerId);
}