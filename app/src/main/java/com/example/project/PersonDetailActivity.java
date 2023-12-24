package com.example.project;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.EditPersonFragment;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class PersonDetailActivity extends AppCompatActivity {
    private MyContentProvider myContentProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        // Get the Person's data from the Intent's extras
        final int personId = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String birthdate = getIntent().getStringExtra("birthdate");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        // Display the Person's details
        EditText nameView = findViewById(R.id.editName);
        EditText birthdateView = findViewById(R.id.editBirthdate);
        EditText phoneNumberView = findViewById(R.id.editPhoneNumber);

        nameView.setText(name);
        birthdateView.setText(birthdate);
        phoneNumberView.setText(phoneNumber);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated details from the EditText views
                String updatedName = ((EditText) findViewById(R.id.editName)).getText().toString();
                String updatedBirthdate = ((EditText) findViewById(R.id.editBirthdate)).getText().toString();
                String updatedPhoneNumber = ((EditText) findViewById(R.id.editPhoneNumber)).getText().toString();

                // Use these details to update the Person
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAME, updatedName);
                values.put(DatabaseHelper.COLUMN_BIRTHDATE, updatedBirthdate);
                values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, updatedPhoneNumber);

                String selection = DatabaseHelper.COLUMN_ID + "=?";
                String[] selectionArgs = {String.valueOf(personId)};

                getContentResolver().update(MyContentProvider.CONTENT_URI, values, selection, selectionArgs);

                // Restart the activity with updated person's details
                Intent intent = new Intent(PersonDetailActivity.this, MainActivity.class);
                intent.putExtra("PERSON_ID", personId);

                // Finish current instance of activity and start new one
                finish();
                startActivity(intent);
            }
        });

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the Person and finish the activity
                //databaseHelper.deletePerson(personId);
                String[] selectionArgs = {String.valueOf(personId)};
                getContentResolver().delete(MyContentProvider.CONTENT_URI, DatabaseHelper.COLUMN_ID + "=?", selectionArgs);
                // Restart the activity with updated person's details
                Intent intent = new Intent(PersonDetailActivity.this, MainActivity.class);
                intent.putExtra("PERSON_ID", personId);

                // Finish current instance of activity and start new one
                finish();
                startActivity(intent);
            }
        });
    }

    // This is a placeholder for the method you'll need to implement to fetch Person by id
    private Person getPersonById(int personId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.getPersonById(personId);
    }
}

