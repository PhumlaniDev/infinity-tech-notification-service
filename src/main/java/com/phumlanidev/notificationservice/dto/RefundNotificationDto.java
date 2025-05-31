package com.phumlanidev.notificationservice.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundNotificationDto {
  private String userId;
  private Long orderId;
  private BigDecimal refundAmount;
}
