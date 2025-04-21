package com.devsu.hackerearth.backend.account.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.dto.ReportRequestEvent;
import com.devsu.hackerearth.backend.account.dto.ReportResponseEvent;
import com.devsu.hackerearth.backend.account.service.ReportService;

import lombok.RequiredArgsConstructor;

/**
 * Kafka consumer component responsible for receiving {@link ReportRequestEvent} messages from the {@code report-requests} topic.
 * <p>
 * Delegates the report generation logic to the {@link ReportService}, which processes the request asynchronously
 * and responds with a {@link ReportResponseEvent}.
 * </p>
 *
 * Functional Behavior:
 * <ul>
 *     <li>Listens for report request events produced by upstream services.</li>
 *     <li>Delegates all processing logic to the {@link ReportService}.</li>
 * </ul>
 *
 * Kafka Configuration:
 * <ul>
 *     <li><strong>Topic:</strong> report-requests</li>
 *     <li><strong>Group ID:</strong> account-service</li>
 * </ul>
 *
 * Author: Germán Ponce  
 * Version: 1.0
 */
@Component
@RequiredArgsConstructor
public class ReportRequestConsumer {

    private final ReportService reportService;

    /**
     * Consumes incoming report request events and triggers asynchronous processing.
     *
     * @param request the event containing client ID, date range, and correlation ID
     */
    @KafkaListener(topics = "report-requests", groupId = "account-service")
    public void receiveReportRequest(ReportRequestEvent request) {
        reportService.processReportRequest(request);
    }
}
