package com.infosys.springboard.banksimulation.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConfig {

    private static Connection conn = null;

    static {
        try (InputStream input = DBConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) { // Load the properties file from resources

            Properties prop = new Properties(); // Create Properties object to read key-value pairs

            if (input != null) {
                // Load all properties from the file
                prop.load(input);

                // Read database configuration from properties file
                String url = prop.getProperty("spring.datasource.url");
                String user = prop.getProperty("spring.datasource.username");
                String password = prop.getProperty("spring.datasource.password");
                String driver = prop.getProperty("spring.datasource.driver-class-name");

                Class.forName(driver);

                // Establish the connection using DriverManager
                conn = DriverManager.getConnection(url, user, password);
            } else {
                System.out.println("Sorry, unable to find application.properties");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Public method to get the connection object
    public static Connection getConnection() throws SQLException {
        return conn;
    }
}
