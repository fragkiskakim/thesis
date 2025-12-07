import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class auxFunctions5 {
    private DatabaseFunctions5 dbFunctions = new DatabaseFunctions5();

    public ResultSet readAll() throws SQLException {
        try {
            ResultSet resultSet = dbFunctions.loadEntries();
            return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error reading all entries");
        }
    }

    public void add(String coordinateType, Double coordinateA, Double coordinateB, String label){

        try {
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            double[] coordinates = calculateCoordinates(coordinateType, coordinateA, coordinateB);
            dbFunctions.addEntry(coordinates[0], coordinates[1], coordinates[2], coordinates[3], formattedDateTime, label);

            
        } catch (Exception ex ) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");
        }
    }

    public void update(int id, String coordinateType, Double coordinateA, Double coordinateB, String label) {

        try {
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            double[] coordinates = calculateCoordinates(coordinateType , coordinateA, coordinateB);
            dbFunctions.updateEntry(id, coordinates[0], coordinates[1], coordinates[2], coordinates[3], formattedDateTime, label);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");
        }
    }

    public void delete(int id) {
        try {
            
            dbFunctions.deleteEntry(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error deleting entry");
        }
    }

    public ResultSet readByLabel(String label) throws SQLException {
        try {
            ResultSet resultSet = dbFunctions.loadEntriesByLabel(label);
            return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error reading entries by label");
        }
    }

    public double[] calculateCoordinates(String coordinateType, Double coordinateA, Double coordinateB) {
        double xCoordinate = 0.0;
        double yCoordinate = 0.0;
        double rCoordinate = 0.0;
        double thetaCoordinate = 0.0;

        if (coordinateType.equals("CARTESIAN")) {
            xCoordinate = coordinateA;
            yCoordinate = coordinateB;
            rCoordinate = Math.sqrt(coordinateA * coordinateA + coordinateB * coordinateB);
            thetaCoordinate = Math.atan2(yCoordinate, xCoordinate);
        } else if (coordinateType.equals("POLAR")) {
            xCoordinate = coordinateA * Math.cos(Math.toRadians(coordinateB));
            yCoordinate = coordinateA * Math.sin(Math.toRadians(coordinateB));
            rCoordinate = coordinateA;
            thetaCoordinate = coordinateB;
        }

        return new double[] {xCoordinate, yCoordinate, rCoordinate, thetaCoordinate};
    }
}
