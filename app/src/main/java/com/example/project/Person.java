package com.example.project;

import android.database.Cursor;

import java.util.List;

public class Person {
    private String name;
    private String birthdate;
    private String phoneNumber;

    public Person(String name, String birthdate, String phoneNumber) {
        this.name = name;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
    }

    public static List<Person> fromCursor(Cursor cursor) {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
