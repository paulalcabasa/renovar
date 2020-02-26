package com.drei.renovarapp.Models;

public class Collection {
    private int id;
    private String collection;
    private String image_url;
    private int category_id;

    public Collection(int id, String collection, String image_url, int category_id) {
        this.id = id;
        this.collection = collection;
        this.image_url = image_url;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
