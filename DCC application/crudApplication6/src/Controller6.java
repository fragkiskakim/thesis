import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller6 {
    private Model6 model = new Model6();
    private View6 view;

    public void setView(View6 view) {
        this.view = view;
    }

    public ResultSet readAll() throws SQLException {
        try {
            ResultSet resultSet = model.loadEntries();
            return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error reading all entries");
        }
    }

    public void add(CoordinateType6 currentCoordinateType ,Double coordinateA, Double coordinateB, String label){

        try {
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            double[] coordinates = convertCoordinates(currentCoordinateType, coordinateA, coordinateB);
            model.addEntry(coordinates[0], coordinates[1], coordinates[2], coordinates[3], formattedDateTime, label);
            view.refreshTable(readAll());

            
        } catch (Exception ex ) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");
        }
    }

    public void update(int id, CoordinateType6 currentCoordinateType, Double coordinateA, Double coordinateB, String label) {

        try {
            // Get current datetime
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);

            double[] coordinates = convertCoordinates(currentCoordinateType, coordinateA, coordinateB);
            model.updateEntry(id, coordinates[0], coordinates[1], coordinates[2], coordinates[3], formattedDateTime, label);

            view.refreshTable(readAll());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");
        }
    }

    public void delete(int id) {
        try {
            
            model.deleteEntry(id);
            view.refreshTable(readAll());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error deleting entry");
        }
    }

    public Boolean readByLabel(String label) throws SQLException {
        try {
            ResultSet resultSet = model.loadEntriesByLabel(label);
            if(!resultSet.isBeforeFirst()){
                return false;
            }

            view.refreshTable(resultSet);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error reading entries by label");
        }
    }

    public double[] convertCoordinates(CoordinateType6 coordinateType, Double coordinateA, Double coordinateB) {
        double xCoordinate = 0.0;
        double yCoordinate = 0.0;
        double rCoordinate = 0.0;
        double thetaCoordinate = 0.0;

        if (coordinateType == CoordinateType6.CARTESIAN) {
            xCoordinate = coordinateA;
            yCoordinate = coordinateB;
            rCoordinate = Math.sqrt(coordinateA * coordinateA + coordinateB * coordinateB);
            thetaCoordinate = Math.atan2(yCoordinate, xCoordinate);
        } else if (coordinateType == CoordinateType6.POLAR) {
            xCoordinate = coordinateA * Math.cos(Math.toRadians(coordinateB));
            yCoordinate = coordinateA * Math.sin(Math.toRadians(coordinateB));
            rCoordinate = coordinateA;
            thetaCoordinate = coordinateB;
        }

        return new double[] {xCoordinate, yCoordinate, rCoordinate, thetaCoordinate};
    }
}
