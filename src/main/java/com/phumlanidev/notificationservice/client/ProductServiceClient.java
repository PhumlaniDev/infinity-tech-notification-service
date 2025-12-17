package com.phumlanidev.notificationservice.client;

import com.phumlanidev.commonevents.events.product.ProductDto;
import com.phumlanidev.notificationservice.config.FeingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
//        url = "${services.product-service.url}",
        path = "/api/v1/products",
        configuration = FeingConfig.class
)
public interface ProductServiceClient {

  @GetMapping("/find/{productId}")
  ProductDto getProductById(@PathVariable("productId") Long productId);
}
