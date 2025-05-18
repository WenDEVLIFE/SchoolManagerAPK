package model;

public class GradeModel {

    String enrollmentID;

    String gradeID;

    String StudentName;

    String SubjectName;

    boolean isEnrolled;

    public GradeModel(String enrollmentID, String gradeID, String studentName, String subjectName, boolean isEnrolled) {
        this.enrollmentID = enrollmentID;
        this.gradeID = gradeID;
        StudentName = studentName;
        SubjectName = subjectName;
        this.isEnrolled = isEnrolled;
    }

    public String getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(String enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public String getGradeID() {
        return gradeID;
    }

    public void setGradeID(String gradeID) {
        this.gradeID = gradeID;
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
                "enrollmentID='" + enrollmentID + '\'' +
                ", gradeID='" + gradeID + '\'' +
                ", StudentName='" + StudentName + '\'' +
                ", SubjectName='" + SubjectName + '\'' +
                ", isEnrolled=" + isEnrolled +
                '}';
    }




}
