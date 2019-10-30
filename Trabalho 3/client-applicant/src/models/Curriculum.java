package models;

import java.io.Serializable;

public class Curriculum implements Serializable {
    private String name;
    private String contact;
    private String area;
    private int workload;
    private float intendedSalary;

    public Curriculum(String name, String contact, String area, int workload, float intendedSalary) {
        this.name = name;
        this.contact = contact;
        this.area = area;
        this.workload = workload;
        this.intendedSalary = intendedSalary;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public float getIntendedSalary() {
        return intendedSalary;
    }

    public void setIntendedSalary(float intendedSalary) {
        this.intendedSalary = intendedSalary;
    }
}
