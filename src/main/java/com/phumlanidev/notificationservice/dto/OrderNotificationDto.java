package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
  private List<OrderItemDto> items;
}
