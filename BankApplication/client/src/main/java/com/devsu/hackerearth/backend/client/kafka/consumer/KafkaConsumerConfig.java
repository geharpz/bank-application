package com.devsu.hackerearth.backend.client.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.devsu.hackerearth.backend.client.dto.ReportResponseEvent;

/**
 * Kafka consumer configuration class that defines the deserialization and
 * listener container setup
 * for consuming {@link ReportResponseEvent} messages from Kafka topics.
 * <p>
 * Enables Kafka listener support and binds topic consumption to
 * application-specific domain models.
 * </p>
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Defines the consumer factory for Kafka listeners.
     * Configures deserialization of message keys as Strings and message values as
     * {@link ReportResponseEvent}.
     * 
     * @return a configured {@link ConsumerFactory} instance for processing report
     *         response events
     */
    @Bean
    public ConsumerFactory<String, ReportResponseEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "person-service");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ReportResponseEvent.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Creates the container factory used by Spring Kafka to manage listener
     * containers.
     * This factory uses the consumerFactory defined above and is required for any
     * {@code @KafkaListener} to function.
     * 
     * @return a {@link ConcurrentKafkaListenerContainerFactory} that manages Kafka
     *         message listeners
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReportResponseEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReportResponseEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}