package edu;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminManagement extends JFrame {
    public AdminManagement() {
        setTitle("Admin Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2)); // Adjusted grid layout

        JLabel adminIdLabel = new JLabel("Admin ID:");
        JTextField adminIdField = new JTextField();
        JLabel startDateLabel = new JLabel("Start Date:");
        JTextField startDateField = new JTextField(); // Add Start Date field
        JLabel endDateLabel = new JLabel("End Date:");
        JTextField endDateField = new JTextField();   // Add End Date field
        JLabel professorIdLabel = new JLabel("Professor ID:");
        JTextField professorIdField = new JTextField(); // Add Professor ID field

        JButton addButton = new JButton("Add Admin");
        JButton updateButton = new JButton("Update Admin");
        JButton deleteButton = new JButton("Delete Admin");
        JButton viewButton = new JButton("View Admins");

        add(adminIdLabel);
        add(adminIdField);
        add(startDateLabel);
        add(startDateField); // Add Start Date field to the UI
        add(endDateLabel);
        add(endDateField);   // Add End Date field to the UI
        add(professorIdLabel);
        add(professorIdField); // Add Professor ID field to the UI
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(viewButton);

        addButton.addActionListener(e -> handleAdmin("add", adminIdField, startDateField, endDateField, professorIdField));
        updateButton.addActionListener(e -> handleAdmin("update", adminIdField, startDateField, endDateField, professorIdField));
        deleteButton.addActionListener(e -> handleAdmin("delete", adminIdField, startDateField, endDateField, professorIdField));
        viewButton.addActionListener(e -> viewAdmins());
    }

    private void handleAdmin(String action, JTextField adminIdField, JTextField startDateField, JTextField endDateField, JTextField professorIdField) {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query;
            PreparedStatement stmt;

            if ("add".equals(action)) {
                query = "INSERT INTO administrator (AdminID, StartDate, EndDate, ProfessorID) VALUES (?, ?, ?, ?)";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(adminIdField.getText()));
                stmt.setString(2, startDateField.getText()); // Set Start Date
                stmt.setString(3, endDateField.getText());   // Set End Date
                stmt.setInt(4, Integer.parseInt(professorIdField.getText())); // Set Professor ID
            } else if ("update".equals(action)) {
                query = "UPDATE administrator SET StartDate = ?, EndDate = ?, ProfessorID = ? WHERE AdminID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, startDateField.getText());  // Update Start Date
                stmt.setString(2, endDateField.getText());    // Update End Date
                stmt.setInt(3, Integer.parseInt(professorIdField.getText())); // Update Professor ID
                stmt.setInt(4, Integer.parseInt(adminIdField.getText()));
            } else {
                query = "DELETE FROM administrator WHERE AdminID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(adminIdField.getText()));
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Admin " + action + "ed successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + action + "ing admin.");
        }
    }

    private void viewAdmins() {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM administrator";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            JTextArea textArea = new JTextArea(10, 30);
            textArea.setEditable(false);
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("AdminID") +
                        ", Start Date: " + rs.getString("StartDate") +
                        ", End Date: " + rs.getString("EndDate") +
                        ", Professor ID: " + rs.getInt("ProfessorID") + "\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Administrator Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving admin records.");
        }
    }
}
