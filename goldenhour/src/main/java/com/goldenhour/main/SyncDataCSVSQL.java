package com.goldenhour.main;

import com.goldenhour.categories.*;
import com.goldenhour.storage.CSVHandler;
import com.goldenhour.storage.DatabaseHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SyncDataCSVSQL {

    public static void main(String[] args) {
        System.out.println("Starting Migration from CSV to SQLite...");
        
        // 1. Create Tables
        DatabaseHandler.initializeDatabase();

        // 2. Migrate Employees
        List<Employee> employees = CSVHandler.readEmployees(); // Uses your existing CSV code
        String sqlEmp = "INSERT OR IGNORE INTO employees(id, name, role, password) VALUES(?,?,?,?)";
        
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlEmp)) {
            
            conn.setAutoCommit(false); // Speed up bulk inserts
            for (Employee e : employees) {
                pstmt.setString(1, e.getId());
                pstmt.setString(2, e.getName());
                pstmt.setString(3, e.getRole());
                pstmt.setString(4, e.getPassword());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("✅ Migrated " + employees.size() + " Employees.");
        } catch (SQLException e) { e.printStackTrace(); }

        // 3. Migrate Outlets
        List<Outlet> outlets = CSVHandler.readOutlets();
        String sqlOutlet = "INSERT OR IGNORE INTO outlets(code, name) VALUES(?,?)";
        
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlOutlet)) {
            conn.setAutoCommit(false);
            for (Outlet o : outlets) {
                pstmt.setString(1, o.getOutletCode());
                pstmt.setString(2, o.getOutletName());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("✅ Migrated " + outlets.size() + " Outlets.");
        } catch (SQLException e) { e.printStackTrace(); }

        // 4. Migrate Models & Stock (The Tricky Part)
        List<Model> models = CSVHandler.readStock();
        String sqlModel = "INSERT OR IGNORE INTO models(code, price) VALUES(?,?)";
        String sqlStock = "INSERT OR IGNORE INTO stock(model_code, outlet_code, quantity) VALUES(?,?,?)";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement psModel = conn.prepareStatement(sqlModel);
             PreparedStatement psStock = conn.prepareStatement(sqlStock)) {
            
            conn.setAutoCommit(false);
            for (Model m : models) {
                // A. Insert Model
                psModel.setString(1, m.getModelCode());
                psModel.setDouble(2, m.getPrice());
                psModel.addBatch();

                // B. Insert Stock (Loop through the map)
                Map<String, Integer> stockMap = m.getStockPerOutlet();
                for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
                    psStock.setString(1, m.getModelCode());
                    psStock.setString(2, entry.getKey()); // Outlet Code
                    psStock.setInt(3, entry.getValue());  // Quantity
                    psStock.addBatch();
                }
            }
            psModel.executeBatch();
            psStock.executeBatch();
            conn.commit();
            System.out.println("✅ Migrated " + models.size() + " Models and their Stock.");
        } catch (SQLException e) { e.printStackTrace(); }
        
        // 5. Migrate Sales
        List<Sales> sales = CSVHandler.readSales();
        String sqlSale = "INSERT INTO sales(date, time, customer_name, model_code, quantity, subtotal, method, employee_name) VALUES(?,?,?,?,?,?,?,?)";
        
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlSale)) {
            
            conn.setAutoCommit(false);
            for (Sales s : sales) {
                pstmt.setString(1, s.getDate());
                pstmt.setString(2, s.getTime());
                pstmt.setString(3, s.getCustomerName());
                pstmt.setString(4, s.getModel());
                pstmt.setInt(5, s.getQuantity());
                pstmt.setDouble(6, s.getSubtotal());
                pstmt.setString(7, s.getTransactionMethod());
                pstmt.setString(8, s.getEmployee());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("✅ Migrated " + sales.size() + " Sales Records.");
        } catch (SQLException e) { e.printStackTrace(); }

        // 6. ✅ MIGRATE ATTENDANCE (Add this!)
        // Make sure CSVHandler has a readAttendance() method first!
        List<Attendance> attendanceList = CSVHandler.readAttendance();
        String sqlAttendance = "INSERT INTO attendance(emp_id, emp_name, date, clock_in, clock_out, hours_worked) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlAttendance)) {

            conn.setAutoCommit(false);
            for (Attendance a : attendanceList) {
                pstmt.setString(1, a.getEmployeeId());
                pstmt.setString(2, a.getEmployeeName());
                pstmt.setString(3, a.getDate());
                pstmt.setString(4, a.getClockInTime());
                pstmt.setString(5, a.getClockOutTime());
                pstmt.setDouble(6, a.getHoursWorked());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("✅ Migrated " + attendanceList.size() + " Attendance Records.");
        } catch (SQLException e) { 
            System.out.println("❌ Error migrating attendance: " + e.getMessage());
            e.printStackTrace(); 
        }

        System.out.println("🎉 MIGRATION COMPLETE! You can now use SQLite.");
    }
}