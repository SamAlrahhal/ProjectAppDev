package com.example.project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "people.db";
    public static final String TABLE_NAME = "person";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDATE = "birthdate";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_BIRTHDATE + " TEXT NOT NULL," +
                COLUMN_PHONE_NUMBER + " TEXT NOT NULL" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addPerson(String name, String birthdate, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_BIRTHDATE, birthdate);
        contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor getAllPeople() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Person person = new Person(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER))
                );
                personList.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return personList;
    }
}
