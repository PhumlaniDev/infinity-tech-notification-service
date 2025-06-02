package com.phumlanidev.notificationservice.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal rice;
    private ProductDto productDetails;
}
