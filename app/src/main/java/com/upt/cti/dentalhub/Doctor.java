package com.upt.cti.dentalhub;

public class Doctor {
    private String name;
    private String specialization;
    private String schedule;
    private String phoneNumber;
    private String email;
    private int image;

    public Doctor(int image, String name, String specialization, String schedule, String phoneNumber, String email) {

        this.image = image;
        this.name = name;
        this.specialization = specialization;
        this.schedule = schedule;
        this.phoneNumber = phoneNumber;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
