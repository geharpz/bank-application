package com.devsu.hackerearth.backend.account.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.dto.ReportResponseEvent;

import lombok.RequiredArgsConstructor;

/**
 * Kafka producer service responsible for sending {@link ReportResponseEvent}
 * messages
 * to the {@code report-responses} topic.
 * <p>
 * This component encapsulates the publishing logic, allowing other services to
 * remain decoupled
 * from Kafka-specific implementation details.
 * </p>
 *
 * Responsibilities:
 * <ul>
 * <li>Send structured report response messages keyed by correlation ID.</li>
 * <li>Ensure traceability and partitioning via correlation-based message
 * keys.</li>
 * </ul>
 *
 * Topic Configuration:
 * <ul>
 * <li><strong>Target Topic:</strong> report-responses</li>
 * <li><strong>Message Key:</strong> correlationId</li>
 * </ul>
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class ReportResponseProducer {

    private final KafkaTemplate<String, ReportResponseEvent> kafkaTemplate;

    /**
     * Publishes a report response event to Kafka using the correlation ID as the
     * message key.
     *
     * @param response the enriched report response to send
     */
    public void sendReportResponse(ReportResponseEvent response) {
        kafkaTemplate.send("report-responses", response.getCorrelationId(), response);
    }
}