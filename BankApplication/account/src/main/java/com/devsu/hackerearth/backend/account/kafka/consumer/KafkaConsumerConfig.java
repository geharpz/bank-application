package com.devsu.hackerearth.backend.account.kafka.consumer;

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

import com.devsu.hackerearth.backend.account.dto.ReportRequestEvent;

/**
 * Kafka consumer configuration class for listening to
 * {@link ReportRequestEvent} messages.
 * <p>
 * Sets up deserialization logic, consumer properties, and listener container
 * factory for processing
 * Kafka messages in the reporting request pipeline.
 * </p>
 *
 * Key Configurations:
 * <ul>
 * <li><strong>Bootstrap Servers:</strong> Injected from application properties
 * via {@link KafkaProperties}.</li>
 * <li><strong>Deserializer:</strong> {@link JsonDeserializer} configured for
 * {@link ReportRequestEvent} payloads.</li>
 * <li><strong>Group ID:</strong> "account-service" – ensures consumer grouping
 * per service.</li>
 * <li><strong>Trusted Packages:</strong> Wildcard enabled for deserialization
 * flexibility (use with caution in prod).</li>
 * </ul>
 *
 * Beans:
 * <ul>
 * <li>{@code consumerFactory} – Configures how Kafka messages are deserialized
 * and consumed.</li>
 * <li>{@code kafkaListenerContainerFactory} – Enables {@code @KafkaListener}
 * annotations using the above factory.</li>
 * </ul>
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Defines a Kafka {@link ConsumerFactory} with custom deserializer for
     * {@link ReportRequestEvent}.
     *
     * @return configured consumer factory
     */
    @Bean
    public ConsumerFactory<String, ReportRequestEvent> consumerFactory() {
        JsonDeserializer<ReportRequestEvent> deserializer = new JsonDeserializer<>(ReportRequestEvent.class);
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
     * Registers the {@link ConcurrentKafkaListenerContainerFactory} bean to support
     * {@code @KafkaListener} usage.
     *
     * @return listener container factory configured for {@link ReportRequestEvent}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReportRequestEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReportRequestEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}