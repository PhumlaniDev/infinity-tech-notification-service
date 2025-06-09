package com.phumlanidev.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemDto {

    @NotNull(message = "Product ID is required")
    private Long productId;
    @NotBlank(message = "Product name is required")
    private String productName;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @NotNull(message = "Product details is required")
    private ProductDto productDetails;
}
