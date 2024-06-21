package com.upt.cti.dentalhub.Models;

public class Doctor {

    private int imageResource;
    private String name;
    private String specialization;
    private String schedule;
    private String phoneNumber;
    private String email;

    public Doctor(int imageResource, String name, String specialization, String schedule, String phoneNumber, String email) {

        this.imageResource = imageResource;
        this.name = name;
        this.specialization = specialization;
        this.schedule = schedule;
        this.phoneNumber = phoneNumber;
        this.email = email;

    }

    public int getImageResource() { return imageResource; }

    public void setImageResource(int imageResource) { this.imageResource = imageResource; }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSpecialization() {return specialization;}

    public void setSpecialization(String specialization) {this.specialization = specialization;}

    public String getSchedule() {return schedule;}

    public void setSchedule(String schedule) {this.schedule = schedule;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

}