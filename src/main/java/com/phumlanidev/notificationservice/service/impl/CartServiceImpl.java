package com.phumlanidev.notificationservice.service.impl;

import com.phumlanidev.notificationservice.client.CartClient;
import com.phumlanidev.notificationservice.client.ProductClient;
import com.phumlanidev.notificationservice.dto.CartDto;
import com.phumlanidev.notificationservice.dto.CartItemDto;
import com.phumlanidev.notificationservice.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl {

  private final CartClient cartClient;
  private final ProductClient productClient;

  /**
   * Comment: this is the placeholder for documentation.
   */
  public CartDto getCart() {

    CartDto cart = cartClient.getCart();

    if (cart !=null && cart.getCartItems() != null) {
      for (CartItemDto item: cart.getCartItems()) {
        if (item.getProductDetails() == null) {
          ProductDto fallback = new ProductDto();
          fallback.setName(item.getProductName());
          fallback.setPrice(item.getPrice());
          item.setProductDetails(fallback);
        }
        ProductDto productDetails = productClient.getProductDetail(item.getProductId());
        item.setProductDetails(productDetails);
      }
    }

    return cart;
  }
}
