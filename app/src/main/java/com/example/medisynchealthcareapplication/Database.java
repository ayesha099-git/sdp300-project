package com.example.medisynchealthcareapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medisync.db";
    private static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // User table with username as primary key
        String qry1 = "CREATE TABLE users(" +
                "username TEXT PRIMARY KEY, " +
                "email TEXT, " +
                "password TEXT)";
        sqLiteDatabase.execSQL(qry1);

        // Diabetes data table linked by username (foreign key)
        String qry2 = "CREATE TABLE userdiabeticdata(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "date TEXT, " +
                "empty_sugar REAL, " +
                "full_sugar REAL, " +
                "FOREIGN KEY(username) REFERENCES users(username))";
        sqLiteDatabase.execSQL(qry2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Basic upgrade strategy - drop and recreate tables
        db.execSQL("DROP TABLE IF EXISTS userdiabeticdata");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // User registration
    public void register(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
    }

    // User login check
    public int login(String username, String password) {
        int result = 0;
        String[] args = {username, password};
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", args);
        if (c.moveToFirst()) {
            result = 1;
        }
        c.close();
        db.close();
        return result;
    }

    // Insert diabetic data for a user
    public boolean insertDiabeticData(String username, String date, float emptySugar, float fullSugar) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("date", date);
        cv.put("empty_sugar", emptySugar);
        cv.put("full_sugar", fullSugar);

        long result = db.insert("userdiabeticdata", null, cv);
        db.close();

        return result != -1;
    }


    // Get all diabetic data records for a user ordered by date ascending
    public Cursor getDiabeticData(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT date, empty_sugar, full_sugar FROM userdiabeticdata WHERE username=? ORDER BY date ASC",
                new String[]{username}
        );
    }
}
