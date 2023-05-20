package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.EditPersonFragment;

public class PersonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        // Get the Person's id from the Intent's extras
        final int personId = getIntent().getIntExtra("PERSON_ID", -1);

        // Use this id to get the Person's details and display them
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Person person = databaseHelper.getPersonById(personId);
        // Display the Person's details

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated details from the EditText views
                String name = ((EditText) findViewById(R.id.editName)).getText().toString();
                String birthdate = ((EditText) findViewById(R.id.editBirthdate)).getText().toString();
                String phoneNumber = ((EditText) findViewById(R.id.editPhoneNumber)).getText().toString();

                // Use these details to update the Person
                databaseHelper.updatePerson(personId, name, birthdate, phoneNumber);
            }
        });

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the Person and finish the activity
                databaseHelper.deletePerson(personId);
                finish();
            }
        });
    }

    // This is a placeholder for the method you'll need to implement to fetch Person by id
    private Person getPersonById(int personId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.getPersonById(personId);
    }
}

