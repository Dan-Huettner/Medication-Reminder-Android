package com.huettner.dan.medicationreminder.client.applicationlogic.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Settings;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseContract;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseInterface;

/**
 * Created by Liana on 2018-05-06.
 */

public class SettingsController {

    private static SettingsController singleton;
    private DatabaseInterface databaseInterface;

    private SettingsController(Context context) {
        databaseInterface = DatabaseInterface.getSingleton(context);
    }

    public static SettingsController getSingleton(Context context) {
        if (singleton == null)
            singleton = new SettingsController(context);
        return singleton;
    }

    public Settings getSettings() {
        String table = DatabaseContract.Settings.TABLE_NAME;
        String[] columnNames = {DatabaseContract.Settings._ID,
                                DatabaseContract.Settings.COLUMN_NAME_ENABLE_MULTI_PATIENT_MODE,
                                DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_SOUND,
                                DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_VIBRATE};
        String selection = "";
        String[] selectionArgs = new String[] {};
        String groupBy = "";
        String having = "";
        String orderBy = "";
        String limit = "1";
        Cursor settingsCursor = databaseInterface.query(table, columnNames, selection, selectionArgs, groupBy, having, orderBy, limit);
        settingsCursor.moveToFirst();
        Settings settings = null;
        if (!settingsCursor.isAfterLast())
            settings = new Settings(Integer.parseInt(settingsCursor.getString(1)) == 0 ? true : false,
                                    Integer.parseInt(settingsCursor.getString(2)) == 0 ? true : false,
                                    Integer.parseInt(settingsCursor.getString(3)) == 0 ? true : false);
        return settings;
    }

    public int setSettings(Settings settings) {
        String table = DatabaseContract.Settings.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Settings.COLUMN_NAME_ENABLE_MULTI_PATIENT_MODE, settings.getEnableMultiPatientMode() ? "0" : "1");
        values.put(DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_SOUND, settings.getEnableReminderSound() ? "0" : "1");
        values.put(DatabaseContract.Settings.COLUMN_NAME_ENABLE_REMINDER_VIBRATE, settings.getEnableReminderVibrate() ? "0" : "1");
        String whereClause = DatabaseContract.Settings._ID + " = ?";
        String[] whereArgs = new String[] { "1" };
        return databaseInterface.update(table, values, whereClause, whereArgs);
    }

}
