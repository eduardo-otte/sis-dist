package models;

import java.io.Serializable;

public class JobOffering implements Serializable {
    private String companyName;
    private String contact;
    private String area;
    private int workload;
    private float salary;

    public JobOffering(String companyName, String contact, String area, int workload, float salary) {
        this.companyName = companyName;
        this.contact = contact;
        this.area = area;
        this.workload = workload;
        this.salary = salary;
    }

    // Getters
    public String getCompanyName() {
        return companyName;
    }

    public String getContact() {
        return contact;
    }

    public String getArea() {
        return area;
    }

    public int getWorkload() {
        return workload;
    }

    public float getSalary() {
        return salary;
    }

    // Setters

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
}
