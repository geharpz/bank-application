package com.devsu.hackerearth.backend.client.kafka.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.devsu.hackerearth.backend.client.dto.ReportResponseEvent;
import com.devsu.hackerearth.backend.client.kafka.consumer.ReportResponseEnricher;

/**
 * Kafka producer configuration class for publishing {@link ReportResponseEvent}
 * messages.
 * <p>
 * This configuration defines the serialization strategy and connection
 * parameters
 * required to produce events to Kafka topics using {@link KafkaTemplate}.
 * </p>
 *
 * Producer Setup:
 * <ul>
 * <li><strong>Key serializer:</strong> {@link StringSerializer} – for textual
 * message keys.</li>
 * <li><strong>Value serializer:</strong> {@link JsonSerializer} – for
 * structured event payloads.</li>
 * <li><strong>Bootstrap servers:</strong> loaded from external
 * {@link KafkaProperties}.</li>
 * </ul>
 *
 * Beans:
 * <ul>
 * <li>{@code producerFactory} – provides Kafka producers with configured
 * serializers.</li>
 * <li>{@code kafkaTemplate} – high-level abstraction for sending messages.</li>
 * </ul>
 *
 * This configuration enables seamless integration with Kafka-based messaging
 * flows,
 * ensuring reliable and type-safe message publishing.
 * 
 * Used by: {@link ReportResponseEnricher}
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Configures the Kafka producer factory with string key and JSON value
     * serialization.
     *
     * @return a {@link ProducerFactory} for {@link ReportResponseEvent} messages
     */
    @Bean
    public ProducerFactory<String, ReportResponseEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Provides a {@link KafkaTemplate} bean used to send
     * {@link ReportResponseEvent} messages to Kafka.
     *
     * @return a configured KafkaTemplate instance
     */
    @Bean
    public KafkaTemplate<String, ReportResponseEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
