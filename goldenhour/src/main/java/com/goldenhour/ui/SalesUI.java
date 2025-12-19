
package com.goldenhour.ui;

import com.goldenhour.service.salessys.SalesService;
import com.goldenhour.service.salessys.SalesSearch;
import com.goldenhour.service.salessys.EditSales;
import com.goldenhour.service.loginregister.AuthService;
import com.goldenhour.categories.Sales;

import java.util.List;
import java.util.Scanner;

public class SalesUI {
    private static final Scanner sc = new Scanner(System.in);
    private static final SalesService salesService = new SalesService();
    private static final SalesSearch salesSearch = new SalesSearch();
    private static final EditSales editSales = new EditSales();

    public static void openSalesMenu() {
        while (true) {
            System.out.println("\n=== Sales Management ===");
            System.out.println("1. Record New Sale");
            System.out.println("2. Search Sales");
            System.out.println("3. Edit Sale");
            System.out.println("4. Back");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    recordNewSale();
                    break;
                case "2":
                    searchSalesMenu();
                    break;
                case "3":
                    editSalesMenu();
                    break;
                case "4":
                    return; // Back to main menu
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    
    private static void recordNewSale() {
        if (AuthService.getCurrentUser() == null) {
            System.out.println("Error: No logged-in user.");
            return;
        }
        // Call your interactive service (which already prints the header)
        salesService.recordNewSale(AuthService.getCurrentUser());
    }

    private static void searchSalesMenu() {
        while (true) {
            System.out.println("\n=== Search Sales ===");
            System.out.println("1. By Date");
            System.out.println("2. By Customer Name");
            System.out.println("3. By Model Name");
            System.out.println("4. Back");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter Date (yyyy-MM-dd): ");
                    String date = sc.nextLine().trim();
                    List<Sales> byDate = salesSearch.searchByDate(date);
                    salesSearch.printSalesList(byDate);
                    break;
                case "2":
                    System.out.print("Enter Customer Name: ");
                    String customer = sc.nextLine().trim();
                    List<Sales> byCustomer = salesSearch.searchByCustomer(customer);
                    salesSearch.printSalesList(byCustomer);
                    break;
                case "3":
                    System.out.print("Enter Model Name: ");
                    String model = sc.nextLine().trim();
                    List<Sales> byModel = salesSearch.searchByModel(model);
                    salesSearch.printSalesList(byModel);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void editSalesMenu() {
        System.out.println("\n=== Edit Sale ===");
        editSales.editSaleInteractive();
    }
}

