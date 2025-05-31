package com.phumlanidev.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountVerificationDto {
  private String userId;
  private String email;
  private String verificationLink;
}