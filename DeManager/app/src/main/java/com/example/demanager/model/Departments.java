package com.example.demanager.model;

public class Departments {
    private	int	id;
    private	String departmentname;


    public Departments(String departmentname) {
        this.departmentname = departmentname;

    }

    public Departments(int id, String departmentname) {
        this.id = id;
        this.departmentname = departmentname;

    }
    //json add
    public Departments() {

        this.departmentname = departmentname;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentname;
    }

    public void setDepartment(String departmentname) {
        this.departmentname = departmentname;
    }



}
