package com.upt.cti.dentalhub.Models;

public class Device {

    private String name;
    private String description;
    private int imageResource;

    public Device(String name, String description, int imageResource) {

        this.name = name;
        this.description = description;
        this.imageResource = imageResource;

    }

    public String getName() {return name;}

    public String getDescription() {return description;}

    public int getImageResource() {return imageResource;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    public void setImageResource(int imageResource) {this.imageResource = imageResource;}

}