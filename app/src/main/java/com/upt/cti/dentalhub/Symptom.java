package com.upt.cti.dentalhub;

public class Symptom {

    private String name;
    private boolean isChecked;

    public Symptom(String name) {

        this.name = name;
        this.isChecked = false;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}