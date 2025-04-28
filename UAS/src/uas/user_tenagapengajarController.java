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

public class user_tenagapengajarController {
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    @FXML
    private TableColumn<Employee, String> genderColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> dateColumn;
    @FXML
    private TableColumn<Employee, String> photoColumn;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public user_tenagapengajarController() {
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
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoPath"));

        // Set custom cell factory for photo column
        photoColumn.setCellFactory(new Callback<TableColumn<Employee, String>, TableCell<Employee, String>>() {
            @Override
            public TableCell<Employee, String> call(TableColumn<Employee, String> param) {
                return new TableCell<Employee, String>() {
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

        loadEmployees();
    }

    private void loadEmployees() {
        String query = "SELECT * FROM employees";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            ObservableList<Employee> employees = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("position"),
                        resultSet.getString("date"),
                        resultSet.getString("photo")
                );
                employees.add(employee);
            }
            employeeTable.setItems(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employee data.");
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
    private void switchToD3TK() {
        switchScene("user_D3tk.fxml");
    }

    @FXML
    private void switchToProfile() {
        switchScene("loginregister.fxml");
    }
        @FXML
    private void switchToTeachers() {
    }

    
    private void switchScene(String fxmlFile) {
        try {
            Parent newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage currentStage = (Stage) employeeTable.getScene().getWindow();
            currentStage.setScene(new Scene(newScene));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Scene Switch Failed", "Failed to switch scene to " + fxmlFile);
        }
    }
}
