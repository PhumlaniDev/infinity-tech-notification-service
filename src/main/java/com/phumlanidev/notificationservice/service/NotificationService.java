package com.phumlanidev.notificationservice.service;

import com.phumlanidev.notificationservice.dto.*;


public interface NotificationService {

  void sendOrderNotification(OrderNotificationDto dto);
  void sendEmailNotification(EmailNotificationDto dto);
  void sendOrderShippedNotification(OrderStatusDto dto);
  void sendOrderDeliveredNotification(OrderStatusDto dto);
  void sendOrderCancellationNotification(OrderStatusDto dto);
  //  void sendPromotionalNotification(String userId, String promotionDetails);
  void sendRefundProcessedNotification(RefundNotificationDto dto);
  void sendCartAbandonmentReminder(CartReminderDto dto);
  void sendPaymentConfirmation(PaymentConfirmationRequestDto dto);
  void sendPasswordResetNotification(PasswordResetDto dto);
  void sendEmailVerificationNotification(EmailVerificationRequestDto dto);
  void sendUserRegistrationNotification(UserRegistrationDto dto);
}

