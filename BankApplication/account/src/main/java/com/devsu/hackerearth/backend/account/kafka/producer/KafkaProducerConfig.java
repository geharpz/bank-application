package com.devsu.hackerearth.backend.account.kafka.producer;

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

import com.devsu.hackerearth.backend.account.dto.ReportRequestEvent;
import com.devsu.hackerearth.backend.account.dto.ReportResponseEvent;

/**
 * Kafka producer configuration class responsible for declaring and configuring
 * the {@link KafkaTemplate} beans used to publish messages to Kafka topics.
 * <p>
 * This class centralizes the creation of producers for different message types,
 * ensuring consistent serialization and broker connection settings across the
 * application. It supports both {@link ReportRequestEvent} and
 * {@link ReportResponseEvent} producers.
 * </p>
 *
 * Configuration Features:
 * <ul>
 * <li>Uses {@link KafkaProperties} to inject bootstrap server settings.</li>
 * <li>Defines reusable generic producer factory method for consistent
 * serialization.</li>
 * <li>Registers distinct {@link KafkaTemplate} beans for each event type.</li>
 * </ul>
 *
 * Beans:
 * <ul>
 * <li>{@code kafkaTemplate} – for sending {@link ReportResponseEvent}.</li>
 * <li>{@code kafkaTemplateRequest} – for sending
 * {@link ReportRequestEvent}.</li>
 * </ul>
 *
 * Author: Germán Ponce Version: 1.0
 */
@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Builds the Kafka {@link ProducerFactory} with JSON serialization for
     * {@link ReportResponseEvent}.
     *
     * @return configured producer factory for report responses
     */
    @Bean
    public ProducerFactory<String, ReportResponseEvent> producerFactoryReponse() {
        return producerFactory();
    }

    /**
     * Exposes a {@link KafkaTemplate} bean used to send {@link ReportResponseEvent}
     * messages.
     *
     * @return a configured KafkaTemplate instance for report responses
     */
    @Bean
    public KafkaTemplate<String, ReportResponseEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryReponse());
    }

    /**
     * Builds the Kafka {@link ProducerFactory} for {@link ReportRequestEvent}
     * messages.
     *
     * @return configured producer factory for report requests
     */
    @Bean
    public ProducerFactory<String, ReportRequestEvent> producerFactoryRequest() {
        return producerFactory();
    }

    /**
     * Exposes a {@link KafkaTemplate} bean used to send {@link ReportRequestEvent}
     * messages.
     *
     * @return a configured KafkaTemplate instance for report requests
     */
    @Bean
    public KafkaTemplate<String, ReportRequestEvent> kafkaTemplateRequest() {
        return new KafkaTemplate<>(producerFactoryRequest());
    }

    /**
     * Generic factory method for producing {@link KafkaTemplate} instances with
     * shared configuration for serialization and broker communication.
     *
     * @param <R> the type of the value being sent to Kafka
     * @return a configured producer factory for the given value type
     */
    public <R> ProducerFactory<String, R> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
