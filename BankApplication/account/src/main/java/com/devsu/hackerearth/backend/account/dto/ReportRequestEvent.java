package com.devsu.hackerearth.backend.account.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event class representing a request to generate a transaction report for a
 * specific client.
 * <p>
 * This event is published to Kafka and consumed asynchronously to initiate the
 * report generation workflow.
 * It contains the client's identifier, the target date range, and a correlation
 * ID to track the request lifecycle.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code clientId} – Unique identifier of the client whose transactions are
 * to be reported.</li>
 * <li>{@code startDate} – Start date of the transaction period
 * (inclusive).</li>
 * <li>{@code endDate} – End date of the transaction period (inclusive).</li>
 * <li>{@code correlationId} – Unique identifier used to match requests and
 * responses.</li>
 * </ul>
 *
 * Used in: Kafka producer to trigger report generation.
 * Consumed by: {@code ReportRequestConsumer}
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestEvent {

	private Long clientId;
	private LocalDate startDate;
	private LocalDate endDate;
	private String correlationId;
}
