package uas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class admin_tenagapengajarController {
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
    private TextField employeeIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField photoField;

    @FXML
    private javafx.scene.control.Button btnLogout;
    @FXML
    private javafx.scene.control.Button btnEmployees;
    @FXML
    private javafx.scene.control.Button btnBerita;

    private static final String DB_URL = "jdbc:mysql://localhost:3307/employee_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public admin_tenagapengajarController() {
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

        genderComboBox.getItems().addAll("Male", "Female", "Other");

        loadEmployees();

        // Add listener to the table to fill fields when a row is selected
        employeeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void fillFields(Employee employee) {
        employeeIdField.setText(employee.getEmployeeId());
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        genderComboBox.setValue(employee.getGender());
        phoneNumberField.setText(employee.getPhoneNumber());
        positionField.setText(employee.getPosition());
        dateField.setText(employee.getDate());
        photoField.setText(employee.getPhotoPath());
    }

    private void loadEmployees() {
        String query = "SELECT * FROM employees";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            employeeTable.getItems().clear();
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
                employeeTable.getItems().add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Failed", "Failed to load employees.");
        }
    }

    @FXML
    private void handleAdd() {
        String employeeId = employeeIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String gender = genderComboBox.getValue();
        String phoneNumber = phoneNumberField.getText();
        String position = positionField.getText();
        String date = dateField.getText();
        String photoPath = photoField.getText();

        if (employeeId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender == null || phoneNumber.isEmpty() || position.isEmpty() || date.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Add Failed", "All fields are required.");
            return;
        }

        String query = "INSERT INTO employees (employee_id, first_name, last_name, gender, phone_number, position, date, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employeeId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, gender);
            statement.setString(5, phoneNumber);
            statement.setString(6, position);
            statement.setString(7, date);
            statement.setString(8, photoPath);
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Add Success", "Employee added successfully.");
            clearFormFields();
            loadEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Add Failed", "Failed to add employee.");
        }
    }

    @FXML
    private void handleEdit() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        String employeeId = employeeIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String gender = genderComboBox.getValue();
        String phoneNumber = phoneNumberField.getText();
        String position = positionField.getText();
        String date = dateField.getText();
        String photoPath = photoField.getText();

        if (employeeId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender == null || phoneNumber.isEmpty() || position.isEmpty() || date.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Edit Failed", "All fields are required.");
            return;
        }

        String query = "UPDATE employees SET employee_id = ?, first_name = ?, last_name = ?, gender = ?, phone_number = ?, position = ?, date = ?, photo = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employeeId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, gender);
            statement.setString(5, phoneNumber);
            statement.setString(6, position);
            statement.setString(7, date);
            statement.setString(8, photoPath);
            statement.setInt(9, selectedEmployee.getId());
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Edit Success", "Employee data updated successfully.");
            clearFormFields();
            loadEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Edit Failed", "Failed to update employee data.");
        }
    }

    @FXML
    private void handleDelete() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.ERROR, "Delete Failed", "No employee selected.");
            return;
        }

        String query = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, selectedEmployee.getId());
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Delete Success", "Employee deleted successfully.");
            clearFormFields();
            loadEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete employee.");
        }
    }

    @FXML
    private void handleLogout() {
        switchScene("loginregister.fxml");
    }

    @FXML
    private void handleChoosePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            photoField.setText(selectedFile.getAbsolutePath());
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
        employeeIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        genderComboBox.setValue(null);
        phoneNumberField.clear();
        positionField.clear();
        dateField.clear();
        photoField.clear();
    }

    @FXML
    private void switchToDashboard() {
        switchScene("admin_dashboardedit.fxml");
    }
        @FXML
    void switchTogaleri() {
                switchScene("admin_galeri.fxml");
    }
    @FXML
    private void switchToD4TRPL() {
        switchScene("admin_D4.fxml");
    }

    @FXML
    private void switchToD3TI() {
        switchScene("admin_d3ti.fxml");
    }

    @FXML
    private void switchToD3TK() {
        switchScene("admin_d3tk.fxml");
    }

    @FXML
    private void switchToNews() {
        switchScene("admin_berita.fxml");
    }

    @FXML
    private void switchToTeachers() {
        switchScene("admin_tenagapengajar.fxml");
    }

    @FXML
    private void switchToProfile() {
        switchScene("admin_profile.fxml");
    }

    @FXML
    private void switchToBerita() {
        switchScene("admin_berita.fxml");
    }

    @FXML
    private void switchToEmployees() {
    }

    @FXML
    private void logout() {
        switchScene("loginregister.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) btnBerita.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Switch View Failed", "Failed to load " + fxmlFile + " view.");
        }
    }
}
