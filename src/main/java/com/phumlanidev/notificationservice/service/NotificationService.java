package com.phumlanidev.notificationservice.service;

import com.phumlanidev.notificationservice.dto.*;


public interface NotificationService {

  void sendOrderNotification(OrderNotificationDto dto);
  void sendPasswordResetNotification(PasswordResetDto dto);
  void sendAccountVerificationNotification(AccountVerificationDto dto);
  void sendEmailNotification(EmailNotificationDto dto);
  //  void sendSmsNotification(String userId, String phoneNumber, String message);
  //  void sendPushNotification(String userId, String title, String message);
  void sendOrderShippedNotification(OrderStatusDto dto);
  void sendOrderDeliveredNotification(OrderStatusDto dto);
  void sendOrderCancellationNotification(OrderStatusDto dto);
  //  void sendPromotionalNotification(String userId, String promotionDetails);
  void sendRefundProcessedNotification(RefundNotificationDto dto);
  void sendCartAbandonmentReminder(CartReminderDto dto);

}

