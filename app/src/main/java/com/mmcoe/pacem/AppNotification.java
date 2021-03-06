package com.mmcoe.pacem;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import java.util.Objects;

public class AppNotification extends Application {
    public static final String CHANNEL_ID = "channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Reminder channel");

        NotificationManager manager = getSystemService(NotificationManager.class);
        Objects.requireNonNull(manager).createNotificationChannel(channel);
    }
}
