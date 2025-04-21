package com.devsu.hackerearth.backend.account.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lightweight, serializable representation of client information used in report
 * events.
 * <p>
 * This DTO encapsulates only the necessary client attributes required for
 * report generation,
 * enrichment, and transmission across Kafka topics. It is decoupled from the
 * persistence layer
 * and optimized for external data exchange.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code id} – Unique identifier of the client.</li>
 * <li>{@code name} – Full name of the client.</li>
 * <li>{@code dni} – National identification number.</li>
 * <li>{@code gender} – Client's gender.</li>
 * <li>{@code age} – Client's age.</li>
 * <li>{@code phone} – Contact phone number.</li>
 * <li>{@code address} – Residential or billing address.</li>
 * </ul>
 *
 * Implements: {@link Serializable} for compatibility with Kafka and distributed
 * systems.
 * 
 * Used in: {@link ReportResponseEvent}
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientData implements Serializable {

    private static final long serialVersionUID = 9202625690582405166L;

    private Long id;
    private String name;
    private String dni;
    private String gender;
    private Integer age;
    private String phone;
    private String address;
}