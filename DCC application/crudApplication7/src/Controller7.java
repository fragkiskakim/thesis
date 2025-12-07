import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller7 {
    private Model7 model = new Model7();
    private View7 view;

    public void setView(View7 view) {
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

    public void add(CoordinateType7 currentCoordinateType ,Double coordinateA, Double coordinateB, String label){

        try {
            model.addEntry(currentCoordinateType, coordinateA, coordinateB, label);
            view.refreshTable(readAll());

            
        } catch (Exception ex ) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid coordinate format. Please enter valid numbers.");
        }
    }

    public void update(int id, CoordinateType7 currentCoordinateType, Double coordinateA, Double coordinateB, String label) {

        try {
            model.updateEntry(id, currentCoordinateType, coordinateA, coordinateB, label);

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
}
