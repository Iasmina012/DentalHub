package com.upt.cti.dentalhub;

public class Appointment {

    private String appointmentId;
    private String doctor;
    private String service;
    private String date;
    private String time;
    private String insurance;
    private String userId;
    private String location;

    public Appointment() {
        //Constructor necesar pentru Firebase
    }

    public Appointment(String appointmentId, String doctor, String service, String date, String time, String insurance, String userId, String location) {

        this.appointmentId = appointmentId;
        this.doctor = doctor;
        this.service = service;
        this.date = date;
        this.time = time;
        this.insurance = insurance;
        this.userId = userId;
        this.location = location;

    }

    public String getAppointmentId() {return appointmentId;}

    public void setAppointmentId(String appointmentId) {this.appointmentId = appointmentId;}

    public String getDoctor() { return doctor; }

    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getService() { return service; }

    public void setService(String service) { this.service = service; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getInsurance() { return insurance; }

    public void setInsurance(String insurance) { this.insurance = insurance; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

}