package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class OrderNotificationDto {

  @NotBlank(message = "User ID is required")
  private String userId;
  @NotNull(message = "Order ID is required")
  private Long orderId;
  @NotNull(message = "Email recipient is required")
  private String toEmail;
  @NotNull(message = "Total amount is required")
  private BigDecimal total;
  @NotNull(message = "Timestamp is required")
  private Instant timestamp;
}
