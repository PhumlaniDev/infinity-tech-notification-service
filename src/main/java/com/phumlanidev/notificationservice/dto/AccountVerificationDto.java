package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountVerificationDto {

  @NotBlank(message = "User ID is required")
  private String userId;
  @NotBlank(message = "Email is required")
  private String email;
  @NotBlank(message = "Verification link is required")
  private String verificationLink;
}