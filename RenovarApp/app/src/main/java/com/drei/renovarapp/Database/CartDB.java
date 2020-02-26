package com.drei.renovarapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartDB implements Serializable {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static CartDB instance;

    private CartDB(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static CartDB getInstance(Context context) {
        if (instance == null) {
            instance = new CartDB(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    private int quantity;
    private String item_name;
    private double total_price;
    private String image_url;

    public CartDB(int quantity, String item_name, double total_price, String image_url) {
        this.quantity = quantity;
        this.item_name = item_name;
        this.total_price = total_price;
        this.image_url = image_url;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean insertRecord() {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", getQuantity());
        values.put("item_name", getItem_name());
        values.put("total_price", getTotal_price());
        values.put("image_url", getImage_url());
        long result = database.insert("cart", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteRecord(String items_name) {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        long result = database.delete("cart", "item_name=" + items_name, null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteAllRecord() {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        long result = database.delete("cart", "", null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<CartDB> getCart() {
        List<CartDB> resultDBS = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT SUM(quantity),item_name,SUM(total_price),image_url FROM cart GROUP BY item_name", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            resultDBS.add(new CartDB(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return resultDBS;
    }

}
