package com.upt.cti.dentalhub.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {

            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            String appointmentId = intent.getStringExtra("appointmentId");
            boolean isReschedule = intent.getBooleanExtra("isReschedule", false);
            boolean isReminder = intent.getBooleanExtra("isReminder", false);
            boolean isMissed = intent.getBooleanExtra("isMissed", false);
            boolean isCheckIn = intent.getBooleanExtra("isCheckIn", false);
            NotificationHelper notificationHelper = new NotificationHelper(context);

            if (isReschedule) {
                notificationHelper.sendRescheduleNotification(title, message, appointmentId);
            } else if (isReminder) {
                notificationHelper.sendReminderNotification(title, message, appointmentId);
            } else if (isMissed) {
                notificationHelper.sendMissedNotification(title, message, appointmentId);
            } else if (isCheckIn) {
                notificationHelper.sendCheckInNotification(title, message);
            } else {
                notificationHelper.sendCancelNotification(title, message);
            }

        }

    }

}