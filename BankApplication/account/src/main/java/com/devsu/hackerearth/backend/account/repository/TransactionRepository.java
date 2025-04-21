package com.devsu.hackerearth.backend.account.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

/**
 * Repository interface for managing {@link Transaction} entities using Spring
 * Data JPA.
 * <p>
 * Provides access to transaction history by account and supports filtering by
 * date ranges.
 * All queries are optimized for financial reporting and account activity
 * tracing.
 * </p>
 *
 * Custom Methods:
 * <ul>
 * <li>{@code findByAccountId} – Retrieves all transactions associated with a
 * specific account.</li>
 * <li>{@code findByClientIdAndTransactionDateRange} – Fetches transactions for
 * an account within a given date range.</li>
 * </ul>
 * 
 * This repository is central to audit trails, balance calculations, and monthly
 * statement generation.
 * 
 * Author: Germán Ponce
 * Version: 1.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves all transactions associated with the specified account ID.
     *
     * @param accountId the ID of the account
     * @return a list of transactions linked to the account
     */
    @Query("SELECT t FROM Transaction t where t.account.id = :accountId ORDER BY t.date, t.id DESC")
    Optional<Transaction> findLastByAccountId(@Param("accountId") Long accountId);

    /**
     * Retrieves transactions for a given account within a specified date range.
     *
     * @param accountId the account ID
     * @param startDate the start of the transaction date range (inclusive)
     * @param endDate   the end of the transaction date range (inclusive)
     * @return a list of matching transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.date BETWEEN :dateTransactionStart AND :dateTransactionEnd")
    List<Transaction> findByClientIdAndTransactionDateRange(
            @Param("accountId") Long accountId,
            @Param("dateTransactionStart") LocalDate startDate,
            @Param("dateTransactionEnd") LocalDate endDate);
}