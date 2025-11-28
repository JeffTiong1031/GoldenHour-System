package com.goldenhour.categories;

public class Sales {

    private String date;
    private String time;
    private String customerName;
    private String model;
    private int quantity;
    private double subtotal;
    private String transactionMethod;
    private String employee;

    public Sales(String date, String time, String customerName, String model,
                 int quantity, double subtotal, String transactionMethod, String employee) {
        this.date = date;
        this.time = time;
        this.customerName = customerName;
        this.model = model;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.transactionMethod = transactionMethod;
        this.employee = employee;
    }

    // Getters
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getCustomerName() { return customerName; }
    public String getModel() { return model; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return subtotal; }
    public String getTransactionMethod() { return transactionMethod; }
    public String getEmployee() { return employee; }

    // Setters (used by EditSales)
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setModel(String model) { this.model = model; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public void setTransactionMethod(String transactionMethod) { this.transactionMethod = transactionMethod; }
    public void setEmployee(String employee) { this.employee = employee; }

    public String toCSV() {
        return String.join(",",
                date,
                time,
                customerName,
                model,
                String.valueOf(quantity),
                String.valueOf(subtotal),
                transactionMethod,
                employee
        );
    }

    public static Sales fromCSV(String line) {
        String[] data = line.split(",", -1);
        // defensive default values
        String date = data.length > 0 ? data[0].trim() : "";
        String time = data.length > 1 ? data[1].trim() : "";
        String customer = data.length > 2 ? data[2].trim() : "";
        String model = data.length > 3 ? data[3].trim() : "";
        int qty = data.length > 4 && !data[4].trim().isEmpty() ? Integer.parseInt(data[4].trim()) : 0;
        double subtotal = data.length > 5 && !data[5].trim().isEmpty() ? Double.parseDouble(data[5].trim()) : 0.0;
        String method = data.length > 6 ? data[6].trim() : "";
        String employee = data.length > 7 ? data[7].trim() : "";
        return new Sales(date, time, customer, model, qty, subtotal, method, employee);
    }

    @Override
    public String toString() {
        return "Date: " + date +
                "\nTime: " + time +
                "\nCustomer: " + customerName +
                "\nModel: " + model +
                "\nQuantity: " + quantity +
                "\nTotal: RM" + subtotal +
                "\nMethod: " + transactionMethod +
                "\nEmployee: " + employee;
    }
}

