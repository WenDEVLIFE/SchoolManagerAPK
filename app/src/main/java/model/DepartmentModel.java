package model;

public class DepartmentModel {

    String id;

    String departmentHead;

    public DepartmentModel(String id, String departmentHead) {
        this.id = id;
        this.departmentHead = departmentHead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    @Override
    public String toString() {
        return "DepartmentModel{" +
                "id='" + id + '\'' +
                ", departmentHead='" + departmentHead + '\'' +
                '}';
    }
}
