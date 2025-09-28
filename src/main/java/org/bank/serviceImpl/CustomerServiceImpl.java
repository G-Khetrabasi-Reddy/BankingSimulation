package org.bank.serviceImpl;

import org.bank.model.Customer;
import org.bank.repository.CustomerRepository;
import org.bank.repositoryImpl.CustomerRepositoryImpl;
import org.bank.service.CustomerService;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository = new CustomerRepositoryImpl();

    // Validation Regex
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z .]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern AADHAR_PATTERN = Pattern.compile("^[2-9][0-9]{11}$");

    @Override
    public Customer addCustomer(Customer customer) {
        validateCustomer(customer);
        try {
            return repository.addCustomer(customer);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    @Override
    public Customer findById(long customerId) {
        return repository.findById(customerId);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        validateCustomer(customer);
        try {
            return repository.updateCustomer(customer);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    @Override
    public boolean deleteCustomer(long customerId) {
        return repository.deleteCustomer(customerId);
    }

    //Validation methods
    private void validateCustomer(Customer customer) {
        if (!isValidName(customer.getName())) {
            throw new IllegalArgumentException("Invalid customer name: " + customer.getName());
        }
        if (!isValidPhone(customer.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number: " + customer.getPhoneNumber());
        }
        if (!isNotBlank(customer.getEmail())) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!isValidAadhar(customer.getAadharNumber())) {
            throw new IllegalArgumentException("Invalid Aadhar number: " + customer.getAadharNumber());
        }
        if (!isNotBlank(customer.getAddress())) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
    }

    private boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    private boolean isValidAadhar(String aadhar){
        return aadhar != null && AADHAR_PATTERN.matcher(aadhar).matches();
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
