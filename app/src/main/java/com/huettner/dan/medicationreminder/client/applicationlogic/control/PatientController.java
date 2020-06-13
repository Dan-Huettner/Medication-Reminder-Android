package com.huettner.dan.medicationreminder.client.applicationlogic.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Patient;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseContract;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseInterface;

import java.util.ArrayList;

/**
 * Created by Liana on 2018-05-06.
 */

public class PatientController {

    private static PatientController singleton;
    private DatabaseInterface databaseInterface;

    private PatientController(Context context) {
        databaseInterface = DatabaseInterface.getSingleton(context);
    }

    public static PatientController getSingleton(Context context) {
        if (singleton == null)
            singleton = new PatientController(context);
        return singleton;
    }

    public ArrayList<Patient> getPatients() {
        String table = DatabaseContract.Patients.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Patients._ID, DatabaseContract.Patients.COLUMN_NAME_PATIENTNAME};
        String selection = "";
        String[] selectionArgs = new String[] {};
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor patientsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        patientsCursor.moveToFirst();
        ArrayList<Patient> patients = new ArrayList<Patient>();
        while (!patientsCursor.isAfterLast()) {
            patients.add(new Patient(patientsCursor.getString(0), patientsCursor.getString(1)));
            patientsCursor.moveToNext();
        }
        return patients;
    }

    public Patient getPatient(String ID) {
        String table = DatabaseContract.Patients.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Patients._ID, DatabaseContract.Patients.COLUMN_NAME_PATIENTNAME};
        String selection = DatabaseContract.Patients._ID + " = ?";
        String[] selectionArgs = new String[] { ID };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor patientsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        patientsCursor.moveToFirst();
        Patient patient;
        if (!patientsCursor.isAfterLast())
            patient = new Patient(patientsCursor.getString(0), patientsCursor.getString(1));
        else
            patient = null;
        return patient;
    }

    public int setPatient(Patient patient) {
        String table = DatabaseContract.Patients.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Patients.COLUMN_NAME_PATIENTNAME, patient.getName());
        String whereClause = DatabaseContract.Patients._ID + " = ?";
        String[] whereArgs = new String[] { patient.getID() };
        return databaseInterface.update(table, values, whereClause, whereArgs);
    }

    public Patient addPatient(Patient patient) {
        String table = DatabaseContract.Patients.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Patients.COLUMN_NAME_PATIENTNAME, patient.getName());
        long newID = databaseInterface.insert(table, values);
        Patient newPatient = new Patient (
                String.valueOf(newID),
                patient.getName()
        );
        return newPatient;
    }

    public boolean deletePatient(String patientID) {
        String table = DatabaseContract.Patients.TABLE_NAME;
        String whereClause = DatabaseContract.Patients._ID + " = ?";
        String[] whereArgs = new String[] { patientID };
        return databaseInterface.delete(table, whereClause, whereArgs) == 1;
    }

}
