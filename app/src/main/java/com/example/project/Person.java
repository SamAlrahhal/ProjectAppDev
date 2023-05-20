package com.example.project;

import android.database.Cursor;

import java.util.List;

public class Person {
    private int id;
    private String name;
    private String birthdate;
    private String phoneNumber;

    // Constructor
    public Person(int id, String name, String birthdate, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public int getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


