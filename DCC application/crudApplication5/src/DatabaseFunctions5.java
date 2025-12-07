import java.sql.*;

public class DatabaseFunctions5 {

    private DatabaseConnection5 dbConnection = new DatabaseConnection5();

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
    
    public void addEntry(Double xcoordinate, Double ycoordinate, Double rcoordinate, Double thetacoordinate, String formattedDateTime, String label){
        try {
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO coordinates (xcoordinate, ycoordinate, rcoordinate, thetacoordinate, timestamp, label) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setDouble(1, xcoordinate);
            statement.setDouble(2, ycoordinate);
            statement.setDouble(3, rcoordinate);
            statement.setDouble(4, thetacoordinate);
            statement.setString(5, formattedDateTime);
            statement.setString(6, label);
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error adding entry");
        }
    }

    public void updateEntry(int id, Double xcoordinate, Double ycoordinate, Double rcoordinate, Double thetacoordinate,  String formattedDateTime, String label) {

        try {
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE coordinates SET xcoordinate = ?, ycoordinate = ?, rcoordinate = ?, thetacoordinate = ?, timestamp = ?, label = ? WHERE id = ?"
            );
            statement.setDouble(1, xcoordinate);
            statement.setDouble(2, ycoordinate);
            statement.setDouble(3, rcoordinate);
            statement.setDouble(4, thetacoordinate);
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

