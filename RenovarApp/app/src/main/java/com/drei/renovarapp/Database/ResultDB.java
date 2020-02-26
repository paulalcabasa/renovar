package com.drei.renovarapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultDB implements Serializable {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static ResultDB instance;

    private ResultDB(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static ResultDB getInstance(Context context) {
        if (instance == null) {
            instance = new ResultDB(context);
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

    private int therapy_id;
    private String path_before;
    private String path_after;
    private long time_millis;

    public ResultDB(int therapy_id, String path_before, String path_after, long time_millis) {
        this.therapy_id = therapy_id;
        this.path_before = path_before;
        this.path_after = path_after;
        this.time_millis = time_millis;
    }

    public int getId() {
        return therapy_id;
    }

    public void setId(int therapy_id) {
        this.therapy_id = therapy_id;
    }

    public String getPath_before() {
        return path_before;
    }

    public void setPath_before(String path_before) {
        this.path_before = path_before;
    }

    public String getPath_after() {
        return path_after;
    }

    public void setPath_after(String path_after) {
        this.path_after = path_after;
    }

    public long getTime_millis() {
        return time_millis;
    }

    public void setTime_millis(long time_millis) {
        this.time_millis = time_millis;
    }

    public boolean insertRecord() {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("therapies_id", getId());
        values.put("image_path_before", getPath_before());
        values.put("image_path_after", getPath_after());
        values.put("date", getTime_millis());
        long result = database.insert("results", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteRecord(long date) {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        long result = database.delete("results", "date=" + date, null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<ResultDB> getResultList() {
        List<ResultDB> resultDBS = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM results", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            resultDBS.add(new ResultDB(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return resultDBS;
    }

    public List<ResultDB> getResultList(String id) {
        List<ResultDB> resultDBS = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM results WHERE therapies_id ="+id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            resultDBS.add(new ResultDB(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return resultDBS;
    }

    public List<ResultDB> getResultListGroup() {
        List<ResultDB> resultDBS = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM results GROUP BY therapies_id", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            resultDBS.add(new ResultDB(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return resultDBS;
    }


}
