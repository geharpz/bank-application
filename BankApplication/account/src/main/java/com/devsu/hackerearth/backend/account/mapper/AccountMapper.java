package com.devsu.hackerearth.backend.account.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.type.AccountType;

@Component
public class AccountMapper {

    public Account toEntity(AccountDto dto) {
        Account account = new Account();
        account.setType(dto.getType().name());
        account.setInitialAmount(dto.getInitialAmount());
        account.setCurrentBalance(dto.getInitialAmount());
        account.setClientId(dto.getClientId());
        account.setActive(true);
        return account;
    }

    public Account toUpdateEntity(Account account, BigDecimal currentBalance) {
        if (currentBalance != null)
            account.setCurrentBalance(currentBalance);
        return account;
    }

    public AccountDto toDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setType(Enum.valueOf(AccountType.class, account.getType()));
        dto.setInitialAmount(account.getInitialAmount());
        dto.setCurrentBalance(account.getCurrentBalance());
        dto.setClientId(account.getClientId());
        dto.setActive(account.isActive());
        return dto;
    }
}