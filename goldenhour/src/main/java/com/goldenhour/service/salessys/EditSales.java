package com.goldenhour.service.salessys;

import com.goldenhour.categories.Sales;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * EditSales - interactive edit of sales record identified by date+customer+model.
 */
public class EditSales {

    private static final String SALES_CSV = "data/sales.csv";
    private final Scanner sc = new Scanner(System.in);

    public void editSaleInteractive() {
        Path p = Paths.get(SALES_CSV);
        if (!Files.exists(p)) {
            System.out.println("sales.csv not found.");
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(p);
        } catch (IOException e) {
            System.out.println("Error reading sales.csv: " + e.getMessage());
            return;
        }

        // Prompt identification
        System.out.print("Enter transaction date (yyyy-MM-dd): ");
        String date = sc.nextLine().trim();
        System.out.print("Enter customer name: ");
        String customer = sc.nextLine().trim();
        System.out.print("Enter model name: ");
        String model = sc.nextLine().trim();

        int foundIdx = -1;
        for (int i = 1; i < lines.size(); i++) { // skip header line 0
            String[] f = lines.get(i).split(",", -1);
            if (f.length < 8) continue;
            String d = f[0].trim();
            String c = f[2].trim();
            String m = f[3].trim();
            if (d.equals(date) && c.equalsIgnoreCase(customer) && m.equalsIgnoreCase(model)) {
                foundIdx = i;
                break;
            }
        }

        if (foundIdx == -1) {
            System.out.println("Sale record not found.");
            return;
        }

        String[] fields = lines.get(foundIdx).split(",", -1);
        Sales sale = Sales.fromCSV(String.join(",", fields));

        System.out.println("\n=== Sale Found ===");
        System.out.println(sale);

        System.out.println("\nSelect field to edit:");
        System.out.println("1. Customer Name");
        System.out.println("2. Model");
        System.out.println("3. Quantity");
        System.out.println("4. Total Price (subtotal)");
        System.out.println("5. Transaction Method");
        System.out.print("> ");

        int choice = 0;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1 -> {
                System.out.print("New Customer Name: ");
                sale.setCustomerName(sc.nextLine().trim());
            }
            case 2 -> {
                System.out.print("New Model: ");
                sale.setModel(sc.nextLine().trim());
            }
            case 3 -> {
                System.out.print("New Quantity: ");
                try {
                    sale.setQuantity(Integer.parseInt(sc.nextLine().trim()));
                } catch (Exception ex) {
                    System.out.println("Invalid number.");
                    return;
                }
            }
            case 4 -> {
                System.out.print("New Total Price (RM): ");
                try {
                    sale.setSubtotal(Double.parseDouble(sc.nextLine().trim()));
                } catch (Exception ex) {
                    System.out.println("Invalid number.");
                    return;
                }
            }
            case 5 -> {
                System.out.print("New Transaction Method: ");
                sale.setTransactionMethod(sc.nextLine().trim());
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        // replace line
        lines.set(foundIdx, sale.toCSV());

        try {
            Files.write(p, lines);
            System.out.println("Sale record updated successfully!");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}


