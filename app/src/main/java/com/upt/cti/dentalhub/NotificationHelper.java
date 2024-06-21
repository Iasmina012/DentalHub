package com.upt.cti.dentalhub;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotificationHelper {

    private static final String CHANNEL_ID = "dentalhub_notifications";
    private static final String ADMIN_EMAIL = "admin@dentalhub.com";
    private Context context;

    public NotificationHelper(Context context) {

        this.context = context;
        createNotificationChannel();

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "DentalHub Notifications";
            String description = "Notifications for appointment updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }

    public void sendRescheduleNotification(String title, String content, String appointmentId) {
        sendNotification(title, content, appointmentId, true, false, false, false);
    }

    public void sendCancelNotification(String title, String content) {
        sendNotification(title, content, null, false, false, false, false);
    }

    public void sendReminderNotification(String title, String content, String appointmentId) {
        sendNotification(title, content, appointmentId, false, true, false, false);
    }

    public void sendMissedNotification(String title, String content, String appointmentId) {
        sendNotification(title, content, appointmentId, false, false, true, false);
    }

    public void sendCheckInNotification(String title, String content) {
        sendNotification(title, content, null, false, false, false, true);
    }

    private void sendNotification(String title, String content, String appointmentId, boolean isRescheduled, boolean isReminder, boolean isMissed, boolean isCheckIn) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity_Login) context, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (isRescheduled || isReminder) {
            Intent intent = getUserIntent(appointmentId, isRescheduled, isReminder, isMissed, isCheckIn);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            builder.addAction(R.drawable.app_logo, "Click to see more details", pendingIntent);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());

    }

    private Intent getUserIntent(String appointmentId, boolean isRescheduled, boolean isReminder, boolean isMissed, boolean isCheckIn) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;

        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (isAdmin(email)) {
                intent = new Intent(context, AdminActivity.class);
            } else if (isDoctor(email)) {
                intent = new Intent(context, DoctorActivity.class);
            } else {
                intent = new Intent(context, Activity_ViewAppointment.class);
            }
        } else {
            intent = new Intent(context, Activity_ViewAppointment.class);
        }

        if (appointmentId != null) {
            intent.putExtra("appointmentId", appointmentId);
        }

        intent.putExtra("isRescheduled", isRescheduled);
        intent.putExtra("isReminder", isReminder);
        intent.putExtra("isMissed", isMissed);
        intent.putExtra("isCheckIn", isCheckIn);
        return intent;

    }

    private boolean isAdmin(String email) { return ADMIN_EMAIL.equals(email); }

    private boolean isDoctor(String email) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_DOCTORS + " WHERE " + DatabaseHelper.COLUMN_DOCTOR_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean isDoctor = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                isDoctor = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        db.close();
        return isDoctor;

    }

}