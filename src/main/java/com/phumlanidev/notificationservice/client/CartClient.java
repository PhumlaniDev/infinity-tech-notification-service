package com.phumlanidev.notificationservice.client;

import com.phumlanidev.notificationservice.config.AuthFeingConfig;
import com.phumlanidev.notificationservice.dto.CartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "cart-service", // if using Eureka
        configuration = AuthFeingConfig.class
)
public interface CartClient {

  @GetMapping("/api/v1/cart")
  CartDto getCart();
}
