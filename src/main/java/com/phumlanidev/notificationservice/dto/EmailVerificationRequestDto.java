package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerificationRequestDto {
  @NotBlank
  private String email;
}
