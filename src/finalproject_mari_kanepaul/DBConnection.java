/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Kane
 */
public class DBConnection {
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            if (connection == null) {
                Properties props = new Properties();
                InputStream stream = DBConnection.class.getClassLoader().getResourceAsStream("db.properties");
                props.load(stream);

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
                );
            }
        } catch (ClassNotFoundException | SQLException | java.io.IOException e) {
            e.printStackTrace();
        }
        return connection; 
    }
    
}
