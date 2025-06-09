package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationDto {

  @NotBlank(message = "User ID is required")
  private String userId;
  @NotBlank(message = "Subject is required")
  private String subject;
  @NotBlank(message = "Message is required")
  private String message;
}
