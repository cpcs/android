package com.example.cpcs.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by cpcs on 11/11/15.
 */

public class MainActivity extends AppCompatActivity {
    private static final int TWO_MINUTE = 1000 * 60 * 2;
    private static final int REQUEST_CODE = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new PhotoListFragment();
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        setAlarm();
    }

    private void setAlarm() {
        Intent intent = new Intent(this,
                AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TWO_MINUTE,
                TWO_MINUTE,
                pendingIntent);
    }
}
