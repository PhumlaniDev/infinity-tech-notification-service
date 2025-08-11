package com.phumlanidev.notificationservice.config;

import com.phumlanidev.commonevents.events.OrderPlacedEvent;
import com.phumlanidev.commonevents.events.PaymentCompletedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

  private final static String bootstrapServers = "localhost:29092";

  @Bean
  public ConsumerFactory<String, PaymentCompletedEvent> paymentCompletedEventConsumerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-group");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    config.put("spring.deserializer.key.delegate.class", StringDeserializer.class);
    config.put("spring.deserializer.value.delegate.class", JsonDeserializer.class);
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.phumlanidev.commonevents.events");
    config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
    config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.phumlanidev.commonevents.events.PaymentCompletedEvent");
//    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(config);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentCompletedKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(paymentCompletedEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    factory.setCommonErrorHandler(new DefaultErrorHandler());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, OrderPlacedEvent> orderPlacedEventConsumerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-group");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    config.put("spring.deserializer.key.delegate.class", StringDeserializer.class);
    config.put("spring.deserializer.value.delegate.class", JsonDeserializer.class);
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.phumlanidev.commonevents.events");
    config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
    config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.phumlanidev.commonevents.events.OrderPlacedEvent");
//    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(config);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> orderPlacedKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(orderPlacedEventConsumerFactory());
    factory.setConcurrency(3);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    factory.setCommonErrorHandler(new DefaultErrorHandler());
    return factory;
  }
}
