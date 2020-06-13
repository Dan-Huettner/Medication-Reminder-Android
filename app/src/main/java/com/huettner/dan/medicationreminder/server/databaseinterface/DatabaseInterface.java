package com.huettner.dan.medicationreminder.server.databaseinterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseInterface {

    private static DatabaseInterface singleton = null;

    // The database helper used to create database objects for database communication.
    private DatabaseHelper databaseHelper;

    private DatabaseInterface(Context context) {
        // Create a new database helper.
        databaseHelper = new DatabaseHelper(context);
    }

    public static DatabaseInterface getSingleton(Context context) {
        if (singleton == null)
            singleton = new DatabaseInterface(context);
        return singleton;
    }

    public Cursor query(String table,
                           String[] columns,
                           String selection,
                           String[] selectionArgs,
                           String groupBy,
                           String having,
                           String orderBy) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public Cursor query(String table,
                           String[] columns,
                           String selection,
                           String[] selectionArgs,
                           String groupBy,
                           String having,
                           String orderBy,
                           String limit) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public long insert(String table,
                       ContentValues values) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long returnValue = database.insert(table, null, values);
        database.close();
        return returnValue;
    }

    public int delete(String table,
                      String whereClause,
                      String[] whereArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        return database.delete(table, whereClause, whereArgs);
    }

    public int update(String table,
                      ContentValues values,
                      String whereClause,
                      String[] whereArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        return database.update(table, values, whereClause, whereArgs);
    }

}
