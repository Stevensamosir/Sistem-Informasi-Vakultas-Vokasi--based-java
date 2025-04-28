package uas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;

public class admin_beritaController {

    @FXML
    private TableView<Berita> beritaTable;
    @FXML
    private TableColumn<Berita, Integer> idColumn;
    @FXML
    private TableColumn<Berita, String> judulColumn;
    @FXML
    private TableColumn<Berita, String> fotoColumn;
    @FXML
    private TableColumn<Berita, String> kontenColumn;
    @FXML
    private TableColumn<Berita, String> tanggalColumn;

    @FXML
    private TextField judulField;
    @FXML
    private TextField fotoField;
    @FXML
    private TextArea kontenField; // Changed to TextArea
    @FXML
    private TextField tanggalField;

    @FXML
    private javafx.scene.control.Button btnLogout;
    @FXML
    private javafx.scene.control.Button btnAddBerita;
    @FXML
    private javafx.scene.control.Button btnEditBerita;
    @FXML
    private javafx.scene.control.Button btnDeleteBerita;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public admin_beritaController() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Failed", "Failed to connect to the database.");
        }
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        judulColumn.setCellValueFactory(new PropertyValueFactory<>("judul"));
        fotoColumn.setCellValueFactory(new PropertyValueFactory<>("foto"));
        kontenColumn.setCellValueFactory(new PropertyValueFactory<>("konten"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        loadBerita();

        beritaTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void fillFields(Berita berita) {
        judulField.setText(berita.getJudul());
        fotoField.setText(berita.getFoto());
        kontenField.setText(berita.getKonten());
        tanggalField.setText(berita.getTanggal());
    }

    private void loadBerita() {
        String query = "SELECT * FROM berita";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            beritaTable.getItems().clear();
            while (resultSet.next()) {
                Berita berita = new Berita(
                        resultSet.getInt("id"),
                        resultSet.getString("judul"),
                        resultSet.getString("foto"),
                        resultSet.getString("konten"),
                        resultSet.getString("tanggal")
                );
                beritaTable.getItems().add(berita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Failed", "Failed to load berita.");
        }
    }

    @FXML
    private void handleAdd() {
        String judul = judulField.getText();
        String foto = fotoField.getText();
        String konten = kontenField.getText();
        String tanggal = tanggalField.getText();

        if (judul.isEmpty() || tanggal.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Add Failed", "Judul and Tanggal fields are required.");
            return;
        }

        String query = "INSERT INTO berita (judul, foto, konten, tanggal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, judul);
            statement.setString(2, foto);
            statement.setString(3, konten);
            statement.setString(4, tanggal);
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Add Success", "Berita added successfully.");
            clearFormFields();
            loadBerita();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Add Failed", "Failed to add berita.");
        }
    }

    @FXML
    private void handleEdit() {
        Berita selectedBerita = beritaTable.getSelectionModel().getSelectedItem();
        String judul = judulField.getText();
        String foto = fotoField.getText();
        String konten = kontenField.getText();
        String tanggal = tanggalField.getText();

        if (judul.isEmpty() || tanggal.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Edit Failed", "Judul and Tanggal fields are required.");
            return;
        }

        String query = "UPDATE berita SET judul = ?, foto = ?, konten = ?, tanggal = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, judul);
            statement.setString(2, foto);
            statement.setString(3, konten);
            statement.setString(4, tanggal);
            statement.setInt(5, selectedBerita.getId());
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Edit Success", "Berita updated successfully.");
            clearFormFields();
            loadBerita();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Edit Failed", "Failed to update berita.");
        }
    }

    @FXML
    private void handleDelete() {
        Berita selectedBerita = beritaTable.getSelectionModel().getSelectedItem();
        if (selectedBerita == null) {
            showAlert(Alert.AlertType.ERROR, "Delete Failed", "No berita selected.");
            return;
        }

        String query = "DELETE FROM berita WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedBerita.getId());
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Delete Success", "Berita deleted successfully.");
            clearFormFields();
            loadBerita();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete berita.");
        }
    }

    @FXML
    void logout() {
        switchScene("loginregister.fxml");
    }

    @FXML
    void switchToDashboard() {
        switchScene("admin_dashboardedit.fxml");
    }

    @FXML
    void switchToD4TRPL() {
        switchScene("admin_D4.fxml");
    }

    @FXML
    void switchToD3TI() {
        switchScene("admin_d3ti.fxml");
    }

    @FXML
    void switchToD3TK() {
        switchScene("admin_d3tk.fxml");
    }

    @FXML
    void switchToBerita() {
    }

    @FXML
    void switchToEmployees() {
        switchScene("admin_tenagapengajar.fxml");
    }    
    @FXML
    void switchToadmingaleri() {
        switchScene("admin_galeri.fxml");
    }

    @FXML
    private void handleChoosePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fotoField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFormFields() {
        judulField.clear();
        fotoField.clear();
        kontenField.clear();
        tanggalField.clear();
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Switch View Failed", "Failed to load " + fxmlFile + " view.");
        }
    }
}
