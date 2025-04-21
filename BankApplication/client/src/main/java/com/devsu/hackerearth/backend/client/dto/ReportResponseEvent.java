package com.devsu.hackerearth.backend.client.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event class representing the completed response to a report generation
 * request.
 * <p>
 * This message is produced after processing a {@link ReportRequestEvent} and
 * contains
 * the aggregated client data, report period, and associated account summaries.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code client} – Basic client information used in the report.</li>
 * <li>{@code reportPeriod} – Time range for the transactions included in the
 * report.</li>
 * <li>{@code accounts} – List of accounts with transaction summaries for the
 * given period.</li>
 * <li>{@code correlationId} – Identifier used to match this response to its
 * originating request.</li>
 * </ul>
 *
 * Used in: Kafka messaging to deliver completed reports to consumers.
 * Produced by: {@code ReportService}
 * Consumed by: {@code ReportResponseListener}
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseEvent {

    private ClientData client;
    private ReportPeriod reportPeriod;
    private List<AccountData> accounts;
    private String correlationId;
}
