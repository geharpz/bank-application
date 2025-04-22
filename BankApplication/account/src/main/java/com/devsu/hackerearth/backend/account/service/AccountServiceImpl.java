package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exception.AccountInactiveException;
import com.devsu.hackerearth.backend.account.exception.AccountNotFoundException;
import com.devsu.hackerearth.backend.account.exception.DataConflictException;
import com.devsu.hackerearth.backend.account.exception.PersistenceOperationException;
import com.devsu.hackerearth.backend.account.factory.AccountFactoryService;
import com.devsu.hackerearth.backend.account.mapper.AccountMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

import javax.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT = "Account";
    private static final String RETRIEVE = "retrieve";

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountFactoryService accountFactoryService;

    /**
     * Retrieves all accounts currently marked as active in the system.
     *
     * @return list of active account DTOs
     * @throws PersistenceOperationException if an error occurs during retrieval
     */
    @Override
    public List<AccountDto> getAll() {
        try {
            return accountRepository.findAllActiveAccountList().stream()
                    .map(accountMapper::toDto).collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, ACCOUNT, e);
        }
    }

    /**
     * Retrieves an active account by its unique identifier.
     *
     * @param id the account ID
     * @return the corresponding account DTO
     * @throws AccountNotFoundException      if no active account is found
     * @throws PersistenceOperationException on repository access failure
     */
    @Override
    public AccountDto getById(Long id) {
        try {
            Account account = accountRepository.findActiveById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));
            return accountMapper.toDto(account);
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, ACCOUNT, e);
        }
    }

    /**
     * Creates a new account by delegating initialization to
     * {@link AccountFactoryService},
     * then persists the result and returns it as a DTO.
     *
     * @param accountDto the account creation request data
     * @return the persisted account as a DTO
     * @throws DataConflictException         if the account number already exists
     * @throws PersistenceOperationException if the operation fails at the
     *                                       persistence layer
     */
    @Transactional
    @Override
    public AccountDto create(AccountDto accountDto) {
        try {
            Account account = accountFactoryService.prepareAccount(accountDto);
            return accountMapper.toDto(accountRepository.save(account));
        } catch (DataIntegrityViolationException e) {
            throw new DataConflictException(ACCOUNT, "Number");
        } catch (PersistenceException e) {
            throw new PersistenceOperationException("create", ACCOUNT, e);
        }
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new AccountNotFoundException(accountDto.getId()));

        if (!Boolean.TRUE.equals(account.isActive())) {
            throw new AccountInactiveException(accountDto.getId());
        }

        try {
            accountMapper.toUpdateEntity(account, accountDto.getCurrentBalance());
            return accountMapper.toDto(accountRepository.save(account));
        } catch (PersistenceException e) {
            throw new PersistenceOperationException("update", ACCOUNT, e);
        }
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        try {
            account.setActive(partialAccountDto.isActive());
            return accountMapper.toDto(accountRepository.save(account));
        } catch (PersistenceException e) {
            throw new PersistenceOperationException("update", ACCOUNT, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
    }

    /**
     * Retrieves an active account by its account number.
     *
     * @param number the account number
     * @return the corresponding account DTO
     * @throws AccountNotFoundException      if no active account matches the number
     * @throws PersistenceOperationException if an error occurs during lookup
     */
    public AccountDto getByNumber(String number) {
        try {
            Account account = accountRepository.findActiveByNumber(number)
                    .orElseThrow(() -> new AccountNotFoundException(number));
            return accountMapper.toDto(account);
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, "Account by Number", e);
        }
    }

}
