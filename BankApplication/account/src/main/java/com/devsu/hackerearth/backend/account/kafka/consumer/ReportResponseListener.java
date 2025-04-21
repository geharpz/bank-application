package com.devsu.hackerearth.backend.account.kafka.consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.dto.ReportResponseEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Kafka listener component responsible for receiving enriched
 * {@link ReportResponseEvent} messages
 * and storing them in memory for synchronous retrieval via correlation ID.
 * <p>
 * This class enables temporary, in-memory caching of report responses, acting
 * as a bridge between
 * asynchronous processing and synchronous client polling.
 * </p>
 *
 * Responsibilities:
 * <ul>
 * <li>Consume enriched report events from Kafka.</li>
 * <li>Store reports in a thread-safe structure keyed by correlation ID.</li>
 * <li>Provide lookup and removal capabilities for stored reports.</li>
 * </ul>
 *
 * Kafka Configuration:
 * <ul>
 * <li><strong>Topic:</strong> report-responses-enriched</li>
 * <li><strong>Group ID:</strong> account-service</li>
 * <li><strong>Container Factory:</strong>
 * kafkaListenerContainerFactoryEnriched</li>
 * </ul>
 *
 * ⚠ This implementation is suitable for short-lived, non-persistent use cases
 * only.
 * In production scenarios, consider integrating a persistent cache or database
 * for durability.
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Slf4j
@Component
public class ReportResponseListener {

    private final Map<String, ReportResponseEvent> responseStore = new ConcurrentHashMap<>();

    /**
     * Kafka listener that receives enriched report responses and stores them by
     * correlation ID.
     *
     * @param recordResponse Kafka record containing the correlation key and report
     *                       payload
     */
    @KafkaListener(topics = "report-responses-enriched", groupId = "account-service", containerFactory = "kafkaListenerContainerFactoryEnriched")
    public void listen(ConsumerRecord<String, ReportResponseEvent> recordResponse) {
        String correlationId = recordResponse.key();
        ReportResponseEvent response = recordResponse.value();
        responseStore.put(correlationId, response);
        log.info("[Kafka] Report enriched received, correlationId: {}", correlationId);
    }

    /**
     * Retrieves a previously stored report by its correlation ID.
     *
     * @param correlationId the identifier of the report request
     * @return the associated {@link ReportResponseEvent}, or null if not found
     */
    public ReportResponseEvent getReportByCorrelationId(String correlationId) {
        return responseStore.get(correlationId);
    }

    /**
     * Clears a cached report from memory by correlation ID.
     *
     * @param correlationId the identifier to remove
     */
    public void clear(String correlationId) {
        responseStore.remove(correlationId);
    }
}