package com.huettner.dan.medicationreminder.client.applicationlogic.entity;

import java.util.Date;

/**
 * Created by Liana on 2018-05-05.
 */

public class MedicationTaken {

    private String id;
    private String medicationID;
    private Date date;
    private int hour;
    private int minute;

    public MedicationTaken(String id, String medicationID, Date date, int hour, int minute) {
        this.id = id;
        this.medicationID = medicationID;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    public String getID() {
        return id;
    }

    public String getMedicationID() {
        return medicationID;
    }

    public Date getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

}
