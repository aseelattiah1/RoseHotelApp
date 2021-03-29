package com.Assel.myapplication2;

public class MenuResturant {

    private int image;
    private String name;
    private String cost;

    public MenuResturant(int image, String name, String cost) {
        this.image = image;
        this.name = name;
        this.cost = cost;
    }

    public MenuResturant() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
