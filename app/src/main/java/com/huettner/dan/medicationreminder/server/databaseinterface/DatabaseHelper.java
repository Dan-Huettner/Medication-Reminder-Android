package com.huettner.dan.medicationreminder.server.databaseinterface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "medicationstore";

    private static final String SQL_CREATE_PATIENTS =
            "CREATE TABLE " + DatabaseContract.Patients.TABLE_NAME + " (" +
            DatabaseContract.Patients._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.Patients.COLUMN_NAME_PATIENTNAME + " VARCHAR(255));";

    private static final String SQL_CREATE_MEDICATIONS =
            "CREATE TABLE " + DatabaseContract.Medications.TABLE_NAME + " (" +
            DatabaseContract.Medications._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.Medications.COLUMN_NAME_PATIENTID + " INTEGER, " +
            DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME + " VARCHAR(255), " +
            DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED + " INTEGER, " +
            DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR + " INTEGER, " +
            DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE + " INTEGER, " +
            DatabaseContract.Medications.COLUMN_NAME_REMINDERID + " INTEGER, " +
            DatabaseContract.Medications.COLUMN_NAME_NOTES + " VARCHAR(255)); ";

    private static final String SQL_CREATE_MEDICATIONSTAKEN =
            "CREATE TABLE " + DatabaseContract.MedicationsTaken.TABLE_NAME + " (" +
            DatabaseContract.MedicationsTaken._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID + " INTEGER, " +
            DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE + " REAL, " +
            DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR + " INTEGER, " +
            DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE + " INTEGER);";

    private static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE " + DatabaseContract.Settings.TABLE_NAME + " (" +
                    DatabaseContract.Settings._ID + " INTEGER PRIMARY KEY, " +
                    DatabaseContract.Settings.COLUMN_NAME_ENABLE_MULTI_PATIENT_MODE + " INTEGER," +
                    DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_SOUND + " INTEGER," +
                    DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_VIBRATE + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ONLY CALLED WHEN THE DATABASE DOES NOT EXIST (i.e. the first time the app is run on a given device).
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Patients.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Medications.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MedicationsTaken.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Settings.TABLE_NAME + ";");
        db.execSQL(SQL_CREATE_PATIENTS);
        db.execSQL(SQL_CREATE_MEDICATIONS);
        db.execSQL(SQL_CREATE_MEDICATIONSTAKEN);
        db.execSQL(SQL_CREATE_SETTINGS);
        db.execSQL("insert into patients (patientName) values('single_mode_patient');");
        db.execSQL("insert into settings (" + DatabaseContract.Settings.COLUMN_NAME_ENABLE_MULTI_PATIENT_MODE + ", " +
                DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_SOUND + ", " +
                DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_VIBRATE + ") values(1, 0, 0);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
