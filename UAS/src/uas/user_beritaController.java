package uas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class user_beritaController {

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
    private TableColumn<Berita, java.sql.Date> tanggalColumn; // Ensure this is of type java.sql.Date

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public user_beritaController() {
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

        // Set custom cell factory for foto column
        fotoColumn.setCellFactory(new Callback<TableColumn<Berita, String>, TableCell<Berita, String>>() {
            @Override
            public TableCell<Berita, String> call(TableColumn<Berita, String> param) {
                return new TableCell<Berita, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            try {
                                Image image = new Image(new FileInputStream(item));
                                imageView.setImage(image);
                                imageView.setFitHeight(50); // Set the height of the image
                                imageView.setFitWidth(50);  // Set the width of the image
                                setGraphic(imageView);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

        loadBerita();
    }

    private void loadBerita() {
        String query = "SELECT * FROM berita";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            ObservableList<Berita> beritaList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Berita berita = new Berita(
                        resultSet.getInt("id"),
                        resultSet.getString("judul"),
                        resultSet.getString("foto"),
                        resultSet.getString("konten"),
                        resultSet.getString("tanggal") // Correct type used here
                );
                beritaList.add(berita);
            }
            beritaTable.setItems(beritaList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load berita data.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Methods for scene switching...


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
            Stage currentStage = (Stage) beritaTable.getScene().getWindow();
            currentStage.setScene(new Scene(newScene));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Scene Switch Failed", "Failed to switch scene to " + fxmlFile);
        }
    }
}
