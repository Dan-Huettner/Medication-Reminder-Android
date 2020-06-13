package com.huettner.dan.medicationreminder.client.applicationlogic.entity;

/**
 * Created by Liana on 2018-05-05.
 */

public class Medication {

    private String id;
    private String patientID;
    private String name;
    private boolean reminderEnabled;
    private int reminderHour;
    private int reminderMinute;
    private int reminderID;
    private String notes;

    public Medication(String id, String patientID, String name, boolean reminderEnabled, int reminderHour, int reminderMinute, int reminderID, String notes) {
        this.id = id;
        this.patientID = patientID;
        this.name = name;
        this.reminderEnabled = reminderEnabled;
        this.reminderHour = reminderHour;
        this.reminderMinute = reminderMinute;
        this.reminderID = reminderID;
        this.notes = notes;
    }

    public String getID() {
        return id;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public boolean getReminderEnabled() {
        return reminderEnabled;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public int getReminderMinute() {
        return reminderMinute;
    }

    public int getReminderID() {
        return reminderID;
    }

    public String getNotes() {
        return notes;
    }

}
