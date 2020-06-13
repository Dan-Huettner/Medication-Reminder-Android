package com.huettner.dan.medicationreminder.server.databaseinterface;

import android.provider.BaseColumns;

public final class DatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {}

    /* Inner class that defines the contents of the "patients" table. */
    public static class Patients implements BaseColumns {

        public static final String TABLE_NAME = "patients";
        public static final String COLUMN_NAME_PATIENTNAME = "patientName";

    }

    /* Inner class that defines the contents of the "Medications" table. */
    public static class Medications implements BaseColumns {

        public static final String TABLE_NAME = "medications";
        public static final String COLUMN_NAME_PATIENTID = "patientID";
        public static final String COLUMN_NAME_MEDICATIONNAME = "medicationName";
        public static final String COLUMN_NAME_REMINDERENABLED = "reminderEnabled";
        public static final String COLUMN_NAME_REMINDERHOUR = "reminderHour";
        public static final String COLUMN_NAME_REMINDERMINUTE = "reminderMinute";
        public static final String COLUMN_NAME_REMINDERID = "reminderID";
        public static final String COLUMN_NAME_NOTES = "notes";

    }

    /* Inner class that defines the contents of the "MedicationsTaken" table. */
    public static class MedicationsTaken implements BaseColumns {

        public static final String TABLE_NAME = "medicationsTaken";
        public static final String COLUMN_NAME_MEDICATIONID = "medicationID";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_HOUR = "hour";
        public static final String COLUMN_NAME_MINUTE = "minute";

    }

    /* Inner class that defines the contents of the "settings" table. */
    public static class Settings implements BaseColumns {

        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME_ENABLE_MULTI_PATIENT_MODE = "enableMultiPatientMode";
        public static final String COLUMN_NAME_ENABLE_REMINDER_SOUND = "enableReminderSound";
        public static final String COLUMN_NAME_ENABLE_REMINDER_VIBRATE = "enableReminderVibrate";

    }

}
