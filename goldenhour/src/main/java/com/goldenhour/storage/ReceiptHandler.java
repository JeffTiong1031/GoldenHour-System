package com.goldenhour.storage;

import com.goldenhour.categories.Sales;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceiptHandler {
    public static void appendReceipt(String receiptText) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        File dir = new File("data/receipts");
        if (!dir.exists()) dir.mkdirs();
        String filename = "data/receipts/receipts_" + date + ".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(receiptText);
            bw.newLine();
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing receipt file.");
        }
    }

    /**
     * Write a sales receipt using the same style as other receipts.
     * outletCode may be null or empty; it's included in the receipt for traceability.
     * unitPrice is the per-unit price (for printing); subtotal is sale.getSubtotal().
     */
    public static void writeSalesReceipt(Sales sale, String outletCode, double unitPrice) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sales Receipt").append(System.lineSeparator());
        sb.append("Date: ").append(sale.getDate()).append(System.lineSeparator());
        sb.append("Time: ").append(sale.getTime()).append(System.lineSeparator());
        if (outletCode != null && !outletCode.isEmpty()) {
            sb.append("Outlet: ").append(outletCode).append(System.lineSeparator());
        }
        sb.append("Customer Name: ").append(sale.getCustomerName()).append(System.lineSeparator());
        sb.append("Model Purchased: ").append(sale.getModel()).append(System.lineSeparator());
        sb.append("Quantity: ").append(sale.getQuantity()).append(System.lineSeparator());
        sb.append("Unit Price: RM").append(unitPrice).append(System.lineSeparator());
        sb.append("Subtotal: RM").append(sale.getSubtotal()).append(System.lineSeparator());
        sb.append("Transaction Method: ").append(sale.getTransactionMethod()).append(System.lineSeparator());
        sb.append("Employee in Charge: ").append(sale.getEmployee()).append(System.lineSeparator());

        appendReceipt(sb.toString());
    }
}

