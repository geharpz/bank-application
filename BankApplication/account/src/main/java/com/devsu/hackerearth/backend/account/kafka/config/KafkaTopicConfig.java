package com.devsu.hackerearth.backend.account.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka topic configuration class responsible for declaring application-level
 * topics related to reporting workflows.
 * <p>
 * Ensures that necessary Kafka topics are automatically created at application
 * startup, allowing
 * seamless communication between producers and consumers for report request and
 * response events.
 * </p>
 *
 * Topics Defined:
 * <ul>
 * <li><strong>report-requests</strong> – Topic for sending report generation
 * requests.</li>
 * <li><strong>report-responses</strong> – Topic for receiving basic report
 * responses.</li>
 * <li><strong>report-responses-enriched</strong> – Topic for consuming enriched
 * report data.</li>
 * </ul>
 *
 * All topics are configured with:
 * <ul>
 * <li>Partitions: 1</li>
 * <li>Replication Factor: 1 (suitable for local or single-broker
 * environments)</li>
 * </ul>
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic reportRequestsTopic() {
        return new NewTopic("report-requests", 1, (short) 1);
    }

    @Bean
    public NewTopic reportResponsesTopic() {
        return new NewTopic("report-responses", 1, (short) 1);
    }

    @Bean
    public NewTopic reportResponsesEnrichedTopic() {
        return new NewTopic("report-responses-enriched", 1, (short) 1);
    }
}