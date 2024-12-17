package edu;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitorManagement extends JFrame {
    public VisitorManagement() {
        setTitle("Visitor Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

       
        JLabel visitorIdLabel = new JLabel("Visitor ID:");
        JTextField visitorIdField = new JTextField();
        JLabel nameLabel = new JLabel("Visitor Name:");
        JTextField nameField = new JTextField();
        JLabel nidLabel = new JLabel("NID:");
        JTextField nidField = new JTextField();
        JLabel visitDateLabel = new JLabel("Visit Date (YYYY-MM-DD):");
        JTextField visitDateField = new JTextField();

        
        JButton addButton = new JButton("Add Visitor");
        JButton updateButton = new JButton("Update Visitor");
        JButton deleteButton = new JButton("Delete Visitor");
        JButton viewButton = new JButton("View Visitors");

      
        add(visitorIdLabel);
        add(visitorIdField);
        add(nameLabel);
        add(nameField);
        add(nidLabel);
        add(nidField);
        add(visitDateLabel);
        add(visitDateField);
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(viewButton);

       
        addButton.addActionListener(e -> handleVisitor("add", visitorIdField, nameField, nidField, visitDateField));
        updateButton.addActionListener(e -> handleVisitor("update", visitorIdField, nameField, nidField, visitDateField));
        deleteButton.addActionListener(e -> handleVisitor("delete", visitorIdField, nameField, nidField, visitDateField));
        viewButton.addActionListener(e -> viewVisitors());
    }

    private void handleVisitor(String action, JTextField visitorIdField, JTextField nameField, JTextField nidField, JTextField visitDateField) {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query;
            PreparedStatement stmt;

            if ("add".equals(action)) {
                query = "INSERT INTO Visitor (VisitorID, Name, NID, VisitDate) VALUES (?, ?, ?, ?)";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(visitorIdField.getText()));
                stmt.setString(2, nameField.getText());
                stmt.setString(3, nidField.getText());
                stmt.setString(4, visitDateField.getText());
            } else if ("update".equals(action)) {
                query = "UPDATE Visitor SET Name = ?, NID = ?, VisitDate = ? WHERE VisitorID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, nidField.getText());
                stmt.setString(3, visitDateField.getText());
                stmt.setInt(4, Integer.parseInt(visitorIdField.getText()));
            } else {
                query = "DELETE FROM Visitor WHERE VisitorID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(visitorIdField.getText()));
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Visitor " + action + "ed successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + action + "ing visitor.");
        }
    }

    private void viewVisitors() {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM Visitor";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            JTextArea textArea = new JTextArea(10, 40);
            textArea.setEditable(false);
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("VisitorID") +
                        ", Name: " + rs.getString("Name") +
                        ", NID: " + rs.getString("NID") +
                        ", Visit Date: " + rs.getString("VisitDate") + "\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Visitor Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving visitor records.");
        }
    }
}
