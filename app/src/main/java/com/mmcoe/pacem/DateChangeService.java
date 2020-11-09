package com.mmcoe.pacem;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.util.Objects;

import static com.mmcoe.pacem.AppNotification.CHANNEL_ID;

public class DateChangeService extends Service {
    private int age;
    private NotificationManagerCompat notificationManager;
    private static BroadcastReceiver dateBroadcastReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerDateChangeReceiver();
        notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(this));
        Toast.makeText(this,"Service running",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(dateBroadcastReceiver);
        dateBroadcastReceiver = null;
    }

    private void registerDateChangeReceiver()
    {
        dateBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                age = sh.getInt("age", -1);
                age++;
                sh.edit().putInt("age",age).apply();
                checkVaccine();
                Toast.makeText(context,"Date changed",Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        registerReceiver(dateBroadcastReceiver, filter);
    }

    private void checkVaccine(){
        if (age == 0 || age == 42 || age == 70 || age == 98 || age == 183 || age == 274 || age == 335 || age == 365 || age == 456 || age == 517 || age == 548 || age == 730 || age == 1460){
            notifyUser();
        }
    }

    private void notifyUser(){
        // Create an explicit intent for an Activity
        Intent intent = new Intent(this, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("Reminder")
                .setContentText("Your child's vaccination is due tomorrow")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1,builder.build());
    }
}
