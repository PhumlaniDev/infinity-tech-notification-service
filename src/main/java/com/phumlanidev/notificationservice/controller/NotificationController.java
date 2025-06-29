package com.phumlanidev.notificationservice.controller;

import com.phumlanidev.notificationservice.dto.EmailVerificationRequestDto;
import com.phumlanidev.notificationservice.dto.OrderNotificationDto;
import com.phumlanidev.notificationservice.dto.PasswordResetDto;
import com.phumlanidev.notificationservice.dto.PaymentConfirmationRequestDto;
import com.phumlanidev.notificationservice.service.impl.NotificationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

  private final NotificationServiceImpl notificationService;

  @PostMapping("/email")
  public ResponseEntity<Void> sendOrderNotification(@Valid @RequestBody OrderNotificationDto dto) {
    notificationService.sendOrderNotification(dto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/payment-confirmation")
  public ResponseEntity<Void> sendPaymentConfirmation(@Valid @RequestBody PaymentConfirmationRequestDto dto) {
    notificationService.sendPaymentConfirmation(dto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/password-reset")
  public ResponseEntity<Void> sendPasswordReset(@Valid @RequestBody PasswordResetDto dto) {
    notificationService.sendPasswordResetNotification(dto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/email-verification")
  public ResponseEntity<Void> sendEmailVerification(@Valid @RequestBody EmailVerificationRequestDto dto) {
    notificationService.sendEmailVerificationNotification(dto);
    return ResponseEntity.ok().build();
  }
}
