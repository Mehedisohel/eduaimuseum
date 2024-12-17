package edu;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreatorManagement extends JFrame {
    public CreatorManagement() {
        setTitle("Creator Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2)); 

        
        JLabel creatorIdLabel = new JLabel("Creator ID:");
        JTextField creatorIdField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField();
        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField();

       
        JButton addButton = new JButton("Add Creator");
        JButton updateButton = new JButton("Update Creator");
        JButton deleteButton = new JButton("Delete Creator");
        JButton viewButton = new JButton("View Creators");

       
        add(creatorIdLabel);
        add(creatorIdField);
        add(nameLabel);
        add(nameField);
        add(roleLabel);
        add(roleField);
        add(contactLabel);
        add(contactField);
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(viewButton);

       
        addButton.addActionListener(e -> handleCreator("add", creatorIdField, nameField, roleField, contactField));
        updateButton.addActionListener(e -> handleCreator("update", creatorIdField, nameField, roleField, contactField));
        deleteButton.addActionListener(e -> handleCreator("delete", creatorIdField, nameField, roleField, contactField));
        viewButton.addActionListener(e -> viewCreators());
    }

    private void handleCreator(String action, JTextField creatorIdField, JTextField nameField, JTextField roleField, JTextField contactField) {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query;
            PreparedStatement stmt;

            if ("add".equals(action)) {
                query = "INSERT INTO Creator (CreatorID, Name, Role, Contact) VALUES (?, ?, ?, ?)";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(creatorIdField.getText()));
                stmt.setString(2, nameField.getText());
                stmt.setString(3, roleField.getText());
                stmt.setString(4, contactField.getText());
            } else if ("update".equals(action)) {
                query = "UPDATE Creator SET Name = ?, Role = ?, Contact = ? WHERE CreatorID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, roleField.getText());
                stmt.setString(3, contactField.getText());
                stmt.setInt(4, Integer.parseInt(creatorIdField.getText()));
            } else {
                query = "DELETE FROM Creator WHERE CreatorID = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(creatorIdField.getText()));
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Creator " + action + "ed successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error " + action + "ing creator.");
        }
    }

    private void viewCreators() {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM Creator";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            JTextArea textArea = new JTextArea(10, 40);
            textArea.setEditable(false);
            while (rs.next()) {
                textArea.append("ID: " + rs.getInt("CreatorID") +
                        ", Name: " + rs.getString("Name") +
                        ", Role: " + rs.getString("Role") +
                        ", Contact: " + rs.getString("Contact") + "\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Creator Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving creator records.");
        }
    }
}
