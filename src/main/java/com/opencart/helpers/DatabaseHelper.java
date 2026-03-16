package com.opencart.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseHelper {
    public static void resetLoginAttempt() {
        try {
            String url = "jdbc:mysql://localhost:3306/opencart_db";
            String user = "root";
            String password = "root";

            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("TRUNCATE TABLE oc_customer_login");

            stmt.close();
            conn.close();

            System.out.println("Reset login attempt thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
