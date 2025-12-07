package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection0 {
    private static final String URL = "jdbc:mysql://localhost:3306/crud_db";
    private static final String USER = "username";  // MySQL username
    private static final String PASSWORD = "root"; // MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
