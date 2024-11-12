package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class BirthdayReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BirthdayReminderReceiver", "Birthday reminder broadcast received");

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = null;
        Cursor cursor = null;
        ArrayList<Person> todayBirthdays;
        try {
            database = dbHelper.getReadableDatabase();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("CET"));
            String currentDate = sdf.format(new Date());

            String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                    " WHERE strftime('%m-%d', " + DatabaseHelper.COLUMN_BIRTHDATE + ") = strftime('%m-%d', ?)";
            cursor = database.rawQuery(query, new String[]{currentDate});

            todayBirthdays = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String birthdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIRTHDATE));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE_NUMBER));
                todayBirthdays.add(new Person(id, name, birthdate, phoneNumber));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        if (!todayBirthdays.isEmpty()) {
            sendBirthdayNotification(context, todayBirthdays);
        }
    }

    private void sendBirthdayNotification(Context context, ArrayList<Person> todayBirthdays) {
        Log.d("BirthdayReminderReceiver", "Attempting to send birthday notification");
        StringBuilder birthdayNames = new StringBuilder();
        for (Person person : todayBirthdays) {
            birthdayNames.append(person.getName()).append(", ");
        }
        if (birthdayNames.length() > 2) {
            birthdayNames.setLength(birthdayNames.length() - 2);
        }

        String notificationTitle = "Today's Birthdays";
        String notificationText = "Don't forget to wish: " + birthdayNames;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "BIRTHDAY_CHANNEL_ID")
                .setSmallIcon(R.drawable.cake)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1002, builder.build());
        } else {
            Log.e("BirthdayReminderReceiver", "Notification permission not granted");
        }
    }
}
