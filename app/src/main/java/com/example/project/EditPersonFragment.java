package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditPersonFragment extends Fragment {
    private Person person;


    public void populatePersonData(Person person) {
        this.person = person;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (person != null) {
            EditText editTextName = view.findViewById(R.id.editTextName);
            EditText editTextBirthdate = view.findViewById(R.id.editTextBirthdate);
            EditText editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);

            editTextName.setText(person.getName());
            editTextBirthdate.setText(person.getBirthdate());
            editTextPhoneNumber.setText(person.getPhoneNumber());
        }
    }

}
