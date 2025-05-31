package com.phumlanidev.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationDto {
  private String userId;
  private String subject;
  private String message;
}
