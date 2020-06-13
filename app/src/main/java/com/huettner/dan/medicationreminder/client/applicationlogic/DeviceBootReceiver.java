package com.huettner.dan.medicationreminder.client.applicationlogic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.PatientController;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Medication;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Liana on 2018-05-14.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MedicationController medicationController = MedicationController.getSingleton(context);
        PatientController patientController = PatientController.getSingleton(context);
        ArrayList<Medication> medications = medicationController.getMedicationsWithReminders();
        if (medications == null)
            return;
        for (int i = 0; i < medications.size(); i++) {
            Medication medication = medications.get(i);
            Patient patient = patientController.getPatient(medication.getPatientID());
            addReminder(medication, patient, context);
        }
    }

    private void addReminder(Medication medication, Patient patient, Context context) {
        Intent alarmIntent = new Intent(context, ReminderBroadcastReceiver.class);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(null);
        uriBuilder.path("com.huettner.dan.videorentalstore.client.applicationlogic.ReminderBroadcastReceiver");
        uriBuilder.appendQueryParameter("patientID", patient.getID());
        uriBuilder.appendQueryParameter("medicationID", medication.getID());
        alarmIntent.setData(uriBuilder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {}
        long millisAtMidnight = c.getTime().getTime();
        long millisSinceMidnight = (medication.getReminderHour() * 60 + medication.getReminderMinute()) * 60 * 1000;
        long timeOfFirstAlarm = millisAtMidnight + millisSinceMidnight;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, timeOfFirstAlarm, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}