package model;

public class GradeModel {

    String id;

    String StudentName;

    String SubjectName;

    boolean isEnrolled;


    public GradeModel(String id, String studentName, String subjectName, boolean isEnrolled) {
        this.id = id;
        StudentName = studentName;
        SubjectName = subjectName;
        this.isEnrolled = isEnrolled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStudentName() {
        return StudentName;
    }


    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    @Override
    public String toString() {
        return "GradeModel{" +
                "id='" + id + '\'' +
                ", StudentName='" + StudentName + '\'' +
                ", SubjectName='" + SubjectName + '\'' +
                ", isEnrolled=" + isEnrolled +
                '}';
    }

}
