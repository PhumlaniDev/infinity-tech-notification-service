package com.phumlanidev.notificationservice.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStatusDto {
  private String userId;
  private Long orderId;
  private Instant eventTime;
  private String status; // e.g., "SHIPPED", "DELIVERED", "CANCELLED"
  private String reason; // Optional for cancellations
}