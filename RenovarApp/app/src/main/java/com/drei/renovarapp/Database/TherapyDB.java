package com.drei.renovarapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TherapyDB implements Serializable {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static TherapyDB instance;

    private TherapyDB(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static TherapyDB getInstance(Context context) {
        if (instance == null) {
            instance = new TherapyDB(context);
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

    private int id;
    private String name;
    private int hour;
    private int minute;
    private int duration;
    private long time_millis;
    private String message;

    public TherapyDB(int id, String name, int duration, int hour, int minute, long time_millis, String message) {
        this.id = id;
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.duration = duration;
        this.time_millis = time_millis;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getTime_millis() {
        return time_millis;
    }

    public void setTime_millis(long time_millis) {
        this.time_millis = time_millis;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean insertRecord() {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", getId());
        values.put("name", getName());
        values.put("duration", getDuration());
        values.put("hour", getHour());
        values.put("minute", getMinute());
        values.put("time_millis", getTime_millis());
        values.put("message", getMessage());
        long result = database.insert("therapies", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteRecord() {
        SQLiteDatabase database = openHelper.getWritableDatabase();
        long result = database.delete("therapies", "id=" + getId(), null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int generateID() {
        int id = 1;
        Cursor cursor = database.rawQuery("SELECT * FROM therapies", null);
        cursor.moveToFirst();
        if (!(cursor.getCount() <= 0)) {
            id = cursor.getInt(0) + 1;
        }
        cursor.close();
        return id;
    }


    public List<TherapyDB> getTherapyList() {
        List<TherapyDB> therapyDBS = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM therapies", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            therapyDBS.add(new TherapyDB(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getLong(5),
                    cursor.getString(6)));
            cursor.moveToNext();
        }
        cursor.close();
        return therapyDBS;
    }

    public List<TherapyDB> getTherapyList(String id) {
        List<TherapyDB> therapyDBS = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM therapies WHERE id = "+id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            therapyDBS.add(new TherapyDB(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getLong(5),
                    cursor.getString(6)));
            cursor.moveToNext();
        }
        cursor.close();
        return therapyDBS;
    }
}
