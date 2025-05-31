package com.phumlanidev.notificationservice.service.impl;

import com.phumlanidev.notificationservice.dto.AccountVerificationDto;
import com.phumlanidev.notificationservice.dto.CartReminderDto;
import com.phumlanidev.notificationservice.dto.EmailNotificationDto;
import com.phumlanidev.notificationservice.dto.OrderNotificationDto;
import com.phumlanidev.notificationservice.dto.OrderStatusDto;
import com.phumlanidev.notificationservice.dto.PasswordResetDto;
import com.phumlanidev.notificationservice.dto.RefundNotificationDto;
import com.phumlanidev.notificationservice.model.NotificationLog;
import com.phumlanidev.notificationservice.repository.NotificationLogRepository;
import com.phumlanidev.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final EmailService emailService;
  private final NotificationLogRepository notificationLogRepository;


  @Override
  public void sendOrderNotification(OrderNotificationDto dto) {
    log.info("Sending order notification for order ID: {}", dto.getOrderId());
    emailService.sendOrderConfirmationEmail("arendsephumlani@gmail.com", dto.getOrderId(), dto.getTotal());

    notificationLogRepository.save(
            NotificationLog.builder()
                    .userId(dto.getUserId())
                    .channel("ORDER_PLACED")
                    .type("EMAIL")
                    .status("SENT")
                    .sentAt(Instant.now())
                    .content("Order confirmation for order ID: " + dto.getOrderId())
                    .build()
    );
    log.info("Order notification sent successfully for order ID: {}", dto.getOrderId());
  }

  @Override
  public void sendPasswordResetNotification(PasswordResetDto dto) {

  }

  @Override
  public void sendAccountVerificationNotification(AccountVerificationDto dto) {

  }

  @Override
  public void sendEmailNotification(EmailNotificationDto dto) {

  }

  @Override
  public void sendOrderShippedNotification(OrderStatusDto dto) {

  }

  @Override
  public void sendOrderDeliveredNotification(OrderStatusDto dto) {

  }

  @Override
  public void sendOrderCancellationNotification(OrderStatusDto dto) {

  }

  @Override
  public void sendRefundProcessedNotification(RefundNotificationDto dto) {

  }

  @Override
  public void sendCartAbandonmentReminder(CartReminderDto dto) {

  }
}
