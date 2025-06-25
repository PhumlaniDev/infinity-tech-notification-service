package com.phumlanidev.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class PaymentConfirmationRequestDto {

  private String toEmail;
  private String orderId;
  private BigDecimal totalAmount;
  private Instant timestamp;
}
