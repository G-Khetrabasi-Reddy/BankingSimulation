package com.infosys.springboard.banksimulation.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConfig {

    // Static connection object – only one connection is created for the entire application
    private static Connection conn = null;

    // Static block executes once when the class is loaded
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

                // Load the JDBC driver class dynamically
                Class.forName(driver);

                // Establish the connection using DriverManager
                conn = DriverManager.getConnection(url, user, password);
            } else {
                // If the properties file is not found, print an error message
                System.out.println("Sorry, unable to find application.properties");
            }

        } catch (Exception ex) {
            // Print stack trace if any exception occurs (IO, SQL, ClassNotFound, etc.)
            ex.printStackTrace();
        }
    }

    // Public method to get the connection object
    // Other classes will use this method to interact with the database
    public static Connection getConnection() throws SQLException {
        return conn;
    }
}
