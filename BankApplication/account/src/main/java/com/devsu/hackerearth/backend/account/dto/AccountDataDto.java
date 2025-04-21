package com.devsu.hackerearth.backend.account.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data structure representing the state and transaction summary of a specific
 * account,
 * used primarily in report responses sent to clients.
 * <p>
 * This class is a flattened, immutable projection of an {@code Account} entity
 * intended for external consumption.
 * It includes basic account metadata and a summarized list of transactions
 * relevant to a reporting period.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code number} – Unique account number.</li>
 * <li>{@code type} – Account type (e.g., SAVINGS, CHECKING).</li>
 * <li>{@code initialAmount} – Starting balance when the account was
 * created.</li>
 * <li>{@code currentBalance} – Actual balance after all transactions.</li>
 * <li>{@code transactions} – Summarized list of transactions for the report
 * period.</li>
 * </ul>
 *
 * Used in: {@link ReportResponseEvent}
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDataDto {

    private String number;

    private String type;

    private BigDecimal initialAmount;

    private BigDecimal currentBalance;

    private List<TransactionSummaryDTO> transactions;
}
