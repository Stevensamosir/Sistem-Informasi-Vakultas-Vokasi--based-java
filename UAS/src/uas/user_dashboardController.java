package uas;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class user_dashboardController {

    @FXML
    private TextArea deskripsiTextArea;

    @FXML
    private ImageView imageView;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public user_dashboardController() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Failed", "Failed to connect to the database.");
        }
    }

    @FXML
    public void initialize() {
        loadDashboardContent();
    }

    @FXML
    public void loadDashboardContent() {
        try {
            PreparedStatement selectStmt = connection.prepareStatement("SELECT kata_kata, gambar FROM Deskripsi WHERE id = ?");
            selectStmt.setInt(1, 1); // Replace with appropriate ID
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                String kata_kata = resultSet.getString("kata_kata");
                deskripsiTextArea.setText(kata_kata);
                
                // Load and display the image
                byte[] imageData = resultSet.getBytes("gambar");
                if (imageData != null) {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    imageView.setImage(image);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard content.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToDashboard() {
        switchScene("user_dashboard.fxml");
    }

    @FXML
    private void switchToD4TRPL() {
        switchScene("user_D4trpl.fxml");
    }

    @FXML
    private void switchToD3TI() {
        switchScene("user_D3ti.fxml");
    }

    @FXML
    private void switchToNews() {
        switchScene("user_berita.fxml");
    }
    @FXML
    private void switchToNews1() {
        switchScene("user_galeriuser.fxml");
    }
    
    @FXML
    private void switchToTeachers() {
        switchScene("user_tenagapengajar.fxml");
    }

    @FXML
    private void switchToD3TK() {
        switchScene("user_D3tk.fxml");
    }

    @FXML
    private void switchToProfile() {
        switchScene("loginregister.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            Parent newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage currentStage = (Stage) deskripsiTextArea.getScene().getWindow();
            currentStage.setScene(new Scene(newScene));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Scene Switch Failed", "Failed to switch scene to " + fxmlFile);
        }
    }
}
