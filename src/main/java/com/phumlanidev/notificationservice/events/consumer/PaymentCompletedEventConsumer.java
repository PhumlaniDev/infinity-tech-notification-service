package com.phumlanidev.notificationservice.events.consumer;

import com.phumlanidev.commonevents.events.payment.PaymentCompletedEvent;
import com.phumlanidev.commonevents.events.product.ProductDto;
import com.phumlanidev.notificationservice.client.ProductServiceClient;
import com.phumlanidev.notificationservice.model.NotificationLog;
import com.phumlanidev.notificationservice.repository.NotificationLogRepository;
import com.phumlanidev.notificationservice.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentCompletedEventConsumer {
  private final NotificationServiceImpl notificationService;
  private final NotificationLogRepository notificationLogRepository;
  private final ProductServiceClient productServiceClient;

  @Retryable(
          maxAttempts = 3,
          backoff = @Backoff(delay = 1000, multiplier = 2),
          retryFor = {RecoverableDataAccessException.class},
          noRetryFor = {IllegalAccessException.class}
  )
  @KafkaListener(
          topics = "payment.completed",
          groupId = "notification-group",
          containerFactory = "paymentCompletedKafkaListenerContainerFactory",
          errorHandler = "paymentCompletedEventErrorHandler"
  )
  public void paymentCompleted(ConsumerRecord<String, PaymentCompletedEvent> record) {
    PaymentCompletedEvent event = record.value();
    log.info("📩 Received PaymentCompletedEvent for order {}", event.getOrderId());

    try {
      if (event.getItems() != null && !event.getItems().isEmpty()) {
        event.getItems().forEach(item -> {
          try {
            ProductDto product = productServiceClient.getProductById(item.getProductId());
            item.setProductDetails(product);
            log.info("🔍 Fetched product details: {} for order {}", product.getName(), event.getOrderId());
          } catch (Exception ex) {
            log.error("❌ Error fetching product details for product {}: {}", item.getProductId(), ex.getMessage());
            throw new RuntimeException(ex);
          }
        });
      }
      notificationService.sendPaymentConfirmation(event);
      log.info("✅ Payment confirmation email sent for order: {}", event.getOrderId());
      notificationLogRepository.save(NotificationLog.success("PaymentConfirmation", event.getToEmail()));

    } catch(Exception ex) {
      log.error("❌ Error fetching product details for order {}: {}", event.getOrderId(), ex.getMessage());
      throw new RuntimeException(ex);
    }
  }
}