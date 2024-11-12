package com.example.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.project.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import com.example.project.FirstFragment;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    private FloatingActionButton fab;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        startBirthdayReminderCheck();
        createNotificationChannel();
    }

    private void startBirthdayReminderCheck() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BirthdayReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to trigger every 24 hours
        long intervalMillis = AlarmManager.INTERVAL_DAY;
        long triggerAtMillis = SystemClock.elapsedRealtime() + intervalMillis;
        if (alarmManager != null) {
            Log.d("MainActivity", "Setting alarm for birthday reminder check");
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("Home");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {

            //go to home page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.addPerson) {

            //go to add person page using nav controller
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

            if (navController.getCurrentDestination().getId() == R.id.FirstFragment) {
                navController.navigate(R.id.action_FirstFragment_to_addPerson2);
                getSupportActionBar().setTitle("Add Person");

            }else if(navController.getCurrentDestination().getId() == R.id.showAll2){
                navController.navigate(R.id.action_showAll2_to_addPerson2);
                getSupportActionBar().setTitle("Add Person");

            }
            return true;
        }

        //make an option for the ShowAll button
        if (id == R.id.showAll) {
            //go to show all page using nav controller
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

            if (navController.getCurrentDestination().getId() == R.id.FirstFragment) {
                navController.navigate(R.id.action_FirstFragment_to_showAll2);
                getSupportActionBar().setTitle("Show All");

            }else if(navController.getCurrentDestination().getId() == R.id.addPerson2){
                navController.navigate(R.id.action_addPerson2_to_showAll2);
                getSupportActionBar().setTitle("Show All");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                if (navController.getCurrentDestination().getId() == R.id.FirstFragment) {
                    navController.navigate(R.id.action_FirstFragment_to_addPerson2);
                    getSupportActionBar().setTitle("Add Person");
                } else if (navController.getCurrentDestination().getId() == R.id.showAll2) {
                    navController.navigate(R.id.action_showAll2_to_addPerson2);
                    getSupportActionBar().setTitle("Add Person");
                }
                break;
        }
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Birthday Notifications";
            String description = "Notifications for today's birthdays";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("BIRTHDAY_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}