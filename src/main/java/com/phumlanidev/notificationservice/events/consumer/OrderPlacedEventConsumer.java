package com.phumlanidev.notificationservice.events.consumer;


import com.phumlanidev.commonevents.events.order.OrderNotificationDto;
import com.phumlanidev.commonevents.events.order.OrderPlacedEvent;
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
public class OrderPlacedEventConsumer {

  private final NotificationServiceImpl notificationService;

  @Retryable(
          backoff = @Backoff(delay = 1000, multiplier = 2),
          retryFor = {RecoverableDataAccessException.class},
          noRetryFor = {IllegalAccessException.class}
  )
  @KafkaListener(
          topics = "order.placed",
          groupId = "notification-group",
          containerFactory = "orderPlacedKafkaListenerContainerFactory",
          errorHandler = "orderKafkaListenerErrorHandler"
  )
  public void consumeOrderPlaced(ConsumerRecord<String, OrderPlacedEvent> record) {
    OrderPlacedEvent event = record.value();

    log.info("📩 Notification received: {}", event);
    try {
      OrderNotificationDto notificationDto = OrderNotificationDto.builder()
              .userId(event.getUserId())
              .orderId(event.getOrderId())
              .total(event.getTotal())
              .items(event.getItems())
              .toEmail(event.getToEmail())
              .build();

      notificationService.sendOrderNotification(notificationDto);
    } catch (Exception ex) {
      log.error("Failed to send notification for OrderPlacedEvent: {}", event, ex);
      throw ex; // This will trigger the retry mechanism
    }
  }
}
