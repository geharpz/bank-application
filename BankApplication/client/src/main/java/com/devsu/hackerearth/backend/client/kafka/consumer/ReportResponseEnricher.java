package com.devsu.hackerearth.backend.client.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.client.dto.ClientData;
import com.devsu.hackerearth.backend.client.dto.ReportResponseEvent;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

/**
 * Kafka consumer component responsible for enriching incoming
 * {@link ReportResponseEvent} messages
 * with full {@link Client} data before publishing them to a secondary topic.
 * <p>
 * This class listens to the {@code report-responses} topic, retrieves client
 * information from the database,
 * injects enriched client details into the event, and forwards the updated
 * message to the
 * {@code report-responses-enriched} topic.
 * </p>
 *
 * Functional flow:
 * <ol>
 * <li>Receives a {@link ReportResponseEvent} via Kafka listener.</li>
 * <li>Extracts client ID and loads full client data from the repository.</li>
 * <li>If the client exists, builds a {@link ClientData} object and attaches it
 * to the event.</li>
 * <li>Publishes the enriched event to Kafka using the original correlation ID
 * as key.</li>
 * </ol>
 *
 * This component plays a key role in decoupling enrichment logic from the
 * initial event source.
 * 
 * Kafka Configuration:
 * <ul>
 * <li><strong>Input topic:</strong> report-responses</li>
 * <li><strong>Output topic:</strong> report-responses-enriched</li>
 * <li><strong>Group ID:</strong> person-service</li>
 * <li><strong>Listener container factory:</strong>
 * kafkaListenerContainerFactory</li>
 * </ul>
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ReportResponseEnricher {

    private final KafkaTemplate<String, ReportResponseEvent> kafkaTemplate;
    private final ClientRepository clientRepository;

    /**
     * Kafka listener method that enriches an incoming {@link ReportResponseEvent}
     * with full client data and republishes it to the
     * {@code report-responses-enriched} topic.
     *
     * @param recordReponse the Kafka record containing the original response event
     */
    @KafkaListener(topics = "report-responses", groupId = "person-service", containerFactory = "kafkaListenerContainerFactory")
    public void enrichReportResponse(ConsumerRecord<String, ReportResponseEvent> recordReponse) {
        ReportResponseEvent response = recordReponse.value();

        clientRepository.findById(response.getClient().getId()).ifPresent(client -> {
            ClientData data = new ClientData(
                    client.getId(),
                    client.getName(),
                    client.getDni(),
                    client.getGender(),
                    client.getAge(),
                    client.getPhone(),
                    client.getAddress());
            response.setClient(data);
            kafkaTemplate.send("report-responses-enriched", response.getCorrelationId(), response);
        });
    }
}
