package com.huettner.dan.medicationreminder.client.applicationlogic.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Medication;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseContract;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseInterface;

import java.util.ArrayList;

/**
 * Created by Liana on 2018-05-06.
 */

public class MedicationController {

    private static MedicationController singleton;
    private DatabaseInterface databaseInterface;

    private MedicationController(Context context) {
        databaseInterface = DatabaseInterface.getSingleton(context);
    }

    public static MedicationController getSingleton(Context context) {
        if (singleton == null)
            singleton = new MedicationController(context);
        return singleton;
    }

    public ArrayList<Medication> getMedications() {
        String table = DatabaseContract.Medications.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Medications._ID,
                                DatabaseContract.Medications.COLUMN_NAME_PATIENTID,
                                DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERID,
                                DatabaseContract.Medications.COLUMN_NAME_NOTES
        };
        String selection = "";
        String[] selectionArgs = new String[] {};
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationsCursor.moveToFirst();
        ArrayList<Medication> medications = new ArrayList<Medication>();
        while (!medicationsCursor.isAfterLast()) {
            medications.add(new Medication(medicationsCursor.getString(0),
                                           medicationsCursor.getString(1),
                                           medicationsCursor.getString(2),
                                           Integer.parseInt(medicationsCursor.getString(3)) == 0,
                                           Integer.parseInt(medicationsCursor.getString(4)),
                                           Integer.parseInt(medicationsCursor.getString(5)),
                                           Integer.parseInt(medicationsCursor.getString(6)),
                                           medicationsCursor.getString(7)));
            medicationsCursor.moveToNext();
        }
        return medications;
    }

    public ArrayList<Medication> getMedications(String patientID) {
        String table = DatabaseContract.Medications.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Medications._ID,
                                DatabaseContract.Medications.COLUMN_NAME_PATIENTID,
                                DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERID,
                                DatabaseContract.Medications.COLUMN_NAME_NOTES
        };
        String selection = DatabaseContract.Medications.COLUMN_NAME_PATIENTID + " = ?";
        String[] selectionArgs = new String[] { patientID };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationsCursor.moveToFirst();
        ArrayList<Medication> medications = new ArrayList<Medication>();
        while (!medicationsCursor.isAfterLast()) {
            medications.add(new Medication(medicationsCursor.getString(0),
                                           medicationsCursor.getString(1),
                                           medicationsCursor.getString(2),
                                           Integer.parseInt(medicationsCursor.getString(3)) == 0,
                                           Integer.parseInt(medicationsCursor.getString(4)),
                                           Integer.parseInt(medicationsCursor.getString(5)),
                                           Integer.parseInt(medicationsCursor.getString(6)),
                                           medicationsCursor.getString(7)));
            medicationsCursor.moveToNext();
        }
        return medications;
    }

    public ArrayList<Medication> getMedicationsWithReminders() {
        String table = DatabaseContract.Medications.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Medications._ID,
                                DatabaseContract.Medications.COLUMN_NAME_PATIENTID,
                                DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE,
                                DatabaseContract.Medications.COLUMN_NAME_REMINDERID,
                                DatabaseContract.Medications.COLUMN_NAME_NOTES
        };
        String selection = DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED + " = ?";
        String[] selectionArgs = new String[] { "0" };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationsCursor.moveToFirst();
        ArrayList<Medication> medications = new ArrayList<Medication>();
        while (!medicationsCursor.isAfterLast()) {
            medications.add(new Medication(medicationsCursor.getString(0),
                                           medicationsCursor.getString(1),
                                           medicationsCursor.getString(2),
                                           Integer.parseInt(medicationsCursor.getString(3)) == 0,
                                           Integer.parseInt(medicationsCursor.getString(4)),
                                           Integer.parseInt(medicationsCursor.getString(5)),
                                           Integer.parseInt(medicationsCursor.getString(6)),
                                           medicationsCursor.getString(7)));
            medicationsCursor.moveToNext();
        }
        return medications;
    }

    public Medication getMedication(String ID) {
        String table = DatabaseContract.Medications.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Medications._ID,
                DatabaseContract.Medications.COLUMN_NAME_PATIENTID,
                DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME,
                DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED,
                DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR,
                DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE,
                DatabaseContract.Medications.COLUMN_NAME_REMINDERID,
                DatabaseContract.Medications.COLUMN_NAME_NOTES
        };
        String selection = DatabaseContract.Medications._ID + " = ?";
        String[] selectionArgs = new String[] { ID };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationsCursor.moveToFirst();
        Medication medication;
        if (!medicationsCursor.isAfterLast())
            medication = new Medication(medicationsCursor.getString(0),
                    medicationsCursor.getString(1),
                    medicationsCursor.getString(2),
                    Integer.parseInt(medicationsCursor.getString(3)) == 0,
                    Integer.parseInt(medicationsCursor.getString(4)),
                    Integer.parseInt(medicationsCursor.getString(5)),
                    Integer.parseInt(medicationsCursor.getString(6)),
                    medicationsCursor.getString(7));
        else
            medication = null;
        return medication;
    }

    public int setMedication(Medication medication) {
        String table = DatabaseContract.Medications.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Medications.COLUMN_NAME_PATIENTID, medication.getPatientID());
        values.put(DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME, medication.getName());
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED, medication.getReminderEnabled() ? 0 : 1);
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR, String.valueOf(medication.getReminderHour()));
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE, String.valueOf(medication.getReminderMinute()));
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERID, String.valueOf(medication.getReminderID()));
        values.put(DatabaseContract.Medications.COLUMN_NAME_NOTES, String.valueOf(medication.getNotes()));
        String whereClause = DatabaseContract.Medications._ID + " = ?";
        String[] whereArgs = new String[] { medication.getID() };
        return databaseInterface.update(table, values, whereClause, whereArgs);
    }

    public Medication addMedication(Medication medication) {
        String table = DatabaseContract.Medications.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Medications.COLUMN_NAME_PATIENTID, medication.getPatientID());
        values.put(DatabaseContract.Medications.COLUMN_NAME_MEDICATIONNAME, medication.getName());
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERENABLED, medication.getReminderEnabled() ? "0" : "1");
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERHOUR, String.valueOf(medication.getReminderHour()));
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERMINUTE, String.valueOf(medication.getReminderMinute()));
        values.put(DatabaseContract.Medications.COLUMN_NAME_REMINDERID, String.valueOf(medication.getReminderID()));
        values.put(DatabaseContract.Medications.COLUMN_NAME_NOTES, medication.getNotes());
        long newID = databaseInterface.insert(table, values);
        Medication newMedication = new Medication (
                String.valueOf(newID),
                medication.getPatientID(),
                medication.getName(),
                medication.getReminderEnabled(),
                medication.getReminderHour(),
                medication.getReminderMinute(),
                medication.getReminderID(),
                medication.getNotes()
        );
        return newMedication;
    }

    public boolean deleteMedication(String medicationID) {
        String table = DatabaseContract.Medications.TABLE_NAME;
        String whereClause = DatabaseContract.Medications._ID + " = ?";
        String[] whereArgs = new String[] { medicationID };
        return databaseInterface.delete(table, whereClause, whereArgs) == 1;
    }

}
