package org.bank;

import org.bank.config.DBConfig;
import org.bank.config.DBSetup;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Connection conn;
        try{
            conn = DBConfig.getConnection();
            if(conn != null){
                System.out.println("Database Connection is Successful..!");
            }
            DBSetup.setCustomerAutoIncrement();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
