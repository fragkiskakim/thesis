import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.format.DateTimeFormatter;


public class CRUDApplication4 {
    private JFrame frame;
    private JComboBox<String> coordinateTypeComboBox;
    private JTextField xCoordinateField;
    private JTextField yCoordinateField;
    private JTextField rCoordinateField;
    private JTextField thetaCoordinateField;
    private JTextField labelField;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseFunctions4 dbFunctions = new DatabaseFunctions4();
    private auxFunctions4 auxFunctions = new auxFunctions4();

    private CoordinateType currentCoordinateType = CoordinateType.CARTESIAN;

    public CRUDApplication4() {
        frame = new JFrame("Cartesian/Polar Coordinate Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Coordinate type combo box
        coordinateTypeComboBox = new JComboBox<>(new String[]{"Cartesian", "Polar"});
        coordinateTypeComboBox.setSelectedIndex(0); // Default selection
        coordinateTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) coordinateTypeComboBox.getSelectedItem();
                if (selectedType.equals("Cartesian")) {
                    currentCoordinateType = CoordinateType.CARTESIAN;
                    showCartesianFields();
                } else if (selectedType.equals("Polar")) {
                    currentCoordinateType = CoordinateType.POLAR;
                    showPolarFields();
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(coordinateTypeComboBox, gbc);

        // Cartesian fields
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("X Coordinate:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        xCoordinateField = new JTextField(10);
        inputPanel.add(xCoordinateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Y Coordinate:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        yCoordinateField = new JTextField(10);
        inputPanel.add(yCoordinateField, gbc);

        // Polar fields
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("R Coordinate:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        rCoordinateField = new JTextField(10);
        inputPanel.add(rCoordinateField, gbc);


        gbc.gridx = 2;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Theta Coordinate:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        thetaCoordinateField = new JTextField(10);
        inputPanel.add(thetaCoordinateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Label:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        labelField = new JTextField(10);
        inputPanel.add(labelField, gbc);

        // Buttons
        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton addButton = new JButton("Add");
        inputPanel.add(addButton, gbc);

        gbc.gridx = 2;
        JButton updateButton = new JButton("Update");
        inputPanel.add(updateButton, gbc);

        gbc.gridx = 3;
        JButton deleteButton = new JButton("Delete");
        inputPanel.add(deleteButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton readByLabelButton = new JButton("Read by Label");
        inputPanel.add(readByLabelButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton showAllButton = new JButton("Show All");
        inputPanel.add(showAllButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Label" ,"X Coordinate", "Y Coordinate", "R Coordinate", "Theta Coordinate", "Datetime added or modified"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        load();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });

        readByLabelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readByLabel();
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });

        frame.setVisible(true);
    }

    private void showCartesianFields() {
        xCoordinateField.setVisible(true);
        yCoordinateField.setVisible(true);
        rCoordinateField.setVisible(false);
        thetaCoordinateField.setVisible(false);
    }

    private void showPolarFields() {
        xCoordinateField.setVisible(false);
        yCoordinateField.setVisible(false);
        rCoordinateField.setVisible(true);
        thetaCoordinateField.setVisible(true);
    }

    private void load() {
        try {
            dbFunctions.loadEntries();
            ResultSet resultSet = dbFunctions.loadEntries();

            tableModel.setRowCount(0);
            while (resultSet.next()) {
                // Retrieve the timestamp from the result set
                Timestamp timestamp = resultSet.getTimestamp("timestamp");

                // Format the timestamp to your desired string format
                String formattedDateTime = "";
                if (timestamp != null) {
                    formattedDateTime = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                tableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getDouble("xcoordinate"),
                        resultSet.getDouble("ycoordinate"),
                        resultSet.getDouble("rcoordinate"),
                        resultSet.getDouble("thetacoordinate"),
                        formattedDateTime
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void add() {
        String label = labelField.getText();
        if (label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Label field cannot be empty. Please enter a label.");
            return;
        }

        try {
                if(currentCoordinateType == CoordinateType.CARTESIAN){
                    double[] coordinates = auxFunctions.calculateCoordinates("CARTESIAN", Double.parseDouble(xCoordinateField.getText()), Double.parseDouble(yCoordinateField.getText()));
                    dbFunctions.addEntry(coordinates[0], coordinates[1], coordinates[2], coordinates[3], label);
                }else if(currentCoordinateType == CoordinateType.POLAR){
                    double[] coordinates = auxFunctions.calculateCoordinates("POLAR", Double.parseDouble(rCoordinateField.getText()), Double.parseDouble(thetaCoordinateField.getText()));
                    dbFunctions.addEntry(coordinates[0], coordinates[1], coordinates[2], coordinates[3], label);
                }
            
                load();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid coordinate format. Please enter valid numbers.");
        }
    }

    private void update() {
        String label = labelField.getText();
        if (label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Label field cannot be empty. Please enter a label.");
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to update");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            if (currentCoordinateType == CoordinateType.CARTESIAN) {
                double[] coordinates = auxFunctions.calculateCoordinates("CARTESIAN", Double.parseDouble(xCoordinateField.getText()), Double.parseDouble(yCoordinateField.getText()));
                dbFunctions.updateEntry(id, coordinates[0], coordinates[1], coordinates[2], coordinates[3], label);
            
            } else if (currentCoordinateType == CoordinateType.POLAR) {
                double[] coordinates = auxFunctions.calculateCoordinates("POLAR", Double.parseDouble(rCoordinateField.getText()), Double.parseDouble(thetaCoordinateField.getText()));
                dbFunctions.updateEntry(id, coordinates[0], coordinates[1], coordinates[2], coordinates[3], label);   
            }

            load();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid coordinate format. Please enter valid numbers.");
        }
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        dbFunctions.deleteEntry(id);
        load();
    }

    private void readByLabel() {
        String label = labelField.getText();
        if (label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a label");
            return;
        }

        try {
            ResultSet resultSet = dbFunctions.readByLabel(label);
            if(!resultSet.isBeforeFirst()){
                JOptionPane.showMessageDialog(frame, "No entry found with the given label");
                return;
            }
            tableModel.setRowCount(0); // Clear the table
            while(resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                String formattedDateTime = "";
                if (timestamp != null) {
                    formattedDateTime = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                tableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("label"),
                        resultSet.getDouble("xcoordinate"),
                        resultSet.getDouble("ycoordinate"),
                        resultSet.getDouble("rcoordinate"),
                        resultSet.getDouble("thetacoordinate"),
                        formattedDateTime
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CRUDApplication4();
            }
        });
    }
}
