package com.upt.cti.dentalhub;

public class UserModel {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private long createdAt;
    private String mRecipientId;

    public UserModel() {

    }

    public UserModel(String firstName, String lastName, String userName, String email, long createdAt) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.email = email;
        this.createdAt = createdAt;

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

    public String getmRecipientId() { return mRecipientId; }

    public void setmRecipientId(String mRecipientId) { this.mRecipientId = mRecipientId; }

}