package com.devsu.hackerearth.backend.account.dto;

import lombok.Data;

/**
 * Data structure representing the reporting period boundaries used in a
 * transaction report.
 * <p>
 * Encapsulates the start and end dates as formatted strings (typically ISO
 * 8601) to clearly define
 * the temporal scope of the report.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code from} – Start date of the reporting range (inclusive).</li>
 * <li>{@code to} – End date of the reporting range (inclusive).</li>
 * </ul>
 *
 * Used in: {@link ReportResponseEvent} to contextualize transaction data.
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Data
public class ReportPeriod {

	private String from;

	private String to;
}
