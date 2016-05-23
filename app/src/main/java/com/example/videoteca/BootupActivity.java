package com.example.videoteca;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author: Mario Velasco Casquero
 * Date: 23/05/2016
 */
public class BootupActivity extends BroadcastReceiver {
    private static final long INTERVAL = 30000;
    private static final long INITIAL_DELAY = 5000;

    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().endsWith(Intent.ACTION_BOOT_COMPLETED)) {
            scheduleRecommendationUpdate(context);
        }
    }

    private void scheduleRecommendationUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        Intent recommendationIntent = new Intent(context,
                UpdateRecommendationService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0,
                recommendationIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager
                .ELAPSED_REALTIME_WAKEUP, INITIAL_DELAY, INTERVAL, alarmIntent);
    }
}
