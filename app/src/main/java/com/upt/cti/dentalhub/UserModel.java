package com.upt.cti.dentalhub;

public class UserModel {
    private String displayName;
    private String email;
    private long createdAt;
    private String mRecipientId;


    public UserModel() {

    }

    public UserModel(String displayName, String email,long createdAt) {
        this.displayName = displayName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    private String getUserEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRecipientId() {
        return mRecipientId;
    }

    public void setRecipientId(String recipientId) {
        this.mRecipientId = recipientId;
    }
}



