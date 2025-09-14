package com.infosys.springboard.banksimulation.repository;

import java.util.List;
import com.infosys.springboard.banksimulation.model.Customer;

public interface CustomerRepository {
    boolean addCustomer(Customer customer);
    Customer findById(long customerId);
    List<Customer> findAll();
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(long customerId);
}
