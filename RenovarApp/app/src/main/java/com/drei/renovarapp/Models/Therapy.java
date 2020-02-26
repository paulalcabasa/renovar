package com.drei.renovarapp.Models;

import java.io.Serializable;

public class Therapy implements Serializable {
    private long date_time;
    private Product product;
    private String notes;

    public Therapy(long date_time, Product product, String notes) {
        this.date_time = date_time;
        this.product = product;
        this.notes = notes;
    }

    public long getDate_time() {
        return date_time;
    }

    public void setDate_time(long date_time) {
        this.date_time = date_time;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
