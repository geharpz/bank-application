package com.devsu.hackerearth.backend.client.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka topic configuration class responsible for declaring application-level
 * topics.
 * <p>
 * This configuration ensures that required Kafka topics are automatically
 * created
 * on application startup if they do not already exist in the broker.
 * </p>
 * 
 * Topics:
 * <ul>
 * <li><strong>report-responses</strong> – Base topic for receiving report
 * response events.</li>
 * <li><strong>report-responses-enriched</strong> – Topic for enriched versions
 * of report responses, typically after processing.</li>
 * </ul>
 * 
 * Both topics are configured with:
 * <ul>
 * <li>Partitions: 1</li>
 * <li>Replication factor: 1 (suitable for local or non-HA environments)</li>
 * </ul>
 * 
 * Used by Kafka producers and consumers in reporting flows.
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic reportResponsesTopic() {
        return new NewTopic("report-responses", 1, (short) 1);
    }

    @Bean
    public NewTopic reportResponsesEnrichedTopic() {
        return new NewTopic("report-responses-enriched", 1, (short) 1);
    }
}