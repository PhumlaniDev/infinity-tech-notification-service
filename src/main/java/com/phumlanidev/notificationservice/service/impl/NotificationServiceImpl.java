package com.phumlanidev.notificationservice.service.impl;

import com.phumlanidev.notificationservice.config.JwtAuthenticationConverter;
import com.phumlanidev.notificationservice.dto.*;
import com.phumlanidev.notificationservice.model.NotificationLog;
import com.phumlanidev.notificationservice.repository.NotificationLogRepository;
import com.phumlanidev.notificationservice.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final EmailService emailService;
  private final NotificationLogRepository notificationLogRepository;
  private final JwtAuthenticationConverter jwtAuthenticationConverter;
  private final HttpServletRequest request;
  private final AuditLogServiceImpl auditLogService;


  @Override
  public void sendOrderNotification(OrderNotificationDto dto) {
    log.info("Sending order notification for order ID: {}", dto.getOrderId());
    emailService.sendOrderConfirmationEmail(dto.getToEmail(), dto.getOrderId(), dto.getTotal());

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
    logAudit("ORDER_NOTIFICATION_SENT", "Order notification sent for order ID: " + dto.getOrderId());
    log.info("Order notification sent successfully for order ID: {}", dto.getOrderId());
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

  @Override
  public void sendPaymentConfirmation(PaymentConfirmationRequestDto dto) {
    log.info("Sending payment confirmation for order ID: {}", dto.getOrderId());
    if (dto.getToEmail() == null || dto.getToEmail().isEmpty()) {
      log.error("Email address is required for payment confirmation");
      throw new IllegalArgumentException("Email address is required for payment confirmation");
    }
    emailService.sendPaymentConfirmationEmail(dto);

    Jwt jwt = jwtAuthenticationConverter.getJwt();

    String userId = jwtAuthenticationConverter.extractUserId(jwt);

    notificationLogRepository.save(
            NotificationLog.builder()
                    .userId(userId)
                    .channel("PAYMENT_CONFIRMATION")
                    .type("EMAIL")
                    .status("SENT")
                    .sentAt(Instant.parse(Instant.now().toString()))
                    .content("Payment confirmation for order ID: " + dto.getOrderId())
                    .build()
    );
    logAudit("PAYMENT_CONFIRMATION_SENT", "Payment confirmation sent for order ID: " + dto.getOrderId());
    log.info("Payment confirmation sent successfully for order ID: {}", dto.getOrderId());
  }

  @Override
  public void sendPasswordResetNotification(PasswordResetDto dto) {
    log.info("Sending password reset notification to email: {}", dto.getEmail());
    if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
      log.error("Email address is required for password reset");
      throw new IllegalArgumentException("Email address is required for password reset");
    }
    emailService.sendResetPasswordEmail(dto.getEmail());

    Jwt jwt = jwtAuthenticationConverter.getJwt();
    String userId = jwtAuthenticationConverter.extractUserId(jwt);

    notificationLogRepository.save(
            NotificationLog.builder()
                    .userId(userId)
                    .channel("PASSWORD_RESET")
                    .type("EMAIL")
                    .status("SENT")
                    .sentAt(Instant.now())
                    .content("Password reset notification sent to " + dto.getEmail())
                    .build()
    );
    logAudit("PASSWORD_RESET_SENT", "Password reset notification sent to " + dto.getEmail());
    log.info("Password reset notification sent successfully to email: {}", dto.getEmail());

  }

  @Override
  public void sendEmailVerificationNotification(EmailVerificationRequestDto dto) {
    log.info("Sending email verification notification to: {}", dto.getEmail());
    if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
      log.error("Email address is required for email verification");
      throw new IllegalArgumentException("Email address is required for email verification");
    }
    emailService.sendEmailVerificationNotification(dto.getEmail());

    Jwt jwt = jwtAuthenticationConverter.getJwt();
    String userId = jwtAuthenticationConverter.extractUserId(jwt);

    notificationLogRepository.save(
            NotificationLog.builder()
                    .userId(userId)
                    .channel("EMAIL_VERIFICATION")
                    .type("EMAIL")
                    .status("SENT")
                    .sentAt(Instant.now())
                    .content("Email verification sent to " + dto.getEmail())
                    .build()
    );
    logAudit("EMAIL_VERIFICATION_SENT", "Email verification sent to " + dto.getEmail());
    log.info("Email verification sent successfully to: {}", dto.getEmail());
  }

  @Override
  public void sendUserRegistrationNotification(UserRegistrationDto dto) {

  }

  private void logAudit(String action, String details) {
    String clientIp = request.getRemoteAddr();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth != null ? auth.getName() : "anonymous";
    Jwt jwt = jwtAuthenticationConverter.getJwt();
    String userId = jwtAuthenticationConverter.extractUserId(jwt);

    auditLogService.log(
            action,
            userId,
            username,
            clientIp,
            details
    );
  }
}
