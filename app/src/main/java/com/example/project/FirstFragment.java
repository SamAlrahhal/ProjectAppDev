package com.example.project;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.databinding.FragmentFirstBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SQLiteDatabase database;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        // Initialize the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getReadableDatabase();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call your method here to fetch data from the database
        getUpcomingBirthdays();
    }


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

        // Fetch data from cursor and update the appropriate ListView
        ArrayList<String> todayBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            todayBirthdays.add(name);
        }
        cursor.close();

        // Query to get this week's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDate + "') AND strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") <= strftime('%m-%d', '" + currentDatePlusWeek + "')";
        cursor = database.rawQuery(query, null);

        ArrayList<String> weekBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            weekBirthdays.add(name);
        }
        cursor.close();



        // Query to get this month's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = '" + currentMonth + "'";
        cursor = database.rawQuery(query, null);

        ArrayList<String> monthBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            monthBirthdays.add(name);
        }
        cursor.close();


        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, monthBirthdays);
        ListView monthListView = binding.monthBirthdays.monthBirthdayList;
        monthListView.setAdapter(monthAdapter);

/*
        ArrayAdapter<String> weekAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, weekBirthdays);
        ListView weekListView = binding.weekBirthdays.weekBirthdayList;
        weekListView.setAdapter(weekAdapter);

        ArrayAdapter<String> todayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, todayBirthdays);
        ListView todayListView = binding.weekBirthdays.weekBirthdayList;
        todayListView.setAdapter(todayAdapter);


 */
    }
}
