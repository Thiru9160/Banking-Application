package bank.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn implements AutoCloseable {
    // Database connection parameters
    public Connection c;

    // Constructor to establish the connection
    public Conn() {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankmanagementsystem", "root", "Thirumalesh9160@");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }

    // Close the connection (if needed)
    @Override
    public void close() {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
