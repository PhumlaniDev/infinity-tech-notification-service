package com.phumlanidev.notificationservice.service.invoice;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.phumlanidev.commonevents.events.payment.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class InvoiceGenerator {

  /**
   * Generates a PDF invoice for a completed payment.
   * Returns the PDF as a byte array for email attachment.
   */
  public byte[] generateInvoice(PaymentCompletedEvent event) {
    log.info("Generating invoice PDF for orderId: {}", event.getOrderId());
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdfDoc = new PdfDocument(writer);
      Document document = new Document(pdfDoc, PageSize.A4);
      document.setMargins(40, 40, 40, 40);

      addContent(document, event);

      document.close();
      log.info("Invoice PDF generated for orderId: {}", event.getOrderId());
      return outputStream.toByteArray();

    } catch (Exception e) {
      log.error("Failed to generate invoice for orderId {}: {}",
              event.getOrderId(), e.getMessage());
      throw new RuntimeException("Failed to generate invoice PDF", e);
    }
  }

  // ── private helpers ──────────────────────────────────────────────────────

  private void addContent(Document document, PaymentCompletedEvent event) {
    // ── Header ──────────────────────────────────────────────────────────
    document.add(new Paragraph("INVOICE")
            .setFontSize(26)
            .setBold()
            .setFontColor(new DeviceRgb(30, 100, 50)) // dark green — Spring brand feel
            .setMarginBottom(4));

    document.add(new Paragraph("PhumlaniDev E-Commerce")
            .setFontSize(11)
            .setFontColor(ColorConstants.GRAY)
            .setMarginBottom(2));

    document.add(new LineSeparator(new SolidLine(1f))
            .setMarginTop(8)
            .setMarginBottom(16));

    // ── Invoice metadata ─────────────────────────────────────────────────
    document.add(metaRow("Invoice Number:", "INV-" + event.getOrderId()));
    document.add(metaRow("Payment ID:", String.valueOf(event.getPaymentId())));
    document.add(metaRow("Date:",           formatInstant(event.getTimestamp())));
    document.add(metaRow("Customer:",       event.getCustomerName()));
    document.add(metaRow("Email:",          event.getToEmail()));
    document.add(metaRow("Payment Method:", event.getPaymentMethod()));

    document.add(new Paragraph(" ").setMarginBottom(12));

    // ── Line items table ─────────────────────────────────────────────────
    if (event.getInvoiceItems() != null && !event.getInvoiceItems().isEmpty()) {
      document.add(buildItemsTable(event.getInvoiceItems(), event.getCurrency()));
    }

    document.add(new Paragraph(" ").setMarginBottom(8));

    // ── Total ────────────────────────────────────────────────────────────
    document.add(new LineSeparator(new SolidLine(0.5f))
            .setMarginBottom(8));

    document.add(new Paragraph(
            "Total Paid: " + formatAmount(event.getTotalAmount(), event.getCurrency()))
            .setFontSize(14)
            .setBold()
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginBottom(24));

    // ── Footer ───────────────────────────────────────────────────────────
    document.add(new LineSeparator(new SolidLine(0.5f))
            .setMarginBottom(8));

    document.add(new Paragraph("Thank you for your purchase!")
            .setFontSize(10)
            .setFontColor(ColorConstants.GRAY)
            .setTextAlignment(TextAlignment.CENTER));

    document.add(new Paragraph("This is an automatically generated invoice. " +
            "Please keep it for your records.")
            .setFontSize(8)
            .setFontColor(ColorConstants.LIGHT_GRAY)
            .setTextAlignment(TextAlignment.CENTER));
  }

  private Table buildItemsTable(
          List<PaymentCompletedEvent.InvoiceItemDto> items,
          String currency
  ) {
    // 4 columns: Product | Qty | Unit Price | Line Total
    float[] columnWidths = {3f, 1f, 1.5f, 1.5f};
    Table table = new Table(UnitValue.createPercentArray(columnWidths))
            .useAllAvailableWidth()
            .setMarginBottom(8);

    // Header row
    Stream.of("Product", "Qty", "Unit Price", "Total")
            .forEach(header -> table.addHeaderCell(
                    new Cell().add(new Paragraph(header)
                                    .setBold()
                                    .setFontSize(10))
                            .setBackgroundColor(new DeviceRgb(230, 245, 230))
                            .setPadding(6)));

    // Data rows
    for (PaymentCompletedEvent.InvoiceItemDto item : items) {
      table.addCell(cell(item.getProductName()));
      table.addCell(cell(String.valueOf(item.getQuantity())));
      table.addCell(cell(formatAmount(item.getPrice(), currency)));
      table.addCell(cell(formatAmount(item.getLineTotal(), currency)));
    }

    return table;
  }

  /**
   * Single label + value row for invoice metadata section.
   */
  private Paragraph metaRow(String label, String value) {
    return new Paragraph()
            .add(new Text(label + " ").setBold().setFontSize(10))
            .add(new Text(value == null ? "—" : value).setFontSize(10))
            .setMarginBottom(3);
  }

  private Cell cell(String content) {
    return new Cell()
            .add(new Paragraph(content == null ? "—" : content)
                    .setFontSize(10))
            .setPadding(5);
  }

  private String formatAmount(BigDecimal amount, String currency) {
    if (amount == null) return "—";
    return currency + " " + String.format("%.2f", amount);
  }

  private String formatInstant(Instant instant) {
    if (instant == null) return "—";
    return DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm 'UTC'")
            .withZone(ZoneOffset.UTC)
            .format(instant);
  }
}
