package com.phumlanidev.notificationservice.service;

import com.phumlanidev.notificationservice.dto.ProductDto;
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
public class ProductService {

  private final RestTemplate restTemplate;

  public ProductDto getProductDetail(Long productId) {
    String productServiceUrl = "http://localhost:9400/api/v1/products/find/" + productId;

    // Retrieve the Jwt object from the SecurityContext
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();

    // Extract the token string from the Jwt object
    String jwtToken = jwt.getTokenValue();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + jwtToken);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(productServiceUrl, HttpMethod.GET, entity, ProductDto.class).getBody();
  }
}
