import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class View6 {
    private JFrame frame;
    private JComboBox<String> coordinateTypeComboBox;
    private JTextField xCoordinateField;
    private JTextField yCoordinateField;
    private JTextField rCoordinateField;
    private JTextField thetaCoordinateField;
    private JTextField labelField;
    private JTable table;
    private DefaultTableModel tableModel;

    private CoordinateType6 currentCoordinateType = CoordinateType6.CARTESIAN;

    //private controller6 controller = new controller6();
    private Controller6 controller;

    public void setController(Controller6 controller) {
        this.controller = controller;
    }

    public View6() {
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
                    currentCoordinateType = CoordinateType6.CARTESIAN;
                    showCartesianFields();
                } else if (selectedType.equals("Polar")) {
                    currentCoordinateType = CoordinateType6.POLAR;
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

        //load();

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

    public void refreshTable(ResultSet resultSet) {
        tableModel.setRowCount(0); // Clear the table
        try {
            while (resultSet.next()) {
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

    private void load() {
        try {
            ResultSet resultSet = controller.readAll();

            refreshTable(resultSet);
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
                if(currentCoordinateType == CoordinateType6.CARTESIAN){
                    controller.add(currentCoordinateType, Double.parseDouble(xCoordinateField.getText()), Double.parseDouble(yCoordinateField.getText()), label);
                }else if(currentCoordinateType == CoordinateType6.POLAR){
                    controller.add(currentCoordinateType, Double.parseDouble(rCoordinateField.getText()), Double.parseDouble(thetaCoordinateField.getText()), label);
                }

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
            if (currentCoordinateType == CoordinateType6.CARTESIAN) {
                controller.update(id, currentCoordinateType ,Double.parseDouble(xCoordinateField.getText()), Double.parseDouble(yCoordinateField.getText()), label);
            } else if (currentCoordinateType == CoordinateType6.POLAR) {
                controller.update(id,currentCoordinateType, Double.parseDouble(rCoordinateField.getText()), Double.parseDouble(thetaCoordinateField.getText()), label);
            }

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
        controller.delete(id);
    }

    private void readByLabel() {
        String label = labelField.getText();
        if (label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a label");
            return;
        }

        try {
            Boolean entryFound = controller.readByLabel(label);
            if(!entryFound){
                JOptionPane.showMessageDialog(frame, "No entry found with the given label");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View6 crudApp = new View6();
                Controller6 controller = new Controller6();

                crudApp.setController(controller);
                controller.setView(crudApp);
                crudApp.load();
            }
        });
    }
}
