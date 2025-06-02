package com.phumlanidev.notificationservice.service.impl;

import com.phumlanidev.notificationservice.dto.CartDto;
import com.phumlanidev.notificationservice.dto.CartItemDto;
import com.phumlanidev.notificationservice.dto.ProductDto;
import com.phumlanidev.notificationservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CartServiceImpl {

  private final RestTemplate restTemplate;
  private final ProductService productService;

  /**
   * Comment: this is the placeholder for documentation.
   */
  public CartDto getCart() {
    String cartServiceUrl = "http://localhost:9200/api/v1/cart";

    // Retrieve the Jwt object from the SecurityContext
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();

    // Extract the token string from the Jwt object
    String jwtToken = jwt.getTokenValue();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + jwtToken);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    CartDto cart = restTemplate.exchange(cartServiceUrl, HttpMethod.GET, entity, CartDto.class).getBody();

    if (cart !=null && cart.getCartItems() != null) {
      for (CartItemDto item: cart.getCartItems()) {
        ProductDto productDetails = productService.getProductDetail(item.getProductId());
        item.setProductDetails(productDetails);
      }
    }

    return cart;
  }
}
