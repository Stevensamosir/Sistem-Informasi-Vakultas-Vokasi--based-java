package uas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class User_galeriuserController implements Initializable {

    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;
    @FXML
    private ImageView imageView5;
    @FXML
    private ImageView imageView6;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final int GALERI_ID = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImages();
    }

    private void loadImages() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT gambar1, gambar2, gambar3, gambar4, gambar5, gambar6 FROM galeri WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, GALERI_ID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                setImageView(resultSet.getBytes("gambar1"), imageView1);
                setImageView(resultSet.getBytes("gambar2"), imageView2);
                setImageView(resultSet.getBytes("gambar3"), imageView3);
                setImageView(resultSet.getBytes("gambar4"), imageView4);
                setImageView(resultSet.getBytes("gambar5"), imageView5);
                setImageView(resultSet.getBytes("gambar6"), imageView6);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load images from database.");
        }
    }

    private void setImageView(byte[] imageBytes, ImageView imageView) {
        if (imageBytes != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            Image image = new Image(inputStream);
            imageView.setImage(image);
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
    private void switchToGallery() {
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
            Stage currentStage = (Stage) imageView1.getScene().getWindow();
            currentStage.setScene(new Scene(newScene));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Scene Switch Failed", "Failed to switch scene to " + fxmlFile);
        }
    }
}
