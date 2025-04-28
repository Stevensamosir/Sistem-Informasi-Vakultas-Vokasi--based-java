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


public class admin_dashboardeditController {

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Button openImageButton;

    @FXML
    private Button saveImageButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public admin_dashboardeditController() {
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
            descriptionTextArea.setText(kata_kata);
            
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


    @FXML
    public void saveDashboardContent() {
        String newKataKata = descriptionTextArea.getText();

        try {
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE Deskripsi SET kata_kata = ? WHERE id = ?");
            updateStmt.setString(1, newKataKata);
            updateStmt.setInt(2, 1); // Replace with appropriate ID
            updateStmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Dashboard content updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update dashboard content.");
        }
    }

    @FXML
    private void editDashboard() {
        descriptionTextArea.setEditable(true); // Mengaktifkan TextArea untuk diedit
        saveButton.setDisable(false); // Mengaktifkan tombol simpan
    }

    @FXML
    private void switchToD4TRPL() {
        switchScene("admin_D4.fxml");
    }

        @FXML
    private void switchTogaleri() {
        switchScene("admin_galeri.fxml");
    }
    
    @FXML
    private void switchToD3TI() {
        switchScene("admin_d3ti.fxml");
    }

    @FXML
    private void switchToBerita() {
        switchScene("admin_berita.fxml");
    }

    @FXML
    private void switchTogaleri1() {
        switchScene("admin_tenagapengajar.fxml");
    }

    @FXML
    private void switchToD3TK() {
        switchScene("admin_d3tk.fxml");
    }

    @FXML
    private void logout() {
        switchScene("loginregister.fxml");
    }

    @FXML
    private void openImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open image.");
            }
        }
    }

    @FXML
    private void saveImage(ActionEvent event) {
        if (imageView.getImage() != null) {
            try {
                // Convert Image to byte array
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Save image to database
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE Deskripsi SET gambar = ? WHERE id = ?");
                updateStmt.setBytes(1, imageBytes);
                updateStmt.setInt(2, 1); // Ganti dengan ID yang sesuai
                updateStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Image saved successfully.");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save image.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No image to save.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchScene(String fxmlFile) {
        try {
            Parent newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage currentStage = (Stage) descriptionTextArea.getScene().getWindow();
            currentStage.setScene(new Scene(newScene));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Scene Switch Failed", "Failed to switch scene to " + fxmlFile);
        }
    }
}
