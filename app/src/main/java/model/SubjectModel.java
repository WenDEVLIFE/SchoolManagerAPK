package model;

public class SubjectModel {

    String id;

    String subjectName;

    String scheduleDate;

    String professorName;

    public SubjectModel(String id, String subjectName, String scheduleDate, String professorName) {
        this.id = id;
        this.subjectName = subjectName;
        this.scheduleDate = scheduleDate;
        this.professorName = professorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    @Override
    public String toString() {
        return "SubjectModel{" +
                "id='" + id + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", scheduleDate='" + scheduleDate + '\'' +
                ", professorName='" + professorName + '\'' +
                '}';
    }
}
