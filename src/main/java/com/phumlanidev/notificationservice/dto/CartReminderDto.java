package com.phumlanidev.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CartReminderDto {
  private String userId;
  private List<CartItemDto> items;
  private Instant abandonedAt;
}