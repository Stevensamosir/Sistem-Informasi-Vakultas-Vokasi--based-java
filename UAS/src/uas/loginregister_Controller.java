package uas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginregister_Controller {
    @FXML
    private AnchorPane login_form;
    @FXML
    private AnchorPane register_form;
    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerPassword1;
    @FXML
    private TextField registerPhone;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    
    
    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public loginregister_Controller() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private Label registerLabel;
    @FXML
    private Label loginLabel;

    @FXML
    private void initialize() {
        showLoginForm();
    }

    @FXML
    private void showLoginForm() {
        login_form.setVisible(true);
        register_form.setVisible(false);
    }

    @FXML
    private void showRegisterForm() {
        login_form.setVisible(false);
        register_form.setVisible(true);
    }

    @FXML
    private void registerAccount() {
        String usernameInput = registerUsername.getText();
        String passwordInput = registerPassword.getText();
        String confirmPasswordInput = registerPassword1.getText();
        String phoneNumberInput = registerPhone.getText();

        if (usernameInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty() || phoneNumberInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields are required.");
            return;
        }

        if (!passwordInput.equals(confirmPasswordInput)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
            return;
        }

        try {
            boolean registrationSuccessful = registerUser(usernameInput, passwordInput, phoneNumberInput);
            if (registrationSuccessful) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Success", "User registered successfully.");
                registerUsername.clear();
                registerPassword.clear();
                registerPassword1.clear();
                registerPhone.clear();
                showLoginForm(); // Automatically switch to login form after successful registration
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred during registration.");
        }
    }

        @FXML
    private void loginAccount() {
        String username = this.username.getText();
        String password = this.password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username or Password cannot be empty.");
            return;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if ("admin".equalsIgnoreCase(role)) {
                    loadDashboard("admin_dashboardedit.fxml", "Admin Dashboard");
                } else {
                    loadDashboard("user_dashboard.fxml", "User Dashboard");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Failed", "An error occurred during login.");
        }
    }
    
        private void loadDashboard(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
    }
    
    
    private boolean registerUser(String username, String password, String phoneNumber) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, phone_number, full_name, role) VALUES (?, ?, ?, ?, 'user')")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, phoneNumber);
            statement.setString(4, "Default Name"); // Replace "Default Name" with appropriate value
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    private boolean loginUser(String username, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) username.getScene().getWindow();
        stage.close();
    }
}
