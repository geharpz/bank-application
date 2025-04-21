package com.devsu.hackerearth.backend.account.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


import com.devsu.hackerearth.backend.account.dto.ReportResponseEvent;


/**
 * Kafka consumer configuration class for listening to enriched
 * {@link ReportResponseEvent} messages.
 * <p>
 * This configuration handles deserialization and listener container setup for
 * consumers
 * that subscribe to the {@code report-responses-enriched} topic.
 * </p>
 *
 * Configuration Details:
 * <ul>
 * <li><strong>Topic Group:</strong> "account-service" – groups all listeners
 * under the same consumer group.</li>
 * <li><strong>Deserializer:</strong> Custom {@link JsonDeserializer} configured
 * for {@link ReportResponseEvent}.</li>
 * <li><strong>Trusted Packages:</strong> Set to "*" to accept all classes
 * during deserialization (only safe in trusted environments).</li>
 * </ul>
 *
 * Beans:
 * <ul>
 * <li>{@code consumerFactoryEnriched} – Builds consumers for enriched report
 * responses.</li>
 * <li>{@code kafkaListenerContainerFactoryEnriched} – Enables
 * {@code @KafkaListener} binding with enriched data.</li>
 * </ul>
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Configuration
public class KafkaConsumerEnriched {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerEnriched(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Builds the {@link ConsumerFactory} for deserializing
     * {@link ReportResponseEvent} messages
     * from the {@code report-responses-enriched} topic.
     *
     * @return configured consumer factory
     */
    @Bean
    public ConsumerFactory<String, ReportResponseEvent> consumerFactoryEnriched() {
        JsonDeserializer<ReportResponseEvent> deserializer = new JsonDeserializer<>(ReportResponseEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "account-service");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    /**
     * Registers the listener container factory for enriched report response
     * consumption.
     *
     * @return a Kafka listener container factory configured for
     *         {@link ReportResponseEvent}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReportResponseEvent> kafkaListenerContainerFactoryEnriched() {
        ConcurrentKafkaListenerContainerFactory<String, ReportResponseEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryEnriched());
        return factory;
    }
}
