package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.project.databinding.FragmentFirstBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SQLiteDatabase database;


    private List<String> getUpcomingBirthdays(String timePeriod) {
        // Create a List to hold the names
        List<String> names = new ArrayList<>();

        // Create a SQL query
        String query;
        switch (timePeriod) {
            case "today":
                query = "SELECT name FROM person WHERE DATE(birthdate) = DATE('now')";
                break;
            case "week":
                query = "SELECT name FROM person WHERE DATE(birthdate) BETWEEN DATE('now') AND DATE('now', '+7 day')";
                break;
            case "month":
                query = "SELECT name FROM person WHERE DATE(birthdate) BETWEEN DATE('now') AND DATE('now', '+1 month')";
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + timePeriod);
        }

        // Execute the query
        Cursor cursor = database.rawQuery(query, null);

        // Loop through the results and add them to the List
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // Close the cursor
        cursor.close();

        return names;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}