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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SQLiteDatabase database;


    public void getUpcomingBirthdays() {
        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        // Get current date + 7 days (for "This week" section)
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 7);
        String currentDatePlusWeek = sdf.format(c.getTime());

        // Get current month (for "This month" section)
        String currentMonth = new SimpleDateFormat("MM").format(new Date());

        // Query to get today's birthdays
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = strftime('%m-%d', '" + currentDate + "')";
        Cursor cursor = database.rawQuery(query, null);

        // Loop over the cursor to get data...
        // Close the cursor
        cursor.close();

        // Query to get this week's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDate + "') AND strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") < strftime('%m-%d', '" + currentDatePlusWeek + "')";
        cursor = database.rawQuery(query, null);

        // Loop over the cursor to get data...
        // Close the cursor
        cursor.close();

        // Query to get this month's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = '" + currentMonth + "'";
        cursor = database.rawQuery(query, null);

        // Loop over the cursor to get data...
        // Close the cursor
        cursor.close();
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        // Initialize the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getReadableDatabase();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getReadableDatabase();

        // Call your method here to fetch data from the database
        getUpcomingBirthdays();

    }



}