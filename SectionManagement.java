package edu;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionManagement extends JFrame {
    public SectionManagement() {
        setTitle("Section Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2)); // Adjusted grid layout to accommodate new fields

       
        JLabel sectionIdLabel = new JLabel("Section ID:");
        JTextField sectionIdField = new JTextField();
        JLabel nameLabel = new JLabel("Section Name:");
        JTextField nameField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();

       
        JButton addButton = new JButton("Add Section");
        JButton updateButton = new JButton("Update Section");
        JButton deleteButton = new JButton("Delete Section");
        JButton viewButton = new JButton("View Sections");

   
        add(sectionIdLabel);
        add(sectionIdField);
        add(nameLabel);
        add(nameField);
        add(descriptionLabel);
        add(descriptionField);
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(viewButton);

       
        addButton.addActionListener(e -> handleSection("add", sectionIdField, nameField, descriptionField));
        updateButton.addActionListener(e -> handleSection("update", sectionIdField, nameField, descriptionField));
        deleteButton.addActionListener(e -> handleSection("delete", sectionIdField, nameField, descriptionField));
        viewButton.addActionListener(e -> viewSections());
    }

    private void handleSection(String action, JTextField sectionIdField, JTextField nameField, JTextField descriptionField) {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query;
            PreparedStatement stmt;

            if ("add".equals(action)) {
                query = "INSERT INTO Section (SectionID, Name, Description) VALUES (?, ?, ?)";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(sectionIdField.getText()));
                stmt.setString(2, nameField.getText());
                stmt.setString(3, descriptionField.getText());
            } else if ("update".equals(action)) {
                query = "UPDATE Section SET Name = ?, Description = ? WHERE SectionID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, descriptionField.getText());
                stmt.setInt(3, Integer.parseInt(sectionIdField.getText()));
            } else {
                query = "DELETE FROM Section WHERE SectionID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(sectionIdField.getText()));
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Section " + action + "ed successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + action + "ing section.");
        }
    }

    private void viewSections() {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM Section";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            JTextArea textArea = new JTextArea(10, 40);
            textArea.setEditable(false);
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("SectionID") +
                        ", Name: " + rs.getString("Name") +
                        ", Description: " + rs.getString("Description") + "\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Section Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving section records.");
        }
    }
}
