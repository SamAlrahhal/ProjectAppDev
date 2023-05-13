package com.example.project;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.databinding.FragmentFirstBinding;
import com.example.project.databinding.UpcomingBirthdaysBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SQLiteDatabase database;

    // Add bindings for each included layout
    private UpcomingBirthdaysBinding todayBirthdaysBinding;
    private UpcomingBirthdaysBinding weekBirthdaysBinding;
    private UpcomingBirthdaysBinding monthBirthdaysBinding;

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

        todayBirthdaysBinding = UpcomingBirthdaysBinding.bind(view.findViewById(R.id.today_birthdays));
        weekBirthdaysBinding = UpcomingBirthdaysBinding.bind(view.findViewById(R.id.week_birthdays));
        monthBirthdaysBinding = UpcomingBirthdaysBinding.bind(view.findViewById(R.id.month_birthdays));

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

        // Fetch data from cursor and update the appropriate TextView
        StringBuilder todayBirthdays = new StringBuilder();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            todayBirthdays.append(name).append("\n");
        }
        cursor.close();

        // Query to get this week's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDate + "') AND strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") <= strftime('%m-%d', '" + currentDatePlusWeek + "')";
        cursor = database.rawQuery(query, null);

        StringBuilder weekBirthdays = new StringBuilder();  // Create a StringBuilder for week birthdays
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            weekBirthdays.append(name).append("\n");  // Append to week birthdays
        }
        cursor.close();

        // Query to get this month's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = '" + currentMonth + "'";
        cursor = database.rawQuery(query, null);

        StringBuilder monthBirthdays = new StringBuilder();  // Create a StringBuilder for month birthdays
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            monthBirthdays.append(name).append("\n");  // Append to month birthdays
        }
        cursor.close();

        // Fetch data from cursor and update the appropriate TextView
        TextView todayBirthdaysTextView = todayBirthdaysBinding.birthdays;
        TextView weekBirthdaysTextView = weekBirthdaysBinding.birthdays;
        TextView monthBirthdaysTextView = monthBirthdaysBinding.birthdays;

        // Set the text for each TextView
        todayBirthdaysTextView.setText(todayBirthdays.toString());
        weekBirthdaysTextView.setText(weekBirthdays.toString());
        monthBirthdaysTextView.setText(monthBirthdays.toString());
    }

}
