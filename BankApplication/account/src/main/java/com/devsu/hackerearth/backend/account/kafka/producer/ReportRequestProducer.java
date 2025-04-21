package com.devsu.hackerearth.backend.account.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.dto.ReportRequestEvent;

import lombok.RequiredArgsConstructor;

/**
 * Kafka producer service responsible for publishing {@link ReportRequestEvent}
 * messages
 * to the {@code report-requests} topic.
 * <p>
 * This component encapsulates the event emission logic for initiating the
 * report generation
 * process, decoupling the application layer from Kafka-specific
 * implementations.
 * </p>
 *
 * Responsibilities:
 * <ul>
 * <li>Send report request events to Kafka with a correlation ID as the message
 * key.</li>
 * <li>Abstract the use of {@link KafkaTemplate} to promote separation of
 * concerns.</li>
 * </ul>
 *
 * Topic Configuration:
 * <ul>
 * <li><strong>Topic:</strong> report-requests</li>
 * <li><strong>Key:</strong> correlationId – used for traceability and
 * partitioning.</li>
 * </ul>
 *
 * Used by: {@link com.bank.account.service.ReportService}
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class ReportRequestProducer {

    private final KafkaTemplate<String, ReportRequestEvent> kafkaTemplate;

    /**
     * Sends a {@link ReportRequestEvent} to the Kafka topic
     * {@code report-requests}, using
     * the event's correlation ID as the message key.
     *
     * @param event the event to be published
     */
    public void send(ReportRequestEvent event) {
        kafkaTemplate.send("report-requests", event.getCorrelationId(), event);
    }
}