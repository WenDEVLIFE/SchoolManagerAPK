package model;

public class CourseModel {

    String id;

    String courseName;

    String subjectNameAssigned;

    public CourseModel(String id, String courseName, String subjectNameAssigned) {
        this.id = id;
        this.courseName = courseName;
        this.subjectNameAssigned = subjectNameAssigned;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSubjectNameAssigned() {
        return subjectNameAssigned;
    }

    public void setSubjectNameAssigned(String subjectNameAssigned) {
        this.subjectNameAssigned = subjectNameAssigned;
    }

    @Override
    public String toString() {
        return "CourseModel{" +
                "id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", subjectNameAssigned='" + subjectNameAssigned + '\'' +
                '}';
    }
}
