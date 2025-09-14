package com.infosys.springboard.banksimulation.model;

import java.time.LocalDateTime;

public record Transaction(
    long transactionId,
    long accountId,
    double amount,
    String transactionType, // debited / credited
    LocalDateTime transactionTime,
    String transactionMode, // debit, upi, credit card
    String receiverDetails,
    String senderDetails
) {}
