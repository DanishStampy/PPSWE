package com.example.ppswe.adapter.reciever;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ppswe.R;
import com.example.ppswe.view.patient.MainMenuActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int reqCode = intent.getIntExtra("reqCode", 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, i, 0);

        Log.d("ALARM", "Alarm fired with request code: " +reqCode);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "medicationChannel")
                .setContentTitle("PPSWE Medication Reminder")
                .setContentText("This is first notification")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
