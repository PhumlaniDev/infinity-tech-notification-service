package com.phumlanidev.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notification_log")
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
}