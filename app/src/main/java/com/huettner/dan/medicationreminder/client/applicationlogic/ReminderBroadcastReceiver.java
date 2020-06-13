package com.huettner.dan.medicationreminder.client.applicationlogic;

/**
 * Created by Liana on 2018-05-14.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.huettner.dan.medicationreminder.R;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationTakenController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.PatientController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.SettingsController;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Medication;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.MedicationTaken;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Patient;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Settings;
import com.huettner.dan.medicationreminder.client.userinterface.PatientActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String patientID = intent.getData().getQueryParameter("patientID");
        String medicationID = intent.getData().getQueryParameter("medicationID");

        PatientController patientController = PatientController.getSingleton(context);
        MedicationController medicationController = MedicationController.getSingleton(context);
        MedicationTakenController medicationTakenController = MedicationTakenController.getSingleton(context);
        SettingsController settingsController = SettingsController.getSingleton(context);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {}
        Date date = c.getTime();

        Patient patient = patientController.getPatient(patientID);
        Medication medication = medicationController.getMedication(medicationID);
        MedicationTaken medicationTaken = medicationTakenController.getMedicationTaken(medication.getID(), date);
        Settings settings = settingsController.getSettings();

        if (medicationTaken != null)
            return;

        if (settings.getEnableMultiPatientMode()) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setAutoCancel(false);
            mBuilder.setSmallIcon(R.drawable.reminder_icon);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setContentTitle("Medication Reminder");
            String reminderHour = medication.getReminderHour() < 10 ? "0" + String.valueOf(medication.getReminderHour()) : String.valueOf(medication.getReminderHour());
            String reminderMinute = medication.getReminderMinute() < 10 ? "0" + String.valueOf(medication.getReminderMinute()) : String.valueOf(medication.getReminderMinute());
            mBuilder.setContentText(patient.getName() + " | " + medication.getName() + " (" + reminderHour + ":" + reminderMinute + ")");

            Intent resultIntent = new Intent(context, PatientActivity.class);
            resultIntent.putExtra("patientID", patient.getID());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(PatientActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (settings.getEnableReminderSound()) {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(notification);
            }

            mNotificationManager.notify(medication.getReminderID(), mBuilder.build());
        }
        else {
            if (patient.getID().equals("1")) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setAutoCancel(false);
                mBuilder.setSmallIcon(R.drawable.reminder_icon);
                Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
                mBuilder.setLargeIcon(largeIcon);
                mBuilder.setContentTitle("Medication Reminder");
                String reminderHour = medication.getReminderHour() < 10 ? "0" + String.valueOf(medication.getReminderHour()) : String.valueOf(medication.getReminderHour());
                String reminderMinute = medication.getReminderMinute() < 10 ? "0" + String.valueOf(medication.getReminderMinute()) : String.valueOf(medication.getReminderMinute());
                mBuilder.setContentText(medication.getName() + " (" + reminderHour + ":" + reminderMinute + ")");

                Intent resultIntent = new Intent(context, PatientActivity.class);
                resultIntent.putExtra("patientID", patient.getID());
                resultIntent.putExtra("caller", "Reminder");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(PatientActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (settings.getEnableReminderSound()) {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(notification);
                }

                mNotificationManager.notify(medication.getReminderID(), mBuilder.build());
            }
        }
    }
}