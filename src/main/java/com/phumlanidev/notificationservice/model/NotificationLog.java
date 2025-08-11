package com.phumlanidev.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userId;
  private String type; // e.g., "EMAIL", "SMS", "PUSH"
  private String channel; // e.g., "PASSWORD_RESET", "ORDER_PLACED"
  private String status; // e.g., "SENT", "FAILED"
  private String destination; // email, phone, etc.

  @Column(length = 2048)
  private String content; // optional: body or metadata

  private Instant sentAt;
  private Instant failedAt;
  private String errorMessage;


  public static NotificationLog success(String type, String email) {
    return NotificationLog.builder()
            .type(type)
            .destination(email)
            .status("SUCCESS")
            .content("Email sent successfully")
            .sentAt(Instant.now())
            .build();
  }

  public static NotificationLog failure(String type, String email, String errorMessage) {
    return NotificationLog.builder()
            .type(type)
            .destination(email)
            .status("FAILED")
            .content(errorMessage)
            .sentAt(Instant.now())
            .build();
  }
}