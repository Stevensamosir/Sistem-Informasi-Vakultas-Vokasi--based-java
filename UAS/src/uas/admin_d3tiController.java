package uas;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class admin_d3tiController {

    @FXML
    private Button btnEditDashboard;

    @FXML
    private Button btnD4TRPL;

    @FXML
    private Button btnD3TI;

    @FXML
    private Button btnD3TK;

    @FXML
    private Button btnBerita;

    @FXML
    private Button btnEmployees;

    @FXML
    private Button btnLogout;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField namaField;

    @FXML
    private TextField judulField;

    @FXML
    private TextArea sejarahTextArea;

    @FXML
    private TextArea visiTextArea;

    @FXML
    private TextArea misiTextArea;

    private Connection connection;

    public admin_d3tiController() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/employee_management", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Failed", "Failed to connect to the database.");
        }
    }

    @FXML
    private void initialize() {
        loadData();
    }

    @FXML
    void editDashboard() {
        switchScene("admin_dashboardedit.fxml");
    }

    @FXML
    void switchToD4TRPL() {
        switchScene("admin_D4.fxml");
    }
        @FXML
    void switchTogaleri() {
                switchScene("admin_galeri.fxml");
    }
    @FXML
    void switchToD3TI() {
    }

    @FXML
    void switchToD3TK() {
        switchScene("admin_d3tk.fxml");
    }

    @FXML
    void switchToBerita() {
        switchScene("admin_berita.fxml");
    }

    @FXML
    void switchToEmployees() {
        switchScene("admin_tenagapengajar.fxml");
    }

    @FXML
    void logout() {
        switchScene("loginregister.fxml");
    }

    @FXML
    private void openImage() {
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
    private void saveData() {
        String nama = namaField.getText();
        String judul = judulField.getText();
        String sejarah = sejarahTextArea.getText();
        String visi = visiTextArea.getText();
        String misi = misiTextArea.getText();

        if (imageView.getImage() != null) {
            try {
                // Convert Image to byte array
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Save data and image to database
                PreparedStatement updateStmt = connection.prepareStatement(
                    "UPDATE d3_ti SET nama = ?, judul = ?, sejarah = ?, visi = ?, misi = ?, foto = ? WHERE id = 1"
                );
                updateStmt.setString(1, nama);
                updateStmt.setString(2, judul);
                updateStmt.setString(3, sejarah);
                updateStmt.setString(4, visi);
                updateStmt.setString(5, misi);
                updateStmt.setBytes(6, imageBytes);
                updateStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully.");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No image to save.");
        }
    }

    @FXML
    public void loadData() {
        try {
            PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM d3_ti WHERE id = 1");
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                String nama = resultSet.getString("nama");
                String judul = resultSet.getString("judul");
                String sejarah = resultSet.getString("sejarah");
                String visi = resultSet.getString("visi");
                String misi = resultSet.getString("misi");

                // Display data
                namaField.setText(nama);
                judulField.setText(judul);
                sejarahTextArea.setText(sejarah);
                visiTextArea.setText(visi);
                misiTextArea.setText(misi);

                // Load and display the image
                byte[] imageData = resultSet.getBytes("foto");
                if (imageData != null) {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    imageView.setImage(image);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data.");
        }
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) btnEditDashboard.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the scene.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
