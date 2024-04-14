package com.example.gamecenter.Main;

public class ListElement {

    // Attributes:
    public String color;
    public String name;

    // Constructor
    public ListElement(String color, String name) {
        this.color = color;
        this.name = name;
    }

    // Setters:
    public void setColor(String color) {
        this.color = color;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getters:
    public String getColor() {
        return color;
    }
    public String getName() {
        return name;
    }
}