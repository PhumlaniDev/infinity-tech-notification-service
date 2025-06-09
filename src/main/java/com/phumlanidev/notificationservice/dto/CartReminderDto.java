package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CartReminderDto {

  @NotBlank(message = "User ID is required")
  private String userId;
  @NotEmpty(message = "Cart items cannot be empty")
  private List<CartItemDto> items;
  @NotNull(message = "Abandoned At (Time) is required")
  private Instant abandonedAt;
}