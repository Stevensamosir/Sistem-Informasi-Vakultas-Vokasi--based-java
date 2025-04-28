package uas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.stage.Stage;

public class user_D3tiController implements Initializable {

    @FXML
    private Button btn_dashboard;
    @FXML
    private Button btn_d4trpl;
    @FXML
    private Button btn_d3ti;
    @FXML
    private Button btn_d3tk;
    @FXML
    private Button btn_news;
    @FXML
    private Button btn_teachers;
    @FXML
    private Button btn_profile;

    @FXML
    private TextArea sejarahTextArea;

    @FXML
    private ImageView imageView;

    @FXML
    private Label namaLabel;

    @FXML
    private Label judulLabel;

    @FXML
    private Label visiLabel;

    @FXML
    private Label misiLabel;

    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/employee_management", "root", "");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection error
        }
    }

    private void loadData() {
        try {
            PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM d3_ti WHERE id = 1");
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                String nama = resultSet.getString("nama");
                String judul = resultSet.getString("judul");
                String sejarah = resultSet.getString("sejarah");
                String visi = resultSet.getString("visi");
                String misi = resultSet.getString("misi");

                // Load and display the image
                byte[] imageData = resultSet.getBytes("foto");
                if (imageData != null) {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    imageView.setImage(image);
                }

                // Set data to UI elements
                namaLabel.setText(nama);
                judulLabel.setText(judul);
                sejarahTextArea.setText(sejarah);
                visiLabel.setText(visi);
                misiLabel.setText(misi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }

    @FXML
    private void switchToD4TRPL(ActionEvent event) {
        switchScene("user_D4trpl.fxml", event);
    }

    @FXML
    private void switchToDashboard(ActionEvent event) {
        switchScene("user_dashboard.fxml", event);
    }

    @FXML
    private void switchToD3TI(ActionEvent event) {
    }

    @FXML
    private void switchToD3TK(ActionEvent event) {
        switchScene("user_D3tk.fxml", event);
    }

    @FXML
    private void switchToNews(ActionEvent event) {
        switchScene("user_berita.fxml", event);
    }
    @FXML
    private void switchToNews1(ActionEvent event) {
        switchScene("user_galeriuser.fxml", event);
    }
    @FXML
    private void switchToTeachers(ActionEvent event) {
        switchScene("user_tenagapengajar.fxml", event);
    }

    @FXML
    private void switchToProfile(ActionEvent event) {
        switchScene("loginregister.fxml", event);
    }

    private void switchScene(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btn_dashboard.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
