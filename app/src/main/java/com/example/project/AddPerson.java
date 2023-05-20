package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class AddPerson extends Fragment {

    private DatabaseHelper databaseHelper;
    private EditText editTextName, editTextBirthdate, editTextPhoneNumber;
    private Button buttonAddPerson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_person, container, false);


        databaseHelper = new DatabaseHelper(getActivity());
        editTextName = view.findViewById(R.id.editTextName);
        editTextBirthdate = view.findViewById(R.id.editTextBirthdate);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        buttonAddPerson = view.findViewById(R.id.buttonAddPerson);

        buttonAddPerson.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String birthdate = editTextBirthdate.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            if (name.isEmpty() || birthdate.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!isValidDate(birthdate)) {
                Toast.makeText(getActivity(), "Invalid birthdate. Format should be yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            } else if (!isNumeric(phoneNumber)) {
                Toast.makeText(getActivity(), "Phone number can only contain digits", Toast.LENGTH_SHORT).show();
            } else {
                boolean result = databaseHelper.addPerson(name, birthdate, phoneNumber);
                if (result) {
                    Toast.makeText(getActivity(), "Person added successfully", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextBirthdate.setText("");
                    editTextPhoneNumber.setText("");
                } else {
                    Toast.makeText(getActivity(), "Error adding person", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}

