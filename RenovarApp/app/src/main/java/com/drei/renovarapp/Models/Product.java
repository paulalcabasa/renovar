package com.drei.renovarapp.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private int product_no;
    private String image;
    private String name;
    private String description;
    private double price;
    private int interval;
    private String message;

    public Product(int product_no, String image, String name, String description, double price, int interval,String message) {
        this.product_no = product_no;
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.interval = interval;
        this.message = message;
    }

    public int getProduct_no() {
        return product_no;
    }

    public void setProduct_no(int product_no) {
        this.product_no = product_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
