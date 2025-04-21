package com.devsu.hackerearth.backend.account.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Serializable Data Transfer Object representing a summarized view of a single
 * financial transaction,
 * used within reporting structures.
 * <p>
 * This class is designed for compact transmission of transaction metadata,
 * excluding sensitive or redundant fields.
 * It is typically embedded within {@link AccountDataDto} as part of a
 * {@link ReportResponseEvent}.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code type} – Type of transaction (e.g., DEPOSIT, WITHDRAWAL).</li>
 * <li>{@code date} – Transaction date formatted as a string (e.g., ISO
 * 8601).</li>
 * <li>{@code amount} – Amount transacted, typically formatted for display.</li>
 * <li>{@code balanceAfterTransaction} – Resulting account balance after
 * applying the transaction.</li>
 * </ul>
 *
 * Implements {@link Serializable} to ensure compatibility across messaging and
 * distributed systems.
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDTO implements Serializable {

    private static final long serialVersionUID = -8830587005830242652L;

    private String type;
    private String date;
    private String amount;
    private String balanceAfterTransaction;
}