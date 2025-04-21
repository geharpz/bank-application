package com.devsu.hackerearth.backend.account.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.devsu.hackerearth.backend.account.dto.AccountDataDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

public interface TransactionService {

    public List<TransactionDto> getAll();

    public TransactionDto getById(Long id);

    public TransactionDto create(TransactionDto transactionDto);

    public List<AccountDataDto> getAllByAccountClientIdAndDateBetween(Long clientId,
            @Param("dateTransactionStart") LocalDate dateTransactionStart,
            @Param("dateTransactionEnd") LocalDate dateTransactionEnd);

    public TransactionDto getLastByAccountId(Long accountId);
}
