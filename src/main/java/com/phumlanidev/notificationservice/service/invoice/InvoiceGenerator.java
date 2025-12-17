package com.phumlanidev.notificationservice.service.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.phumlanidev.commonevents.events.order.OrderPlacedEvent;
import com.phumlanidev.commonevents.events.payment.PaymentCompletedEvent;
import com.phumlanidev.commonevents.events.product.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class InvoiceGenerator {

  public byte[] generateInvoice(PaymentCompletedEvent event) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Document document = new Document(PageSize.A4, 36, 36, 90, 36);
      PdfWriter writer = PdfWriter.getInstance(document, out);
      document.open();

      PdfPTable headerTable = new PdfPTable(2);
      headerTable.setWidthPercentage(100);
      headerTable.setWidths(new float[]{1, 2});


      //logo
      PdfPCell logoCell;
      URL logoUrl = getClass().getResource("/static/images/logo.png");
      if (logoUrl != null) {
        Image logo = Image.getInstance(logoUrl);
        logo.scaleToFit(200, 200);
        logoCell = new PdfPCell(logo, false);
      } else {
        logoCell = new PdfPCell(new Phrase("INFINITY TECH"));
        throw new Exception("Logo image not found at specified path.");
      }

      logoCell.setBorder(Rectangle.NO_BORDER);
      logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
      headerTable.addCell(logoCell);

      //title
      Font titleFont = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, BaseColor.BLACK);
      PdfPCell titleCell = new PdfPCell(new Phrase("INVOICE", titleFont));
      titleCell.setBorder(Rectangle.NO_BORDER);
      titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      headerTable.addCell(titleCell);

      document.add(headerTable);

      document.add(new Paragraph("\n\n"));
      document.add(new LineSeparator());

      // info section
      PdfPTable infoTable = new PdfPTable(2);
      infoTable.setWidthPercentage(100);
      infoTable.setSpacingBefore(10f);

      Instant timestamp = event.getTimestamp();
      LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, java.time.ZoneId.systemDefault());
      String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

      addInfoRow(infoTable, "Invoice Data:", formattedDate);
      addInfoRow(infoTable, "Invoice #:", String.valueOf(event.getOrderId()));
      addInfoRow(infoTable, "Transaction ID:", event.getTransactionId());
      addInfoRow(infoTable, "Billed To:", event.getToEmail());
      addInfoRow(infoTable, "Currency:", event.getCurrency());

      document.add(new Paragraph("\n"));

      // product table
      Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
      Font tableBodyFont = new Font(Font.FontFamily.HELVETICA, 12);
      PdfPTable itemTable = new PdfPTable(4);
      itemTable.setWidthPercentage(100);
      itemTable.setWidths(new float[]{4, 1, 2, 2});

      //header
      addHeaderCell(itemTable, "Description", tableHeaderFont);
      addHeaderCell(itemTable, "Qty", tableHeaderFont);
      addHeaderCell(itemTable, "Unit Price", tableHeaderFont);
      addHeaderCell(itemTable, "Subtotal", tableHeaderFont);

      // body
      List<OrderPlacedEvent.OrderItemDto> items = event.getItems();
      double total = 0.0;
      if (items != null && !items.isEmpty()) {
        for (OrderPlacedEvent.OrderItemDto item : items) {
          ProductDto details = item.getProductDetails();
          String productName = details != null ? details.getName() : "Unknown Product";
          int qty = item.getQuantity() != 0 ? item.getQuantity() : 0;
          BigDecimal price = details != null ? details.getPrice() : BigDecimal.ZERO;
          BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
          total += lineTotal.doubleValue();

//          String desc = item.getProductDetails().getName() != null ? item.getProductDetails().getName() : "Product " + item.getProductId();
//          int qty = item.getQuantity() != 0 ? item.getQuantity() : 0;
//          BigDecimal bigDecimalFromInt = BigDecimal.valueOf(qty);
////          BigDecimal price = item.getProductDetails().getPrice() != null ? item.getProductDetails().getPrice() : BigDecimal.valueOf(0.0);
//          BigDecimal subtotal = bigDecimalFromInt.multiply(price);

          addBodyCell(itemTable, productName, tableBodyFont);
          addBodyCell(itemTable, String.valueOf(qty) , tableBodyFont);
          addBodyCell(itemTable,String.format("%.2f %s", price, event.getCurrency()), tableBodyFont);
          addBodyCell(itemTable,String.format("%.2f %s", total, event.getCurrency()), tableBodyFont);
        }
      } else {
        addBodyCell(itemTable, "No items found", tableBodyFont);
        addBodyCell(itemTable, "-", tableBodyFont);
        addBodyCell(itemTable, "-", tableBodyFont);
        addBodyCell(itemTable, "-", tableBodyFont);
      }
      document.add(itemTable);

      // total section
      Paragraph totalParagraph = new Paragraph(
              String.format("Tota: %s %.2f", event.getCurrency(), total > 0 ? total :
                      event.getTotalAmount().doubleValue()),
              new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
      );
      totalParagraph.setAlignment(Element.ALIGN_RIGHT);
      totalParagraph.setSpacingBefore(15f);
      document.add(totalParagraph);

      //footer
      Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
      Paragraph footer = new Paragraph(" Thank you for your purchase! For support, contact us at " +
              "supportt@infinitytech.com", footerFont);
      footer.setAlignment(Element.ALIGN_CENTER);
      footer.setSpacingBefore(20);
      document.add(footer);

      document.close();
      writer.close();
      return out.toByteArray();

    } catch (Exception e) {
      log.error("Error generating invoice for payment ID {}: {}", event.getPaymentId(), e.getMessage());
      throw new RuntimeException("Failed to generate invoice", e);
    }
  }

  private void addBodyCell(PdfPTable itemTable, String text, Font tableBodyFont) {
    PdfPCell cell = new PdfPCell(new Phrase(text, tableBodyFont));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(8);
    itemTable.addCell(cell);
  }

  private void addHeaderCell(PdfPTable table, String text, Font tableHeaderFont) {
    PdfPCell cell = new PdfPCell(new Phrase(text, tableHeaderFont));
    cell.setBackgroundColor(BaseColor.GRAY);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(8);
    table.addCell(cell);
  }

  private void addInfoRow(PdfPTable infoTable, String label, String value) {
    Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);
    PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
    labelCell.setBorder(Rectangle.NO_BORDER);
    PdfPCell valueCell = new PdfPCell(new Phrase(value!= null ? value : "-", valueFont));
    valueCell.setBorder(Rectangle.NO_BORDER);
    infoTable.addCell(labelCell);
    infoTable.addCell(valueCell);
  }

  private void addTableRow(PdfPTable table, String col1, String col2, String col3) {
    table.addCell(col1);
    table.addCell(col2);
    table.addCell(col3);
  }

  private void addTableHeader(PdfPTable table, String header) {
    Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
    cell.setBackgroundColor(new BaseColor(230, 230, 230));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(5);
    table.addCell(header);
  }

  public String formatAmount(BigDecimal amount, String currency) {
    BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    return currency + " " + decimalFormat.format(roundedAmount);
  }
}
