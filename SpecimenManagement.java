package edu;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecimenManagement extends JFrame {
    public SpecimenManagement() {
        setTitle("Specimen Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        JLabel specimenIdLabel = new JLabel("Specimen ID:");
        JTextField specimenIdField = new JTextField();
        JLabel nameLabel = new JLabel("Specimen Name:");
        JTextField nameField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        JLabel creationDateLabel = new JLabel("Creation Date (YYYY-MM-DD):");
        JTextField creationDateField = new JTextField();
        JLabel sectionIdLabel = new JLabel("Section ID:");
        JTextField sectionIdField = new JTextField();

        JButton addButton = new JButton("Add Specimen");
        JButton updateButton = new JButton("Update Specimen");
        JButton deleteButton = new JButton("Delete Specimen");
        JButton viewButton = new JButton("View Specimens");

        add(specimenIdLabel);
        add(specimenIdField);
        add(nameLabel);
        add(nameField);
        add(descriptionLabel);
        add(descriptionField);
        add(creationDateLabel);
        add(creationDateField);
        add(sectionIdLabel);
        add(sectionIdField);
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(viewButton);

        addButton.addActionListener(e -> handleSpecimen("add", specimenIdField, nameField, descriptionField, creationDateField, sectionIdField));
        updateButton.addActionListener(e -> handleSpecimen("update", specimenIdField, nameField, descriptionField, creationDateField, sectionIdField));
        deleteButton.addActionListener(e -> handleSpecimen("delete", specimenIdField, nameField, descriptionField, creationDateField, sectionIdField));
        viewButton.addActionListener(e -> viewSpecimens());
    }

    private void handleSpecimen(String action, JTextField specimenIdField, JTextField nameField, JTextField descriptionField, JTextField creationDateField, JTextField sectionIdField) {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query;
            PreparedStatement stmt;

            if ("add".equals(action)) {
                query = "INSERT INTO Specimen (SpecimenID, Name, Description, CreationDate, SectionID) VALUES (?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(specimenIdField.getText()));
                stmt.setString(2, nameField.getText());
                stmt.setString(3, descriptionField.getText());
                stmt.setString(4, creationDateField.getText());
                stmt.setInt(5, Integer.parseInt(sectionIdField.getText()));
            } else if ("update".equals(action)) {
                query = "UPDATE Specimen SET Name = ?, Description = ?, CreationDate = ?, SectionID = ? WHERE SpecimenID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, descriptionField.getText());
                stmt.setString(3, creationDateField.getText());
                stmt.setInt(4, Integer.parseInt(sectionIdField.getText()));
                stmt.setInt(5, Integer.parseInt(specimenIdField.getText()));
            } else {
                query = "DELETE FROM Specimen WHERE SpecimenID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(specimenIdField.getText()));
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Specimen " + action + "ed successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + action + "ing specimen.");
        }
    }

    private void viewSpecimens() {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM Specimen";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            JTextArea textArea = new JTextArea(10, 30);
            textArea.setEditable(false);
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("SpecimenID") +
                        ", Name: " + rs.getString("Name") +
                        ", Description: " + rs.getString("Description") +
                        ", Creation Date: " + rs.getString("CreationDate") +
                        ", Section ID: " + rs.getInt("SectionID") + "\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Specimen Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving specimen records.");
        }
    }
}
