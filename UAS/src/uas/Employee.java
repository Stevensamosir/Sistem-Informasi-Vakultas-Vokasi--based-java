package uas;

import javafx.beans.property.*;

public class Employee {

    private final IntegerProperty id;
    private final StringProperty employeeId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty gender;
    private final StringProperty phoneNumber;
    private final StringProperty position;
    private final StringProperty date;
    private final StringProperty photoPath; 

    public Employee(int id, String employeeId, String firstName, String lastName, String gender, String phoneNumber, String position, String date, String photoPath) {
        this.id = new SimpleIntegerProperty(id);
        this.employeeId = new SimpleStringProperty(employeeId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.gender = new SimpleStringProperty(gender);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.position = new SimpleStringProperty(position);
        this.date = new SimpleStringProperty(date);
        this.photoPath = new SimpleStringProperty(photoPath); 
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty positionProperty() {
        return position;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty photoPathProperty() {
        return photoPath; 
    }

    public int getId() {
        return id.get();
    }

    public String getEmployeeId() {
        return employeeId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getPosition() {
        return position.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getPhotoPath() {
        return photoPath.get(); 
    }
}
