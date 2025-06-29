package com.phumlanidev.notificationservice.service.impl;

import com.phumlanidev.notificationservice.dto.CartDto;
import com.phumlanidev.notificationservice.dto.PaymentConfirmationRequestDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender emailSender;
  private final TemplateEngine templateEngine;
  private final CartServiceImpl cartService;

  @Value("${keycloak.auth-server-url}")
  private String keycloakAuthServerUrl;

  @Value("${keycloak.realm}")
  private String keycloakRealm;

  @Value("${keycloak.admin.username}")
  private String keycloakAdminUsername;

  @Value("${keycloak.admin.password}")
  private String keycloakAdminPassword;


  public void sendOrderConfirmationEmail(String to, Long orderId, BigDecimal totalAmount) {
    Context context = new Context();
    context.setVariable("orderId", orderId);
    context.setVariable("totalAmount", totalAmount);

    CartDto cartDto = cartService.getCart();
    log.debug("Cart details: {}", cartDto);
    context.setVariable("items", cartDto.getCartItems());

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

  public void sendResetPasswordEmail(String email) {
    try (Keycloak adminClient = createAdminClient()) {
      List<UserRepresentation> users = adminClient.realm(keycloakRealm).users().searchByEmail(email, true);
      UserRepresentation user = users.stream()
              .filter(u -> email.equalsIgnoreCase(u.getEmail()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
      adminClient.realm(keycloakRealm).users().get(user.getId())
              .executeActionsEmail(List.of("UPDATE_PASSWORD"));
      log.info("✅ Password reset email sent to {}", email);
    } catch (Exception e) {
      log.error("❌ Failed to send password reset email to {}: {}", email, e.getMessage());
      throw new RuntimeException("Failed to send password reset email", e);
    }
//    Context context = new Context();
//    context.setVariable("resetLink", resetLink); // 👈 from backend or keycloak
//
//    String htmlBody = templateEngine.process("passwordResetEmail", context);
//
//    try {
//      MimeMessage message = emailSender.createMimeMessage();
//      MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//      helper.setTo(to);
//      helper.setSubject("Password Reset Request");
//      helper.setText(htmlBody, true);
//
//      emailSender.send(message);
//      log.info("✅ Password reset email sent to {}", to);
//    } catch (Exception e) {
//      log.error("❌ Failed to send password reset email to {}: {}", to, e.getMessage());
//      throw new RuntimeException("Failed to send password reset email", e);
//    }
  }

  public void sendEmailVerificationNotification(String email) {
    try (Keycloak adminClient = createAdminClient()) {
      List<UserRepresentation> users = adminClient.realm(keycloakRealm).users().searchByEmail(email, true);
      UserRepresentation user = users.stream()
              .filter(u -> email.equalsIgnoreCase(u.getEmail()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
      if (!Boolean.TRUE.equals(user.isEmailVerified())) {
        adminClient.realm(keycloakRealm).users().get(user.getId()).sendVerifyEmail();
        log.info("✅ Email verification link sent to {}", email);
      }
    } catch (Exception e) {
      log.error("❌ Failed to send email verification link to {}: {}", email, e.getMessage());
      throw new RuntimeException("Failed to send email verification link", e);
    }
  }

  public void sendPaymentConfirmationEmail(PaymentConfirmationRequestDto request) {
    Context context = new Context();
    context.setVariable("orderId", request.getOrderId());
    context.setVariable("amount", request.getTotalAmount());
    context.setVariable("timestamp", request.getTimestamp());

    String html = templateEngine.process("paymentConfirmationEmail", context);

    try {
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(request.getToEmail());
      helper.setSubject("✅ Payment Confirmation for Order #" + request.getOrderId());
      helper.setText(html, true);

      emailSender.send(message);
      log.info("✅ Sent payment confirmation to {}", request.getToEmail());

    } catch (Exception e) {
      log.error("❌ Failed to send payment confirmation: {}", e.getMessage());
      throw new RuntimeException("Failed to send email", e);
    }
  }

  private Keycloak createAdminClient() {
    return KeycloakBuilder.builder()
            .serverUrl(keycloakAuthServerUrl)
            .realm("master")
            .clientId("admin-cli")
            .username(keycloakAdminUsername)
            .password(keycloakAdminPassword)
            .grantType(OAuth2Constants.PASSWORD)
            .build();
  }

}
