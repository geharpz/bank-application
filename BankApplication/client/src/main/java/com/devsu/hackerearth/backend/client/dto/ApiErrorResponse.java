package com.devsu.hackerearth.backend.client.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Standardized structure for representing API error responses returned to
 * clients.
 * <p>
 * This class encapsulates metadata related to the HTTP error, including
 * timestamps, status codes,
 * error identifiers, human-readable messages, and the request path where the
 * error occurred.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code code} – Application-level or business error code (e.g.,
 * "ERR-001").</li>
 * <li>{@code timestamp} – Time the error occurred (in system local time).</li>
 * <li>{@code status} – HTTP status code (e.g., 404, 500).</li>
 * <li>{@code error} – Short HTTP status phrase (e.g., "Not Found", "Internal
 * Server Error").</li>
 * <li>{@code message} – Detailed description of the error.</li>
 * <li>{@code path} – URI of the endpoint where the error originated.</li>
 * </ul>
 *
 * Used for: Global exception handling and structured client feedback.
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {

    private String code;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}