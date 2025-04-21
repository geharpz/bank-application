package com.devsu.hackerearth.backend.account.mapper;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.dto.TransactionSummaryDTO;
import com.devsu.hackerearth.backend.account.model.Transaction;

@Component
public class TransactionSummaryMapper {

    public TransactionSummaryDTO toDto(Transaction transaction) {
        return new TransactionSummaryDTO(
            transaction.getType(),
            transaction.getDate().toString(),
            transaction.getAmount().toPlainString(),
            transaction.getBalance().toPlainString()
        );
    }
}