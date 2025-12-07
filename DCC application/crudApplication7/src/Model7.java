import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Model7 {

    private DatabaseConnection7 dbConnection = new DatabaseConnection7();

    public double[] convertCoordinates(CoordinateType7 coordinateType, Double coordinateA, Double coordinateB) {
        double xCoordinate = 0.0;
        double yCoordinate = 0.0;
        double rCoordinate = 0.0;
        double thetaCoordinate = 0.0;

        if (coordinateType == CoordinateType7.CARTESIAN) {
            xCoordinate = coordinateA;
            yCoordinate = coordinateB;
            rCoordinate = Math.sqrt(coordinateA * coordinateA + coordinateB * coordinateB);
            thetaCoordinate = Math.atan2(yCoordinate, xCoordinate);
        } else if (coordinateType == CoordinateType7.POLAR) {
            xCoordinate = coordinateA * Math.cos(Math.toRadians(coordinateB));
            yCoordinate = coordinateA * Math.sin(Math.toRadians(coordinateB));
            rCoordinate = coordinateA;
            thetaCoordinate = coordinateB;
        }

        return new double[] {xCoordinate, yCoordinate, rCoordinate, thetaCoordinate};
    }

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
    
    public void addEntry(CoordinateType7 currentCoordinateType ,Double coordinateA, Double coordinateB, String label){
        try {
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);

            double[] coordinates = convertCoordinates(currentCoordinateType, coordinateA, coordinateB);

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
    }

    public void updateEntry(int id, CoordinateType7 currentCoordinateType, Double coordinateA, Double coordinateB, String label) {

        try {
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);

            double[] coordinates = convertCoordinates(currentCoordinateType, coordinateA, coordinateB);

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

    public ResultSet loadEntriesByLabel(String label) throws SQLException {
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

