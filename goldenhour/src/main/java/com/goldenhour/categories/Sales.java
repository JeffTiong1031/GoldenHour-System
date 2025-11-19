package com.goldenhour.categories;

public class Sales {
    private String customerName;
    private String itemPurchased;
    private String transactionMethod;
    private double price;

    public Sales(String customerName, String itemPurchased, String transactionMethod, double price) {
        this.customerName = customerName;
        this.itemPurchased = itemPurchased;
        this.transactionMethod = transactionMethod;
        this.price = price;
    }

    public String getCustomerName() { return customerName; };
    public String getItem() { return itemPurchased; };
    public String getTransactionMethod() { return transactionMethod; };
    public double getPrice() { return price; };

    public String toCSV() {
        return String.join(",", customerName, itemPurchased, transactionMethod, String.valueOf(price));
    }

    public static Sales fromCSV(String line) {
        String [] data = line.split(",");
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }
        Sales sale = new Sales(data[0], data[1], data[2], Double.parseDouble(data[3]));
        return sale;
    }
}
