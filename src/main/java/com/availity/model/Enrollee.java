package com.availity.model;

import java.util.Objects;

public class Enrollee implements Comparable<Enrollee> {

    private String userId;
    private String name;
    private Integer version;
    private String insuranceCompany;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    @Override
    public String toString() {
        return "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", insuranceCompany='" + insuranceCompany + "\n";
    }

    @Override
    public int compareTo(Enrollee enrollee) {
        if (getName() == null || enrollee.getName() == null) {
            return 0;
        }
        return getName().compareTo(enrollee.getName());
    }
}
