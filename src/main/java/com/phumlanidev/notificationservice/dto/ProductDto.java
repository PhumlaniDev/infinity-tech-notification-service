package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

  @NotBlank(message = "Product name is required")
  private String name;
  @NotBlank(message = "Product description is required")
  private String description;
  @NotNull(message = "Price is required")
  private BigDecimal price;
  @NotBlank(message = "Image URL is required")
  private String imageUrl;
}