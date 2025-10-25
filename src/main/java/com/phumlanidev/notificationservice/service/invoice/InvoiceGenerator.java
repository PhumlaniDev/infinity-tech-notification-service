package com.phumlanidev.notificationservice.service.invoice;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.phumlanidev.commonevents.events.payment.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
public class InvoiceGenerator {

  public byte[] generateInvoice(PaymentCompletedEvent event) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Document document = new Document();
      PdfWriter.getInstance(document, out);
      document.open();

      Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
      Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
      Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

      document.add(new Paragraph("INVOICE", titleFont));
      document.add(new Paragraph(" ")); // Empty line
      document.add(new Paragraph("Order ID: " + event.getOrderId(), normalFont));
      document.add(new Paragraph("Transaction ID: " + event.getTransactionId(), normalFont));
      document.add(new Paragraph("Currency: " + event.getCurrency(), normalFont));
      document.add(new Paragraph("Date: " + event.getTimestamp().atZone(ZoneId.systemDefault())
              .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), normalFont));
      document.add(new Paragraph(" ")); // Empty line

      PdfPTable table = new PdfPTable(2);
      table.setWidthPercentage(100);
      table.addCell(new Phrase("Description", headerFont));
      table.addCell(new Phrase("Amount", headerFont));

      table.addCell(new Phrase("Order Payment", normalFont));
      table.addCell(new Phrase(formatAmount(event.getTotalAmount(), event.getCurrency()), normalFont));

      document.add(table);
      document.add(new Paragraph(" "));
      document.add(new Paragraph("Total: " + formatAmount(event.getTotalAmount(), event.getCurrency()), headerFont));

      document.add(new Paragraph("\nThank you for your purchase!", normalFont));

      document.close();
      return out.toByteArray();

    } catch (Exception e) {
      log.error("Error generating invoice for payment ID {}: {}", event.getPaymentId(), e.getMessage());
      throw new RuntimeException("Failed to generate invoice", e);
    }
  }

  public String formatAmount(BigDecimal amount, String currency) {
    BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    return currency + " " + decimalFormat.format(roundedAmount);
  }
}
