import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseFunctions3 {

    private DatabaseConnection3 dbConnection = new DatabaseConnection3();

    private auxFunctions3 auxFunctions = new auxFunctions3();

    public ResultSet loadEntries(){
        try {
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM coordinates");

            return resultSet;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error loading entries");
        }
    }
    
    public void addEntry(String coordinateType, Double coordinateA, Double coordinateB, String label){

        try {
            double[] coordinates = auxFunctions.calculateCoordinates(coordinateType, coordinateA, coordinateB);

            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);

            try {
                dbConnection.setConnection();
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO coordinates (xcoordinate, ycoordinate, rcoordinate, thetacoordinate, timestamp, label) VALUES (?, ?, ?, ?, ?, ?)"
                );
                statement.setDouble(1, coordinates[0]);
                statement.setDouble(2, coordinates[1]);
                statement.setDouble(3, coordinates[2]);
                statement.setDouble(4, coordinates[3]);
                statement.setString(5, formattedDateTime);
                statement.setString(6, label);
                statement.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error adding entry");
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");  
        }
    }

    public void updateEntry(int id, String coordinateType, Double coordinateA, Double coordinateB, String label) {

        try {
            double[] coordinates = auxFunctions.calculateCoordinates(coordinateType, coordinateA, coordinateB);
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);

            try {
                dbConnection.setConnection();
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE coordinates SET xcoordinate = ?, ycoordinate = ?, rcoordinate = ?, thetacoordinate = ?, timestamp = ?, label = ? WHERE id = ?"
                );
                statement.setDouble(1, coordinates[0]);
                statement.setDouble(2, coordinates[1]);
                statement.setDouble(3, coordinates[2]);
                statement.setDouble(4, coordinates[3]);
                statement.setString(5, formattedDateTime);
                statement.setString(6, label);
                statement.setInt(7, id);
                statement.executeUpdate();

                
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error updating entry");
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");
        }
    }

    public void deleteEntry(int id) {
        try {
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM coordinates WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error deleting entry");
        }
    }

    public ResultSet readByLabel(String label) throws SQLException {
        try{
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            String query = "SELECT * FROM coordinates WHERE label = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, label);
            ResultSet resultSet = statement.executeQuery();
            
            return resultSet;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error reading by label");
        }
    }
}

