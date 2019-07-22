package com.example.eventracker.model;

public class Venue {
    private String addressDisplay;
    private String areaDisplay;

    public String getAddressDisplay() {
        return addressDisplay;
    }

    public void setAddressDisplay(String addressDisplay) {
        this.addressDisplay = addressDisplay;
    }

    public String getAreaDisplay() {
        return areaDisplay;
    }

    public void setAreaDisplay(String areaDisplay) {
        this.areaDisplay = areaDisplay;
    }

    public Venue() {
        this.addressDisplay = addressDisplay;
        this.areaDisplay = areaDisplay;
    }
}

