package com.upt.cti.dentalhub;

public class UserModel {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String imageUrl;
    private long createdAt;

    public UserModel() {
        //constructor necesar pentru firebase
    }

    public UserModel(String firstName, String lastName, String username, String email, long createdAt, String imageUrl) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;

    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public long getCreatedAt() { return createdAt; }

    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}