package com.devsu.hackerearth.backend.account.factory;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.mapper.AccountMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.util.AccountNumberGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountFactoryService {

    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public Account prepareAccount(AccountDto dto) {
        Account account = accountMapper.toEntity(dto);
        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.existsByNumber(accountNumber));

        account.setNumber(accountNumber);
        return account;
    }
}
