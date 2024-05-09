package com.upt.cti.dentalhub;

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

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
