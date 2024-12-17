package edu;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EduAiMuseumApp extends JFrame {
    public EduAiMuseumApp() {
        setTitle("EDU AI Museum");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        JPanel mainPanel = new JPanel();
        JButton creatorButton = new JButton("Manage Creators");
        JButton sectionButton = new JButton("Manage Sections");
        JButton specimenButton = new JButton("Manage Specimens");
        JButton visitorButton = new JButton("Manage Visitors");
        JButton adminButton = new JButton("Manage Administrators");

       
        mainPanel.add(creatorButton);
        mainPanel.add(sectionButton);
        mainPanel.add(specimenButton);
        mainPanel.add(visitorButton);
        mainPanel.add(adminButton);

        
        add(mainPanel);

        
        creatorButton.addActionListener(e -> new CreatorManagement().setVisible(true));
        sectionButton.addActionListener(e -> new SectionManagement().setVisible(true));
        specimenButton.addActionListener(e -> new SpecimenManagement().setVisible(true));
        visitorButton.addActionListener(e -> new VisitorManagement().setVisible(true));
        adminButton.addActionListener(e -> new AdminManagement().setVisible(true));
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            if (performLogin()) { 
                JOptionPane.showMessageDialog(null, "Welcome to EDU AI Museum App!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                EduAiMuseumApp app = new EduAiMuseumApp();
                app.setLocationRelativeTo(null); 
                app.setVisible(true);
            } else {
                System.exit(0); 
            }
        });
    }

    private static boolean performLogin() {
        boolean authenticated = false;
        Connection connection = null;
        try {
            
            String username = JOptionPane.showInputDialog(null, "Enter Username:", "Login", JOptionPane.PLAIN_MESSAGE);
            if (username == null || username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return false; 
            }

            String password = JOptionPane.showInputDialog(null, "Enter Password:", "Login", JOptionPane.PLAIN_MESSAGE);
            if (password == null || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return false; 
            }

            
            connection = Databaseconnection.getConnection();
            String query = "SELECT * FROM loginsystem WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                authenticated = true; 
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while verifying credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return authenticated;
    }
}
