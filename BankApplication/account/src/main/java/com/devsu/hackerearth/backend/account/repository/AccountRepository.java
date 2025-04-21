package com.devsu.hackerearth.backend.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Account;

/**
 * Repository interface for managing {@link Account} entities with JPA.
 * <p>
 * Provides query methods for retrieving active accounts by ID, client
 * association, and account number.
 * Supports existence checks and listing of all active accounts in the system.
 * </p>
 *
 * Custom Queries:
 * <ul>
 * <li>{@code findActiveById} – Fetches an active account by its unique
 * identifier.</li>
 * <li>{@code findActiveByIdClient} – Retrieves all active accounts linked to a
 * specific client ID.</li>
 * <li>{@code findActiveByNumber} – Finds an active account using its account
 * number.</li>
 * <li>{@code findAllActiveAccountList} – Returns all accounts flagged as
 * active.</li>
 * <li>{@code existsByNumber} – Checks for existence of an account with the
 * specified number.</li>
 * </ul>
 * 
 * All queries assume soft deletion strategy via {@code isActive = true}.
 * 
 * Author: Germán Ponce
 * Version: 1.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an active account by its ID.
     *
     * @param id the account ID
     * @return an {@link Optional} containing the active account, if found
     */
    @Query("SELECT a FROM Account a WHERE a.id = :id AND a.isActive = true")
    Optional<Account> findActiveById(@Param("id") Long id);

    /**
     * Finds all active accounts associated with a specific client ID.
     *
     * @param clientId the ID of the client
     * @return an {@link Optional} containing a list of active accounts
     */
    @Query("SELECT a FROM Account a WHERE a.clientId = :clientId AND a.isActive = true")
    Optional<List<Account>> findActiveByIdClient(@Param("clientId") Long clientId);

    /**
     * Finds an active account by its account number.
     *
     * @param number the account number
     * @return an {@link Optional} containing the active account, if found
     */
    @Query("SELECT a FROM Account a WHERE a.number = :number AND a.isActive = true")
    Optional<Account> findActiveByNumber(@Param("number") String number);

    /**
     * Retrieves all accounts currently marked as active.
     *
     * @return a list of active accounts
     */
    @Query("SELECT a FROM Account a WHERE a.isActive = true")
    List<Account> findAllActiveAccountList();

    /**
     * Checks if an account exists by its number.
     *
     * @param number the account number
     * @return true if an account with the specified number exists, false otherwise
     */
    boolean existsByNumber(String number);

}
