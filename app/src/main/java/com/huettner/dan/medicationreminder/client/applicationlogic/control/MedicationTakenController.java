package com.huettner.dan.medicationreminder.client.applicationlogic.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.huettner.dan.medicationreminder.client.applicationlogic.entity.MedicationTaken;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseContract;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Liana on 2018-05-06.
 */

public class MedicationTakenController {

    private static MedicationTakenController singleton;
    private DatabaseInterface databaseInterface;

    private MedicationTakenController(Context context) {
        databaseInterface = DatabaseInterface.getSingleton(context);
    }

    public static MedicationTakenController getSingleton(Context context) {
        if (singleton == null)
            singleton = new MedicationTakenController(context);
        return singleton;
    }

    public ArrayList<MedicationTaken> getMedicationsTaken() {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        String[] columnNames = {DatabaseContract.MedicationsTaken._ID,
                                DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID,
                                DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE,
                                DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR,
                                DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE
        };
        String selection = "";
        String[] selectionArgs = new String[] {};
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationTakensCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationTakensCursor.moveToFirst();
        ArrayList<MedicationTaken> medicationTakens = new ArrayList<MedicationTaken>();
        while (!medicationTakensCursor.isAfterLast()) {
            Date date;
            try {
                date = (new SimpleDateFormat()).parse(medicationTakensCursor.getString(2));
            } catch (ParseException e) {
                date = null;
            }
            medicationTakens.add(new MedicationTaken(medicationTakensCursor.getString(0),
                                                     medicationTakensCursor.getString(1),
                                                     date,
                                                     Integer.parseInt(medicationTakensCursor.getString(3)),
                                                     Integer.parseInt(medicationTakensCursor.getString(4))
            ));
            medicationTakensCursor.moveToNext();
        }
        return medicationTakens;
    }

    public ArrayList<MedicationTaken> getMedicationTakens(String medicationID) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        String[] columnNames = {DatabaseContract.MedicationsTaken._ID,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE
        };
        String selection = DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID + " = ?";
        String[] selectionArgs = new String[] { medicationID };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationTakensCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationTakensCursor.moveToFirst();
        ArrayList<MedicationTaken> medicationTakens = new ArrayList<MedicationTaken>();
        while (!medicationTakensCursor.isAfterLast()) {
            Date date;
            try {
                date = (new SimpleDateFormat()).parse(medicationTakensCursor.getString(2));
            } catch (ParseException e) {
                date = null;
            }
            medicationTakens.add(new MedicationTaken(medicationTakensCursor.getString(0),
                    medicationTakensCursor.getString(1),
                    date,
                    Integer.parseInt(medicationTakensCursor.getString(3)),
                    Integer.parseInt(medicationTakensCursor.getString(4))
            ));
            medicationTakensCursor.moveToNext();
        }
        return medicationTakens;
    }

    public MedicationTaken getMedicationTaken(String medicationID, Date date) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        String[] columnNames = {DatabaseContract.MedicationsTaken._ID,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE
        };
        String selection = DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID + " = " + medicationID + " AND " + DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE + " = " + String.valueOf(date.getTime());
        String[] selectionArgs = new String[] { };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationTakensCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        MedicationTaken medicationTaken;
        if (medicationTakensCursor != null && !medicationTakensCursor.isAfterLast() && medicationTakensCursor.getCount() >= 1) {
            medicationTakensCursor.moveToFirst();
            if (!medicationTakensCursor.isNull(3)) {
                Date dbDate;
                try {
                    dbDate = (new SimpleDateFormat()).parse(medicationTakensCursor.getString(2));
                } catch (ParseException e) {
                    dbDate = null;
                }
                medicationTaken = new MedicationTaken(medicationTakensCursor.getString(0),
                        medicationTakensCursor.getString(1),
                        dbDate,
                        Integer.parseInt(medicationTakensCursor.getString(3)),
                        Integer.parseInt(medicationTakensCursor.getString(4))
                );
            }
            else
                medicationTaken = null;
        }
        else
            medicationTaken = null;
        return medicationTaken;
    }

    public MedicationTaken getMedicationTaken(String ID) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        String[] columnNames = {DatabaseContract.MedicationsTaken._ID,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR,
                DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE
        };
        String selection = DatabaseContract.MedicationsTaken._ID + " = ?";
        String[] selectionArgs = new String[] { ID };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        Cursor medicationTakensCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy);
        medicationTakensCursor.moveToFirst();
        MedicationTaken medicationTaken;
        if (!medicationTakensCursor.isAfterLast()) {
            Date date;
            try {
                date = (new SimpleDateFormat()).parse(medicationTakensCursor.getString(2));
            } catch (ParseException e) {
                date = null;
            }
            medicationTaken = new MedicationTaken(medicationTakensCursor.getString(0),
                    medicationTakensCursor.getString(1),
                    date,
                    Integer.parseInt(medicationTakensCursor.getString(3)),
                    Integer.parseInt(medicationTakensCursor.getString(4)));
        }
        else
            medicationTaken = null;
        return medicationTaken;
    }

    public int setMedicationTaken(MedicationTaken medicationTaken) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID, medicationTaken.getMedicationID());
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE, String.valueOf(medicationTaken.getDate().getTime()));
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR, String.valueOf(medicationTaken.getHour()));
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE, String.valueOf(medicationTaken.getMinute()));
        String whereClause = DatabaseContract.MedicationsTaken._ID + " = ?";
        String[] whereArgs = new String[] { medicationTaken.getID() };
        return databaseInterface.update(table, values, whereClause, whereArgs);
    }

    public MedicationTaken addMedicationTaken(MedicationTaken medicationTaken) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID, medicationTaken.getMedicationID());
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_DATE, String.valueOf(medicationTaken.getDate().getTime()));
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_HOUR, String.valueOf(medicationTaken.getHour()));
        values.put(DatabaseContract.MedicationsTaken.COLUMN_NAME_MINUTE, String.valueOf(medicationTaken.getMinute()));
        long newID = databaseInterface.insert(table, values);
        MedicationTaken newMedicationTaken = new MedicationTaken(
                String.valueOf(newID),
                medicationTaken.getMedicationID(),
                medicationTaken.getDate(),
                medicationTaken.getHour(),
                medicationTaken.getMinute()
        );
        return newMedicationTaken;
    }

    public boolean deleteMedicationTaken(String medicationTakenID) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        String whereClause = DatabaseContract.MedicationsTaken._ID + " = ?";
        String[] whereArgs = new String[] { medicationTakenID };
        return databaseInterface.delete(table, whereClause, whereArgs) == 1;
    }

    public int deleteMedicationTakens(String medicationID) {
        String table = DatabaseContract.MedicationsTaken.TABLE_NAME;
        String whereClause = DatabaseContract.MedicationsTaken.COLUMN_NAME_MEDICATIONID + " = ?";
        String[] whereArgs = new String[] { medicationID };
        return databaseInterface.delete(table, whereClause, whereArgs);
    }

}
