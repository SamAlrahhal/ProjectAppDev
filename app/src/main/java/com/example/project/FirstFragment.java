package com.example.project;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.databinding.FragmentFirstBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.annotation.NonNull;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;



public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SQLiteDatabase database;
    private ArrayList<Person> todayBirthdays;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission is granted. Send the notification.
                sendBirthdayNotification(todayBirthdays);
            } else {
                // Permission denied. Handle the case.
            }
        });
    }
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
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String birthdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDATE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
            todayBirthdays.add(new Person(id, name, birthdate, phoneNumber));
        }
        cursor.close();

        RecyclerView todayRecyclerView = binding.todayBirthdays.birthdayList;
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PersonAdapter todayAdapter = new PersonAdapter(todayBirthdays);
        todayRecyclerView.setAdapter(todayAdapter);

        if (!todayBirthdays.isEmpty()) {
            sendBirthdayNotification(todayBirthdays);
        }


        // Query to get this week's birthdays
        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDate + "') AND strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") <= strftime('%m-%d', '" + currentDatePlusWeek + "')";
        cursor = database.rawQuery(query, null);

        ArrayList<Person> weekBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String birthdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDATE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
            weekBirthdays.add(new Person(id, name, birthdate, phoneNumber));
        }
        cursor.close();

        RecyclerView weekRecyclerView = binding.weekBirthdays.birthdayList;
        weekRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PersonAdapter weekAdapter = new PersonAdapter(weekBirthdays);
        weekRecyclerView.setAdapter(weekAdapter);

        query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE strftime('%m', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = '" + currentMonth + "' AND " +
                "strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") > strftime('%m-%d', '" + currentDatePlusWeek + "') AND " +
                "strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") != strftime('%m-%d', '" + currentDate + "')";
        cursor = database.rawQuery(query, null);

        ArrayList<Person> monthBirthdays = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String birthdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDATE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
            monthBirthdays.add(new Person(id, name, birthdate, phoneNumber));
        }
        cursor.close();

        RecyclerView monthRecyclerView = binding.monthBirthdays.birthdayList;
        monthRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PersonAdapter monthAdapter = new PersonAdapter(monthBirthdays);
        monthRecyclerView.setAdapter(monthAdapter);


    }

    private void sendBirthdayNotification(ArrayList<Person> todayBirthdays) {
        // Create a notification channel (required for Android 8.0 and above)
        createNotificationChannel();

        // Build the notification content
        StringBuilder birthdayNames = new StringBuilder();
        for (Person person : todayBirthdays) {
            birthdayNames.append(person.getName()).append(", ");
        }
        // Remove the trailing comma and space
        if (birthdayNames.length() > 2) {
            birthdayNames.setLength(birthdayNames.length() - 2);
        }

        String notificationTitle = "Today's Birthdays";
        String notificationText = "Don't forget to wish: " + birthdayNames.toString();

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "BIRTHDAY_CHANNEL_ID")
                .setSmallIcon(R.drawable.cake) // Replace with your app's icon
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Check and request the POST_NOTIFICATIONS permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                return;
            }
        }

        // Display the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1001, builder.build());
    }

    private void createNotificationChannel() {
        // Notification channels are required for Android 8.0 (API level 26) and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Birthday Notifications";
            String description = "Notifications for today's birthdays";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("BIRTHDAY_CHANNEL_ID", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}