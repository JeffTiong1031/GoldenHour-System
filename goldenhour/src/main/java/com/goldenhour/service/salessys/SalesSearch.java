package com.goldenhour.service.salessys;

import com.goldenhour.categories.Sales;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * SalesSearch - search by date, customer, model, or employee.
 */
public class SalesSearch {

    private static final String SALES_CSV = "data/sales.csv";

    // Load all sales as Sales objects (skip header)
    public List<Sales> loadAllSales() {
        List<Sales> out = new ArrayList<>();
        Path p = Paths.get(SALES_CSV);
        if (!Files.exists(p)) return out;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                out.add(Sales.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading sales.csv: " + e.getMessage());
        }
        return out;
    }

    public List<Sales> searchByCustomer(String customerName) {
        List<Sales> res = new ArrayList<>();
        for (Sales s : loadAllSales()) {
            if (s.getCustomerName().equalsIgnoreCase(customerName)) res.add(s);
        }
        return res;
    }

    public List<Sales> searchByModel(String modelName) {
        List<Sales> res = new ArrayList<>();
        for (Sales s : loadAllSales()) {
            if (s.getModel().equalsIgnoreCase(modelName)) res.add(s);
        }
        return res;
    }

    public List<Sales> searchByDate(String date) {
        List<Sales> res = new ArrayList<>();
        for (Sales s : loadAllSales()) {
            if (s.getDate().equals(date)) res.add(s);
        }
        return res;
    }

    public List<Sales> searchByEmployee(String employeeName) {
        List<Sales> res = new ArrayList<>();
        for (Sales s : loadAllSales()) {
            if (s.getEmployee().equalsIgnoreCase(employeeName)) res.add(s);
        }
        return res;
    }

    // Print helper
    public void printSalesList(List<Sales> list) {
        if (list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (Sales s : list) {
            System.out.println("-----");
            System.out.println(s);
        }
    }
}
     

