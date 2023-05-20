package com.example.project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.content.Intent.getIntent;

public class PersonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        // Get the id of the person from the Intent
        String personName = getIntent().getStringExtra("PERSON_NAME");

        // Use the id to retrieve the details of the person from your database
        // ...
    }
}

