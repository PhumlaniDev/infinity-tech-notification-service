package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RefundNotificationDto {

  @NotBlank(message = "User ID is required")
  private String userId;
  @NotNull(message = "Order ID is required")
  private Long orderId;
  @NotNull(message = "Refund amount is required")
  private BigDecimal refundAmount;
}
