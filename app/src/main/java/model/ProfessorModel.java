package model;

public class ProfessorModel {

    String id;

    String professorName;

    String nameOfDepartment;



    public ProfessorModel(String id, String professorName, String nameOfDepartment) {
        this.id = id;
        this.professorName = professorName;
        this.nameOfDepartment = nameOfDepartment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getNameOfDepartment() {
        return nameOfDepartment;
    }

    public void setNameOfDepartment(String nameOfDepartment) {
        this.nameOfDepartment = nameOfDepartment;
    }

    @Override
    public String toString() {
        return "ProfessorModel{" +
                "id='" + id + '\'' +
                ", professorName='" + professorName + '\'' +
                ", nameOfDepartment='" + nameOfDepartment + '\'' +
                '}';
    }


}
