package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import database.DatabaseConnection1;

public class CRUDApplication1 {
    private JFrame frame;
    private JComboBox<String> coordinateTypeComboBox;
    private JTextField xCoordinateField;
    private JTextField yCoordinateField;
    private JTextField rCoordinateField;
    private JTextField thetaCoordinateField;
    private JTextField labelField;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseConnection1 dbConnection = new DatabaseConnection1();

    private enum CoordinateType {
        CARTESIAN, POLAR
    }

    private CoordinateType currentCoordinateType = CoordinateType.CARTESIAN;

    public CRUDApplication1() {
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
        tableModel = new DefaultTableModel(new String[]{"ID", "Label", "X Coordinate", "Y Coordinate", "R Coordinate", "Theta Coordinate", "Datetime added or modified"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        loadEntries();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEntry();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEntry();
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
                loadEntries();
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

    private void loadEntries() {
        try {
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM coordinates");

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

    private void addEntry() {
        String label = labelField.getText();
        if (label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Label field cannot be empty. Please enter a label.");
            return;
        }

        double xCoordinate = 0.0;
        double yCoordinate = 0.0;
        double rCoordinate = 0.0;
        double thetaCoordinate = 0.0;

        try {
            if (currentCoordinateType == CoordinateType.CARTESIAN) {
                xCoordinate = Double.parseDouble(xCoordinateField.getText());
                yCoordinate = Double.parseDouble(yCoordinateField.getText());
                rCoordinate = Math.sqrt(xCoordinate * xCoordinate + yCoordinate * yCoordinate);
                thetaCoordinate = Math.atan2(yCoordinate, xCoordinate);
            } else if (currentCoordinateType == CoordinateType.POLAR) {
                rCoordinate = Double.parseDouble(rCoordinateField.getText());
                thetaCoordinate = Double.parseDouble(thetaCoordinateField.getText());
                xCoordinate = rCoordinate * Math.cos(Math.toRadians(thetaCoordinate));
                yCoordinate = rCoordinate * Math.sin(Math.toRadians(thetaCoordinate));
            }

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
                statement.setDouble(1, xCoordinate);
                statement.setDouble(2, yCoordinate);
                statement.setDouble(3, rCoordinate);
                statement.setDouble(4, thetaCoordinate);
                statement.setString(5, formattedDateTime);
                statement.setString(6, label);
                statement.executeUpdate();

                loadEntries();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid coordinate format. Please enter valid numbers.");
        }
    }

    private void updateEntry() {
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
        double xCoordinate = 0.0;
        double yCoordinate = 0.0;
        double rCoordinate = 0.0;
        double thetaCoordinate = 0.0;

        try {
            if (currentCoordinateType == CoordinateType.CARTESIAN) {
                xCoordinate = Double.parseDouble(xCoordinateField.getText());
                yCoordinate = Double.parseDouble(yCoordinateField.getText());
                rCoordinate = Math.sqrt(xCoordinate * xCoordinate + yCoordinate * yCoordinate);
                thetaCoordinate = Math.atan2(yCoordinate, xCoordinate);
            } else if (currentCoordinateType == CoordinateType.POLAR) {
                rCoordinate = Double.parseDouble(rCoordinateField.getText());
                thetaCoordinate = Double.parseDouble(thetaCoordinateField.getText());
                xCoordinate = rCoordinate * Math.cos(Math.toRadians(thetaCoordinate));
                yCoordinate = rCoordinate * Math.sin(Math.toRadians(thetaCoordinate));
            }

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
                statement.setDouble(1, xCoordinate);
                statement.setDouble(2, yCoordinate);
                statement.setDouble(3, rCoordinate);
                statement.setDouble(4, thetaCoordinate);
                statement.setString(5, formattedDateTime);
                statement.setString(6, label);
                statement.setInt(7, id);
                statement.executeUpdate();

                loadEntries();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid coordinate format. Please enter valid numbers.");
        }
    }

    private void deleteEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            dbConnection.setConnection();
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM coordinates WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();

            loadEntries();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readByLabel() {
        String label = labelField.getText();
        if (label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a label");
            return;
        }

        try {
            ResultSet resultSet;
            try{
                dbConnection.setConnection();
                Connection connection = dbConnection.getConnection();
                String query = "SELECT * FROM coordinates WHERE label = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, label);
                resultSet = statement.executeQuery();
                
            }
            catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error reading by label");
            }
            
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
                new CRUDApplication1();
            }
        });
    }
}
