package com.devsu.hackerearth.backend.account.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.type.TransactionType;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setType(Enum.valueOf(TransactionType.class, transaction.getType()));
        dto.setAmount(transaction.getAmount());
        dto.setBalance(transaction.getBalance());
        dto.setDate(transaction.getDate());
        dto.setAccountId(transaction.getAccount().getId());
        return dto;
    }

    public Transaction toEntity(TransactionDto dto, Account account, BigDecimal newBalance) {
        Transaction transaction = new Transaction();
        transaction.setType(dto.getType().name());
        transaction.setAmount(dto.getAmount());
        transaction.setBalance(newBalance);
        transaction.setDate(LocalDate.now());
        transaction.setAccount(account);
        return transaction;
    }
}
