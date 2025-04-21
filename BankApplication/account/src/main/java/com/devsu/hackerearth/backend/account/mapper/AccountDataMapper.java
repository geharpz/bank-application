package com.devsu.hackerearth.backend.account.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.dto.AccountDataDto;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountDataMapper {

    private final TransactionSummaryMapper transactionSummaryMapper;

    public AccountDataDto toData(Account account, List<Transaction> transactionList) {
        AccountDataDto data = new AccountDataDto();
        data.setNumber(account.getNumber());
        data.setType(account.getType());
        data.setInitialAmount(account.getInitialAmount());
        data.setCurrentBalance(account.getCurrentBalance());
        if (transactionList != null && !transactionList.isEmpty()) {
            data.setTransactions(
                    transactionList.stream().map(transactionSummaryMapper::toDto).collect(Collectors.toList()));
        }
        return data;
    }
}
