package com.infosys.springboard.banksimulation;

import com.infosys.springboard.banksimulation.Config.DBConfig;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DBConfig.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connected successfully!");

                // Test query: show current database
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DATABASE();");

                if (rs.next()) {
                    System.out.println("Connected to Database: " + rs.getString(1));
                }

                conn.close(); // close connection after test
            } else {
                System.out.println("❌ Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
