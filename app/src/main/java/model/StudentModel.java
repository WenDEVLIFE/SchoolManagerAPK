package model;

public class StudentModel {
    private String id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String email;
    private String phoneNumber;
    private String gender;
    private String selectedCourse;

    public StudentModel(String id, String firstName, String lastName, String birthdate,
                        String email, String phoneNumber, String gender, String selectedCourse) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.selectedCourse = selectedCourse;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getSelectedCourse() {
        return selectedCourse;
    }
}