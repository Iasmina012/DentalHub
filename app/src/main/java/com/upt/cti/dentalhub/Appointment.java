package com.upt.cti.dentalhub;

public class Appointment {

    private String appointmentId;
    private String location;
    private String doctor;
    private int doctorId;
    private String service;
    private String date;
    private String time;
    private String insurance;
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String status;

    public Appointment() {
        //Constructor necesar pentru Firebase
    }

    public Appointment(String appointmentId, String location, String doctor, int doctorId, String service, String date, String time, String insurance, String userId, String userName, String firstName, String lastName, String status) {

        this.appointmentId = appointmentId;
        this.location = location;
        this.doctorId = doctorId;
        this.doctor = doctor;
        this.service = service;
        this.date = date;
        this.time = time;
        this.insurance = insurance;
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;

    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}