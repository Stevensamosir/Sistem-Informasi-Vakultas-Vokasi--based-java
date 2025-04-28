package uas;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class admin_galeriController implements Initializable {

    @FXML
    private ImageView imageView1;
    @FXML
    private Button openImageButton1;
    @FXML
    private Button saveImageButton1;

    @FXML
    private ImageView imageView2;
    @FXML
    private Button openImageButton2;
    @FXML
    private Button saveImageButton2;

    @FXML
    private ImageView imageView3;
    @FXML
    private Button openImageButton3;
    @FXML
    private Button saveImageButton3;

    @FXML
    private ImageView imageView4;
    @FXML
    private Button openImageButton4;
    @FXML
    private Button saveImageButton4;

    @FXML
    private ImageView imageView5;
    @FXML
    private Button openImageButton5;
    @FXML
    private Button saveImageButton5;

    @FXML
    private ImageView imageView6;
    @FXML
    private Button openImageButton6;
    @FXML
    private Button saveImageButton6;

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

    @FXML
    private void openImage1(ActionEvent event) {
        openImage(imageView1);
    }

    @FXML
    private void saveImage1(ActionEvent event) {
        saveImage(imageView1, 1);
    }

    @FXML
    private void openImage2(ActionEvent event) {
        openImage(imageView2);
    }

    @FXML
    private void saveImage2(ActionEvent event) {
        saveImage(imageView2, 2);
    }

    @FXML
    private void openImage3(ActionEvent event) {
        openImage(imageView3);
    }

    @FXML
    private void saveImage3(ActionEvent event) {
        saveImage(imageView3, 3);
    }

    @FXML
    private void openImage4(ActionEvent event) {
        openImage(imageView4);
    }

    @FXML
    private void saveImage4(ActionEvent event) {
        saveImage(imageView4, 4);
    }

    @FXML
    private void openImage5(ActionEvent event) {
        openImage(imageView5);
    }

    @FXML
    private void saveImage5(ActionEvent event) {
        saveImage(imageView5, 5);
    }

    @FXML
    private void openImage6(ActionEvent event) {
        openImage(imageView6);
    }

    @FXML
    private void saveImage6(ActionEvent event) {
        saveImage(imageView6, 6);
    }

    private void openImage(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                System.out.println("File selected: " + file.getAbsolutePath());
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open image.");
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    private void saveImage(ImageView imageView, int imageNumber) {
        if (imageView.getImage() != null) {
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                if (bufferedImage == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "BufferedImage conversion failed.");
                    return;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                if (imageBytes == null || imageBytes.length == 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to convert image to byte array.");
                    return;
                }

                System.out.println("Image bytes length: " + imageBytes.length);

                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String columnName = "gambar" + imageNumber;
                    String query = "UPDATE galeri SET " + columnName + " = ? WHERE id = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(query);
                    updateStmt.setBytes(1, imageBytes);
                    updateStmt.setInt(2, GALERI_ID);

                    int rowsAffected = updateStmt.executeUpdate();
                    System.out.println("Rows affected: " + rowsAffected);

                    if (rowsAffected > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Image saved successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the database.");
                    }
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save image: " + e.getMessage());
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

    @FXML
    private void editDashboard() {
        switchScene("admin_dashboardedit.fxml");
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
    private void switchToadmingaleri() {
        switchScene("admin_galeri.fxml");
    }

    @FXML
    private void switchToBerita() {
        switchScene("admin_berita.fxml");
    }

    @FXML
    private void switchToEmployees() {
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
