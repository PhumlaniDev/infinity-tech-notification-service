package com.phumlanidev.notificationservice.service.impl;

import com.phumlanidev.notificationservice.dto.CartDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender emailSender;
  private final TemplateEngine templateEngine;
  private final CartServiceImpl cartService;
  private final List<CartDto> items = new ArrayList<>();

  public void sendOrderConfirmationEmail(String to, Long orderId, BigDecimal totalAmount) {
    Context context = new Context();
    context.setVariable("orderId", orderId);
    context.setVariable("totalAmount", totalAmount);

    CartDto cartDto = cartService.getCart();
    items.clear();
    items.add(cartDto);
    context.setVariable("items", items);

    Instant orderDate = Instant.now();
    context.setVariable("orderDate", orderDate);

    String htmlBody = templateEngine.process("orderConfirmationEmail", context);

    try {
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(to);
      helper.setSubject("Order Confirmation - Order #" + orderId);
      helper.setText(htmlBody, true);

      emailSender.send(message);
      log.info("✅ Order confirmation email sent to {}", to);

    } catch (Exception e) {
      log.error("❌ Failed to send order confirmation email to {}: {}", to, e.getMessage());
      throw new RuntimeException("Failed to send order confirmation email", e);
    }
  }
}
