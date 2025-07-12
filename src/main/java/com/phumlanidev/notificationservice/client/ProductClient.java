package com.phumlanidev.notificationservice.client;

import com.phumlanidev.notificationservice.config.AuthFeingConfig;
import com.phumlanidev.notificationservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service", // if using Eureka
        configuration = AuthFeingConfig.class
)
public interface ProductClient {

  @GetMapping("/api/v1/products/find/{productId}")
  ProductDto getProductDetail(@PathVariable("productId") Long productId);
}
