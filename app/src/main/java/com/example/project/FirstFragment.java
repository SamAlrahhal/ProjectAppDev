package com.example.project;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.databinding.FragmentFirstBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


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
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
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



        ArrayList<Person> todayBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String birthdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDATE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
            todayBirthdays.add(new Person(name, birthdate, phoneNumber));
        }
        cursor.close();

        RecyclerView todayRecyclerView = binding.todayBirthdays.birthdayList;
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PersonAdapter todayAdapter = new PersonAdapter(todayBirthdays);
        todayRecyclerView.setAdapter(todayAdapter);



        // Query to get this week's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDate + "') AND strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") <= strftime('%m-%d', '" + currentDatePlusWeek + "')";
        cursor = database.rawQuery(query, null);

        ArrayList<Person> weekBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String birthdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDATE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
            weekBirthdays.add(new Person(name, birthdate, phoneNumber));
        }
        cursor.close();
        RecyclerView weekRecyclerView = binding.weekBirthdays.birthdayList;
        weekRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PersonAdapter weekAdapter = new PersonAdapter(weekBirthdays);
        weekRecyclerView.setAdapter(weekAdapter);
        






        // Query to get this month's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = '" + currentMonth + "' AND " +
                "(strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDatePlusWeek + "') OR " +
                "strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") < strftime('%m-%d', '" + currentDate + "'))";
        cursor = database.rawQuery(query, null);

        ArrayList<Person> monthBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String birthdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDATE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
            monthBirthdays.add(new Person(name, birthdate, phoneNumber));
        }
        cursor.close();
        RecyclerView monthRecyclerView = binding.monthBirthdays.birthdayList;
        monthRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PersonAdapter monthAdapter = new PersonAdapter(monthBirthdays);
        monthRecyclerView.setAdapter(monthAdapter);









    }
}
