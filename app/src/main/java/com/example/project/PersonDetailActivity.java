package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.EditPersonFragment; // This import statement was missing

public class PersonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Intent intent = getIntent();
        int personId = intent.getIntExtra("PERSON_ID", -1);

        // Assume you have a method to fetch Person by id
        Person person = getPersonById(personId);

        EditPersonFragment fragment = new EditPersonFragment();

        // Assume you have a method in your fragment to populate person data
        fragment.populatePersonData(person);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // This is a placeholder for the method you'll need to implement to fetch Person by id
    private Person getPersonById(int personId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.getPersonById(personId);
    }
}

