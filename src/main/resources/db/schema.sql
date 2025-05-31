CREATE TABLE notification_log (
  id SERIAL PRIMARY KEY,
  user_id VARCHAR(255),
  type VARCHAR(50),
  channel VARCHAR(100),
  status VARCHAR(50),
  destination VARCHAR(255),
  content TEXT,
  sent_at TIMESTAMP,
  failed_at TIMESTAMP,
  error_message TEXT
);