package edu;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginSystem extends JFrame {
    public LoginSystem() {
        setTitle("EDU Museum Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty cell
        add(loginButton);

        loginButton.addActionListener(e -> authenticate(usernameField.getText(), new String(passwordField.getPassword())));
    }

    private void authenticate(String username, String password) {
        try (Connection connection = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM loginsystem WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome to EDU AI Museum.");
                dispose();
                new EduAiMuseumApp().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginSystem().setVisible(true));
    }
}
