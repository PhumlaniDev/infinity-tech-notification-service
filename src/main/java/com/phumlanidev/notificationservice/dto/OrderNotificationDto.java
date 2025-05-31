package com.phumlanidev.notificationservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderNotificationDto {
  private String userId;
  private Long orderId;
  private BigDecimal total;
  private Instant timestamp;
}
