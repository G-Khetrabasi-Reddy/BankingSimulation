package com.infosys.springboard.banksimulation.model;

public record Customer(
    long customerId,
    String name,
    String phoneNumber,
    String email,
    String address,
    String customerPin,
    String aadharNumber,
    String dob,
    String status
) {
    public Customer withPhoneNumber(String newPhone) {
        return new Customer(customerId, name, newPhone, email, address, customerPin, aadharNumber, dob, status);
    }

    public Customer withEmail(String newEmail) {
        return new Customer(customerId, name, phoneNumber, newEmail, address, customerPin, aadharNumber, dob, status);
    }

    public Customer withAddress(String newAddress) {
        return new Customer(customerId, name, phoneNumber, email, newAddress, customerPin, aadharNumber, dob, status);
    }

    public Customer withStatus(String newStatus) {
        return new Customer(customerId, name, phoneNumber, email, address, customerPin, aadharNumber, dob, newStatus);
    }
}
