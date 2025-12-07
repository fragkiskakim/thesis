package client;

import model.CoordinateEntry;
import service.CoordinateService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class CRUDApplication2 {

    private final CoordinateService service = new CoordinateService();

    private JFrame frame;
    private JComboBox<String> coordinateTypeComboBox;
    private JTextField xCoordinateField;
    private JTextField yCoordinateField;
    private JTextField rCoordinateField;
    private JTextField thetaCoordinateField;
    private JTextField labelField;
    private JTable table;
    private DefaultTableModel tableModel;

    public CRUDApplication2() {

        frame = new JFrame("Cartesian / Polar Coordinate Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        coordinateTypeComboBox = new JComboBox<>(new String[]{"Cartesian", "Polar"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(coordinateTypeComboBox, gbc);

        // Cartesian fields
        gbc.gridx=0; gbc.gridy=1;
        inputPanel.add(new JLabel("X:"), gbc);
        gbc.gridx=1;
        xCoordinateField = new JTextField(10);
        inputPanel.add(xCoordinateField, gbc);

        gbc.gridx=2;
        inputPanel.add(new JLabel("Y:"), gbc);
        gbc.gridx=3;
        yCoordinateField = new JTextField(10);
        inputPanel.add(yCoordinateField, gbc);

        // Polar fields
        gbc.gridx=0; gbc.gridy=2;
        inputPanel.add(new JLabel("R:"), gbc);
        gbc.gridx=1;
        rCoordinateField = new JTextField(10);
        inputPanel.add(rCoordinateField, gbc);

        gbc.gridx=2;
        inputPanel.add(new JLabel("Theta:"), gbc);
        gbc.gridx=3;
        thetaCoordinateField = new JTextField(10);
        inputPanel.add(thetaCoordinateField, gbc);

        // Label
        gbc.gridx=0; gbc.gridy=3;
        inputPanel.add(new JLabel("Label:"), gbc);
        gbc.gridx=1;
        labelField = new JTextField(10);
        inputPanel.add(labelField, gbc);

        JButton addButton = new JButton("Add");
        gbc.gridx=1; gbc.gridy=4;
        inputPanel.add(addButton, gbc);

        JButton updateButton = new JButton("Update");
        gbc.gridx=2;
        inputPanel.add(updateButton, gbc);

        JButton deleteButton = new JButton("Delete");
        gbc.gridx=3;
        inputPanel.add(deleteButton, gbc);

        JButton readByLabelButton = new JButton("Read By Label");
        gbc.gridx=1; gbc.gridy=5;
        inputPanel.add(readByLabelButton, gbc);

        JButton showAllButton = new JButton("Show All");
        gbc.gridx=2;
        inputPanel.add(showAllButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Label", "X", "Y", "R", "Θ", "Timestamp"}, 0);

        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        showCartesianFields();
        coordinateTypeComboBox.addActionListener(e -> {
            if(coordinateTypeComboBox.getSelectedItem().equals("Cartesian"))
                showCartesianFields();
            else
                showPolarFields();
        });

        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteButton.addActionListener(e -> delete());
        readByLabelButton.addActionListener(e -> readByLabel());
        showAllButton.addActionListener(e -> load());

        load();
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
        List<CoordinateEntry> entries = service.loadAll();
        tableModel.setRowCount(0);

        for (CoordinateEntry e : entries) {
            tableModel.addRow(new Object[]{
                    e.getId(), e.getLabel(), e.getX(), e.getY(),
                    e.getR(), e.getTheta(), e.getTimestamp()
            });
        }
    }

    private void add() {
        String label = labelField.getText();
        if(label.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Label required");
            return;
        }

        try {
            if(coordinateTypeComboBox.getSelectedItem().equals("Cartesian")) {
                service.add("CARTESIAN",
                        Double.parseDouble(xCoordinateField.getText()),
                        Double.parseDouble(yCoordinateField.getText()),
                        label);
            } else {
                service.add("POLAR",
                        Double.parseDouble(rCoordinateField.getText()),
                        Double.parseDouble(thetaCoordinateField.getText()),
                        label);
            }
            load();

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input");
        }
    }

    private void update() {
        int row = table.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(frame, "Select row");
            return;
        }

        int id = (int)tableModel.getValueAt(row,0);
        String label = labelField.getText();

        try {
            if(coordinateTypeComboBox.getSelectedItem().equals("Cartesian")) {
                service.update(id,"CARTESIAN",
                        Double.parseDouble(xCoordinateField.getText()),
                        Double.parseDouble(yCoordinateField.getText()),
                        label);
            } else {
                service.update(id,"POLAR",
                        Double.parseDouble(rCoordinateField.getText()),
                        Double.parseDouble(thetaCoordinateField.getText()),
                        label);
            }

            load();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input");
        }
    }

    private void delete() {
        int row = table.getSelectedRow();
        if(row==-1) {
            JOptionPane.showMessageDialog(frame,"Select row");
            return;
        }

        int id = (int)tableModel.getValueAt(row,0);
        service.delete(id);
        load();
    }

    private void readByLabel() {
        String label=labelField.getText();
        if(label.isEmpty()) {
            JOptionPane.showMessageDialog(frame,"Enter label");
            return;
        }

        List<CoordinateEntry> entries = service.readByLabel(label);

        tableModel.setRowCount(0);
        for(CoordinateEntry e : entries) {
            tableModel.addRow(new Object[]{
                    e.getId(),e.getLabel(),e.getX(),e.getY(),
                    e.getR(),e.getTheta(),e.getTimestamp()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CRUDApplication2::new);
    }
}
