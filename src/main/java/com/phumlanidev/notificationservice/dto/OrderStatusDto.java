package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrderStatusDto {

  @NotBlank(message = "User ID is required")
  private String userId;
  @NotNull(message = "Order ID is required")
  private Long orderId;
  @NotNull(message = "Event time is required")
  private Instant eventTime;
  @NotBlank(message = "Status is required")
  private String status; // e.g., "SHIPPED", "DELIVERED", "CANCELLED"
  @NotBlank(message = "Reason is required")
  private String reason; // Optional for cancellations
}