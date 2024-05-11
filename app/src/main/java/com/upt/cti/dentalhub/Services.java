package com.upt.cti.dentalhub;

public class Services {

    private String title;
    private String description;
    private int imageResourceId;

    public Services(String title, String description, int imageResourceId) {

        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;

    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public int getImageResourceId() {return imageResourceId;}

    public void setImageResourceId(int imageResourceId) {this.imageResourceId = imageResourceId;}

}
