package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection2 {
    private static final String URL = "jdbc:mysql://localhost:3306/crud_db";
    private static final String USER = "username";  // MySQL username
    private static final String PASSWORD = "root"; // MySQL password
    private Connection connection = null;

    public void setConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
