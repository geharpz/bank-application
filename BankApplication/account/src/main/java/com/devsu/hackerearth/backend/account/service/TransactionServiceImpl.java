package com.devsu.hackerearth.backend.account.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.dto.AccountDataDto;
import com.devsu.hackerearth.backend.account.exception.AccountNotFoundException;
import com.devsu.hackerearth.backend.account.exception.InsufficientBalanceException;
import com.devsu.hackerearth.backend.account.exception.PersistenceOperationException;
import com.devsu.hackerearth.backend.account.exception.TransactionNotFoundException;
import com.devsu.hackerearth.backend.account.mapper.AccountDataMapper;
import com.devsu.hackerearth.backend.account.mapper.TransactionMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;
import com.devsu.hackerearth.backend.account.type.TransactionType;

import javax.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private static final String TRANSACTION = "Transaction";
    private static final String ACCOUNT = "Account";
    private static final String CREATE = "Create";
    private static final String UPDATE = "Update";
    private static final String RETRIEVE = "retrieve";

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final AccountDataMapper accountDataMapper;

    @Override
    public List<TransactionDto> getAll() {
        try {
            return transactionRepository.findAll().stream()
                    .map(transactionMapper::toDto).collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, TRANSACTION, e);
        }
    }

    @Override
    public TransactionDto getById(Long id) {
        try {
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(id));
            return transactionMapper.toDto(transaction);
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, ACCOUNT, e);
        }
    }

    /**
     * Registers a new financial transaction, updating the associated account's
     * balance.
     * Ensures that deposits and withdrawals follow business rules.
     *
     * @param dto the transaction request payload
     * @return the registered transaction as a DTO
     * @throws IllegalArgumentException      for invalid amounts or transaction
     *                                       types
     * @throws InsufficientBalanceException  if withdrawal results in a negative
     *                                       balance
     * @throws PersistenceOperationException if persistence fails at any step
     */
    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        try {
            Account account = getActiveAccountOrThrow(transactionDto.getAccountId());

            validateTransactionAmount(transactionDto.getAmount());

            BigDecimal newBalance = calculateNewBalance(
                    account.getCurrentBalance(),
                    transactionDto.getType(),
                    transactionDto.getAmount(),
                    account.getNumber());

            updateAccountBalance(account, newBalance);

            Transaction transaction = transactionMapper.toEntity(transactionDto, account, newBalance);

            return saveTransaction(transaction);
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(CREATE, TRANSACTION, e);
        }
    }

    /**
     * Retrieves all transactions for a client's accounts within the given date
     * range.
     *
     * @param clientId the client's ID
     * @param start    the start date (inclusive)
     * @param end      the end date (inclusive)
     * @return list of transactions within the specified period
     * @throws PersistenceOperationException if retrieval fails
     */
    @Override
    public List<AccountDataDto> getAllByAccountClientIdAndDateBetween(Long clientId, LocalDate dateTransactionStart,
            LocalDate dateTransactionEnd) {
        try {
            List<Account> accountList = accountRepository.findActiveByIdClient(clientId).orElse(null);
            List<AccountDataDto> accountDataList = new ArrayList<>(1);

            for (Account account : accountList) {
                List<Transaction> transactionList = transactionRepository.findByClientIdAndTransactionDateRange(
                        account.getId(), dateTransactionStart, dateTransactionEnd);

                accountDataList.add(accountDataMapper.toData(account, transactionList));
            }
            return accountDataList;
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, TRANSACTION, e);
        }
    }

    /**
     * Retrieves the last transaction associated with a specific account.
     *
     * @param accountId the account ID
     * @return list of transactions linked to the account
     * @throws PersistenceOperationException if retrieval fails
     */
    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        try {
            Transaction transaction = transactionRepository.findLastByAccountId(accountId)
                    .orElseThrow(() -> new TransactionNotFoundException(accountId));
            return transactionMapper.toDto(transaction);
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, TRANSACTION, e);
        }
    }

    /**
     * Retrieves an active account by ID or throws an exception if not found or
     * inactive.
     *
     * @param accountId the account ID
     * @return the active {@link Account} entity
     * @throws AccountNotFoundException      if the account is not found or inactive
     * @throws PersistenceOperationException if the lookup fails
     */
    private Account getActiveAccountOrThrow(Long accountId) {
        try {
            return accountRepository.findActiveById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(accountId));
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(RETRIEVE, TRANSACTION, e);
        }
    }

    /**
     * Validates that the transaction amount is greater than zero.
     *
     * @param amount the transaction amount
     * @throws IllegalArgumentException if the amount is null or non-positive
     */
    private void validateTransactionAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
    }

    /**
     * Calculates the new account balance after applying a transaction.
     * Enforces that the balance must remain non-negative.
     *
     * @param currentBalance the current balance of the account
     * @param type           the transaction type (DEPOSIT or WITHDRAWAL)
     * @param amount         the transaction amount
     * @param accountNumber  the account identifier (for exception context)
     * @return the resulting balance after the transaction
     * @throws InsufficientBalanceException if the result would be negative
     */
    private BigDecimal calculateNewBalance(BigDecimal currentBalance, TransactionType type, BigDecimal amount,
            String accountNumber) {
        BigDecimal signedAmount;
        switch (type) {
            case DEPOSIT:
                signedAmount = amount;
                break;
            case WITHDRAWAL:
                signedAmount = amount.negate();
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        BigDecimal newBalance = currentBalance.add(signedAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(accountNumber);
        }

        return newBalance;
    }

    /**
     * Updates the account's balance with the new calculated amount and persists the
     * change.
     *
     * @param account    the account to update
     * @param newBalance the new balance to be saved
     * @throws PersistenceOperationException if the update fails
     */
    private void updateAccountBalance(Account account, BigDecimal newBalance) {
        try {
            account.setCurrentBalance(newBalance);
            accountRepository.save(account);
        } catch (PersistenceException e) {
            throw new PersistenceOperationException(UPDATE, ACCOUNT, e);
        }
    }

    /**
     * Persists the transaction and returns it as a DTO.
     *
     * @param transaction the transaction to be saved
     * @return the saved transaction as a DTO
     * @throws PersistenceOperationException if the save operation fails
     */
    private TransactionDto saveTransaction(Transaction transaction) {
        try {
            return transactionMapper.toDto(transactionRepository.save(transaction));
        } catch (DataAccessException | PersistenceException e) {
            throw new PersistenceOperationException(CREATE, TRANSACTION, e);
        }
    }

}
