package com.huettner.dan.medicationreminder.client.userinterface;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.huettner.dan.medicationreminder.R;
import com.huettner.dan.medicationreminder.client.applicationlogic.ReminderBroadcastReceiver;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.PatientController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.SettingsController;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Medication;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Patient;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Settings;
import com.huettner.dan.medicationreminder.server.databaseinterface.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TITLE = "Select A Patient";
    private static final int PATIENT_CURSOR_LOADER_ID = 0;

    private ArrayList<Patient> patients;
    private ListView patientListView;
    private TextView patientListViewEmpty;
    private ListViewAdapterSingle patientListViewAdapter;
    private SimpleCursorAdapter patientCursorAdapter;
    private ImageButton medicationLogButton;
//    private ImageButton addNewPatientButton;
//    private ImageButton renamePatientButton;
//    private ImageButton removePatientButton;
    private AddNewPatientDialog addNewPatientDialog;
    private RenamePatientDialog renamePatientDialog;
    private RemovePatientDialog removePatientDialog;
    //private LoaderCallbackMethods loaderCallbackMethods;
    private boolean firstSelectionEvent;
    private Patient selectedPatient;
    private PatientController patientController;
    private Activity selfReference;
    private SettingsController settingsController;
    private boolean multiPatientMode;
    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="First";
    private Menu menu;


    private static final int VIDEO_SPINNER_CURSOR_LOADER_ID = 0;
    private static final int UPDATE_BUTTONS_CURSOR_LOADER_ID = 1;

    private Spinner videoSpinner;
    private Button rentButton;
    private Button returnButton;
    private Button practiceStuffButton;
    private SimpleCursorAdapter videoSpinnerAdapter;
//    private LoaderCallbackMethods loaderCallbackMethods;
    private boolean isNotificationSoundServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        dbHelper.getWritableDatabase();

        settingsController = SettingsController.getSingleton(this.getApplicationContext());
        patientController = PatientController.getSingleton(getApplicationContext());
        patients = patientController.getPatients();
        sortPatients();
        patientListView = (ListView) findViewById(R.id.patient_listview);
        patientListViewEmpty = (TextView) findViewById(R.id.patient_listview_empty);

        Settings currentSettings = settingsController.getSettings();
        multiPatientMode = currentSettings.getEnableMultiPatientMode();
        if (!multiPatientMode) {
            Patient patient = patientController.getPatient("1");
            if (patient == null)
                patient = patientController.addPatient(new Patient("", "Patient"));
            openPatientActivity(patient);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        //firstSelectionEvent = true;
        selfReference = this;

        //loaderCallbackMethods = new LoaderCallbackMethods();

        // Set title
        setTitle(TITLE);
        // Create patient selection spinner
        createPatientListView();

        medicationLogButton = (ImageButton) findViewById(R.id.medication_log_button);
        medicationLogButton.setOnClickListener(new MedicationLogButtonClickListener());

//        addNewPatientButton = (ImageButton) findViewById(R.id.addnewpatient_button);
//        addNewPatientButton.setOnClickListener(new AddNewPatientButtonClickListener());

//        renamePatientButton = (ImageButton) findViewById(R.id.renamepatient_button);
//        renamePatientButton.setOnClickListener(new RenamePatientButtonClickListener());

//        removePatientButton = (ImageButton) findViewById(R.id.removepatient_button);
//        removePatientButton.setOnClickListener(new RemovePatientButtonClickListener());

        // Create date selection widget


        // Create medication widget


        // Create edit patient widget

        // Creating the rent button.
//        createRentButton();

        // Creating the return button.
//        createReturnButton();

        // Creating the practice stuff button.
//        createPracticeStuffButton();

        // Creating the video spinner (i.e. drop-down menu) for the video selection.
//        createVideoSpinner();
    }

    private void updateButtons() {
        MenuItem patientRename = menu.findItem(R.id.main_action_rename_patient);
        MenuItem patientRemove = menu.findItem(R.id.main_action_remove_patient);
        if (selectedPatient == null) {
            medicationLogButton.setEnabled(false);
            medicationLogButton.setImageResource(R.drawable.medication_log_disabled_button);

            patientRename.setEnabled(false);
            patientRemove.setEnabled(false);
//            renamePatientButton.setEnabled(false);
//            renamePatientButton.setImageResource(R.drawable.rename_patient_disabled_button);

//            removePatientButton.setEnabled(false);
//            removePatientButton.setImageResource(R.drawable.remove_patient_disabled_button);
        }
        else {
            medicationLogButton.setEnabled(true);
            medicationLogButton.setImageResource(R.drawable.medication_log_button);

            patientRename.setEnabled(true);
            patientRemove.setEnabled(true);
//            renamePatientButton.setEnabled(true);
//            renamePatientButton.setImageResource(R.drawable.rename_patient_button);

//            removePatientButton.setEnabled(true);
//            removePatientButton.setImageResource(R.drawable.remove_patient_button);
        }
    }

    public void addNewPatientDialogOnOkCallback() {
        String newPatientName = addNewPatientDialog.getNewPatientName();
        if (newPatientName != null && newPatientName.length() > 0) {
            Patient newPatient = new Patient("", newPatientName);
            patientController.addPatient(newPatient);
            patients = patientController.getPatients();
            sortPatients();
            createPatientListView();
        }
        addNewPatientDialog.hide();
        addNewPatientDialog = null;
        selectedPatient = null;
        updateButtons();
    }

    public void addNewPatientDialogOnCancelCallback() {
        addNewPatientDialog.hide();
        addNewPatientDialog = null;
    }

    public void renamePatientDialogOnOkCallback() {
        String newPatientName = renamePatientDialog.getNewPatientName();
        if (newPatientName != null && newPatientName.length() > 0) {
            Patient oldPatient = selectedPatient;
            Patient newPatient = new Patient(selectedPatient.getID(),
                                             newPatientName);
            patientController.setPatient(newPatient);
            patients = patientController.getPatients();
            sortPatients();
            selectedPatient = newPatient;
            createPatientListView();
        }
        renamePatientDialog.hide();
        renamePatientDialog = null;
        selectedPatient = null;
        updateButtons();
    }

    public void renamePatientDialogOnCancelCallback() {
        renamePatientDialog.hide();
        renamePatientDialog = null;
    }

    private void removeReminder(Medication medication) {
        Intent alarmIntent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(null);
        uriBuilder.path("com.huettner.dan.videorentalstore.client.applicationlogic.ReminderBroadcastReceiver");
        uriBuilder.appendQueryParameter("patientID", selectedPatient.getID());
        uriBuilder.appendQueryParameter("medicationID", medication.getID());
        alarmIntent.setData(uriBuilder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    public void removePatientDialogOnOkCallback() {
        ArrayList<Medication> medications = MedicationController.getSingleton(getApplicationContext()).getMedications(selectedPatient.getID());
        if (medications != null) {
            for (int i = 0; i < medications.size(); i++)
                if (medications.get(i).getReminderEnabled())
                    removeReminder(medications.get(i));
        }
        patientController.deletePatient(selectedPatient.getID());
        selectedPatient = null;
        patients = patientController.getPatients();
        sortPatients();
        createPatientListView();
        removePatientDialog.hide();
        removePatientDialog = null;
        updateButtons();
    }

    public void removePatientDialogOnCancelCallback() {
        removePatientDialog.hide();
        removePatientDialog = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_action_add_patient:
                addNewPatientDialog = new AddNewPatientDialog(selfReference);
                addNewPatientDialog.show();
                return true;
            case R.id.main_action_rename_patient:
                renamePatientDialog = new RenamePatientDialog(selfReference, selectedPatient.getName());
                renamePatientDialog.show();
                return true;
            case R.id.main_action_remove_patient:
                removePatientDialog = new RemovePatientDialog(selfReference);
                removePatientDialog.show();
                return true;
            case R.id.main_action_settings:
                openSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        this.menu = menu;
        updateButtons();
        return true;
    }

    //@Override
    //protected void onStart() {
        //super.onStart();
        // Bind to LocalService
        //Intent intent = new Intent(this, NotificationSoundService.class);
        //bindService(intent, notificationSoundServiceConnection, Context.BIND_AUTO_CREATE);
    //@Override
    //protected void onStop() {
        //super.onStop();
        // Unbind from the service
        //if (isNotificationSoundServiceBound) {
            //unbindService(notificationSoundServiceConnection);
            //isNotificationSoundServiceBound = false;
        //}
    //}
/*
    private void createRentButton() {
        // Recall that "View" is the superclass of all GUI elements; hence, a "View" object is a GUI element.
        // Many of the View objects for this Activity, including this button, are defined in the XML file
        // (activity_main.xml) for this Activity. Each such object is created when the constructor for this
        // Activity calls the setContentView(int layoutResourceID) method.
        //
        // You can call the findViewByID(R.id.view_name) method to obtain any of these View objects.
        //
        // That been said, we're not actually creating a button here.
        // This Button object was already defined in the activity_main.xml file.
        // Hence, the object for this Button has already been created, as mentioned above.
        // All were doing here is storing a reference to this object in a variable so we can easily
        // access it in the methods below.
        rentButton = (Button) findViewById(R.id.rent_button);
        rentButton.setEnabled(false);
    }

    private void createReturnButton() {
        // Recall that "View" is the superclass of all GUI elements; hence, a "View" object is a GUI element.
        // Many of the View objects for this Activity, including this button, are defined in the XML file
        // (activity_main.xml) for this Activity. Each such object is created when the constructor for this
        // Activity calls the setContentView(int layoutResourceID) method.
        //
        // You can call the findViewByID(R.id.view_name) method to obtain any of these View objects.
        //
        // That been said, we're not actually creating a button here.
        // This Button object was already defined in the activity_main.xml file.
        // Hence, the object for this Button has already been created, as mentioned above.
        // All were doing here is storing a reference to this object in a variable so we can easily
        // access it in the methods below.
        returnButton = (Button) findViewById(R.id.return_button);
        returnButton.setEnabled(false);
    }

    private void createPracticeStuffButton() {
        // Recall that "View" is the superclass of all GUI elements; hence, a "View" object is a GUI element.
        // Many of the View objects for this Activity, including this button, are defined in the XML file
        // (activity_main.xml) for this Activity. Each such object is created when the constructor for this
        // Activity calls the setContentView(int layoutResourceID) method.
        //
        // You can call the findViewByID(R.id.view_name) method to obtain any of these View objects.
        //
        // That been said, we're not actually creating a button here.
        // This Button object was already defined in the activity_main.xml file.
        // Hence, the object for this Button has already been created, as mentioned above.
        // All were doing here is storing a reference to this object in a variable so we can easily
        // access it in the methods below.
        practiceStuffButton = (Button) findViewById(R.id.practice_stuff_button);

        // Set the animation on the button.
        ObjectAnimator practiceStuffButtonAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.button_animation);
        practiceStuffButtonAnimator.setTarget(practiceStuffButton);
        practiceStuffButtonAnimator.start();
    }
*/
    /**
     * Creates the patient spinner (i.e. drop-down menu) for the patient selection.
     */
    private void createPatientListView() {
/*
        // Recall that "View" is the superclass of all GUI elements; hence, a "View" object is a GUI element.
        // Many of the View objects for this Activity, including this spinner, are defined in the XML file
        // (activity_main.xml) for this Activity. Each such object is created when the constructor for this
        // Activity calls the setContentView(int layoutResourceID) method.
        //
        // You can call the findViewByID(R.id.view_name) method to obtain any of these View objects.
        //
        // That been said, we're not actually creating a spinner here.
        // This Spinner object was already defined in the activity_main.xml file.
        // Hence, the object for this Spinner has already been created, as mentioned above.
        // All were doing here is storing a reference to this object in a variable so we can easily
        // access it in the methods below (and we need to add the list of movie titles to it from the database).
        patientListView = (ListView) findViewById(R.id.patient_listview);

        // The column name.
        String[] columnNames = {DatabaseContract.Patients._ID, DatabaseContract.Patients.COLUMN_NAME_PATIENTNAME};
        // The view for the above column.
        int[] viewIDs = {android.R.id.text1};
        // Creating an adapter for populating the spinner with the patients defined in the
        // database's "patients" table.
        patientCursorAdapter = new SimpleCursorAdapter (
                this,
                android.R.layout.activity_list_item,
                null,
                columnNames,
                viewIDs,
                0
        );

        // Prepare the loader.
        getLoaderManager().initLoader(PATIENT_CURSOR_LOADER_ID, null, loaderCallbackMethods);

    }*/


        // Recall that "View" is the superclass of all GUI elements; hence, a "View" object is a GUI element.
        // Many of the View objects for this Activity, including this spinner, are defined in the XML file
        // (activity_main.xml) for this Activity. Each such object is created when the constructor for this
        // Activity calls the setContentView(int layoutResourceID) method.
        //
        // You can call the findViewByID(R.id.view_name) method to obtain any of these View objects.
        //
        // That been said, we're not actually creating a spinner here.
        // This Spinner object was already defined in the activity_main.xml file.
        // Hence, the object for this Spinner has already been created, as mentioned above.
        // All were doing here is storing a reference to this object in a variable so we can easily
        // access it in the methods below (and we need to add the list of movie titles to it from the database).

        list=new ArrayList<HashMap<String,String>>();

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getID().equals("1"))
                continue;
            HashMap<String, String> hashmap = new HashMap<String, String>();
            hashmap.put(FIRST_COLUMN, patients.get(i).getName());
            list.add(hashmap);
        }

        patientListViewAdapter = new ListViewAdapterSingle(this, list);

        // Set the OnSelectionListener.
        patientListView.setOnItemClickListener(new ListViewItemClickListener());
        // Apply the adapter to the spinner.
        patientListView.setAdapter(patientListViewAdapter);
        if (patientListView.getCount() == 0) {
            patientListView.setVisibility(View.INVISIBLE);
            patientListViewEmpty.setVisibility(View.VISIBLE);
        }
        else {
            patientListView.setVisibility(View.VISIBLE);
            patientListViewEmpty.setVisibility(View.INVISIBLE);
        }

        // Prepare the loader.
//        getLoaderManager().initLoader(PATIENT_LISTVIEW_CURSOR_LOADER_ID, null, loaderCallbackMethods);
    }

/*
    public void onClickRentButton(View view) {
        // Disable the buttons.
        rentButton.setEnabled(false);
        returnButton.setEnabled(false);

        if (isNotificationSoundServiceBound)
            notificationSoundService.playNotificationSound();

        // Create the content URI for the database update.
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("content");
        uriBuilder.authority("com.huettner.dan.videorentalstore.server.contentprovider.VideoStoreContentProvider");
        uriBuilder.path("/movies/" + videoSpinner.getSelectedItemId());
        Uri moviesURI = uriBuilder.build();

        // The values to update in the "movies" table for the row pertaining to the movie to rent.
        ContentValues values = new ContentValues();
        values.put("isRented", "y");

        // Defines the SQL WHERE clause.
        String selection = null;
        String[] selectionArgs = null;

        // Request database update.
        getContentResolver().update(moviesURI, values, selection, selectionArgs);
    }

    public void onClickReturnButton(View view) {
        // Disable the buttons.
        rentButton.setEnabled(false);
        returnButton.setEnabled(false);

        if (isNotificationSoundServiceBound)
            notificationSoundService.playNotificationSound();

        // Create the content URI for the database update.
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("content");
        uriBuilder.authority("com.huettner.dan.videorentalstore.server.contentprovider.VideoStoreContentProvider");
        uriBuilder.path("/movies/" + videoSpinner.getSelectedItemId());
        Uri moviesURI = uriBuilder.build();

        // The values to update in the "movies" table for the row pertaining to the movie to return.
        ContentValues values = new ContentValues();
        values.put("isRented", "n");

        // Defines the SQL WHERE clause.
        String selection = null;
        String[] selectionArgs = null;

        // Request database update.
        getContentResolver().update(moviesURI, values, selection, selectionArgs);
    }

    public void onClickPracticeStuffButton(View view) {
        Intent intent = new Intent(this, PatientActivity.class);
        startActivity(intent);
    }

    private void updateButtonsForNewMovieSelection() {
        // Disable both buttons for now.  The appropriate button will be re-enabled once the cursor has been loaded for the query below.
        rentButton.setEnabled(false);
        returnButton.setEnabled(false);

        // Destroy the previous cursor loader.
        getLoaderManager().destroyLoader(UPDATE_BUTTONS_CURSOR_LOADER_ID);

        // Create a new cursor loader.
        getLoaderManager().initLoader(UPDATE_BUTTONS_CURSOR_LOADER_ID, null, loaderCallbackMethods);
    }
*/

    private void openPatientActivity(Patient patient) {
        Intent intent = new Intent(this, PatientActivity.class);
        intent.putExtra("patientID", patient.getID());
        intent.putExtra("caller", "MainActivity");
        startActivityForResult(intent, 1);
    }

    public void patientListViewChangedCallback() {
        patients = patientController.getPatients();
        sortPatients();
        createPatientListView();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        Settings currentSettings = settingsController.getSettings();
        multiPatientMode = currentSettings.getEnableMultiPatientMode();
        if (!multiPatientMode) {
            if (data.getExtras().getString("class").equals("PatientActivity") &&
                data.getExtras().getString("becauseOfSettings").equals("false"))
                finish();
            else {
                Patient patient = patientController.getPatient("1");
                if (patient == null)
                    patient = patientController.addPatient(new Patient("", "single_mode_patient"));
                openPatientActivity(patient);
            }
        }
        else
            patientListViewChangedCallback();
        selectedPatient = null;
        updateButtons();
    }

    private void sortPatients() {
        if (patients.size() <= 1)
            return;
        Patient singleModePatient = patients.get(0);
        patients.remove(0);
        Collections.sort(patients, new PatientsComparator());
        ArrayList<Patient> newPatients = new ArrayList<>();
        newPatients.add(singleModePatient);
        newPatients.addAll(patients);
        patients = newPatients;
    }

    private class PatientsComparator implements Comparator<Patient> {

        @Override
        public int compare(Patient o1, Patient o2) {
            return o1.getName().compareTo(o2.getName());
        }

    }

    /*
    private class LoaderCallbackMethods implements LoaderManager.LoaderCallbacks<Cursor> {

        // This is called when a new Loader needs to be created.
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            CursorLoader cursorLoader;

            switch (id) {

                case PATIENT_CURSOR_LOADER_ID:
                    // Create the content URI for the database query.
                    Uri.Builder listviewUriBuilder = new Uri.Builder();
                    listviewUriBuilder.scheme("content");
                    listviewUriBuilder.authority("com.huettner.dan.videorentalstore.server.contentprovider.VideoStoreContentProvider");
                    listviewUriBuilder.path("/patients");
                    Uri listviewQueryURI = listviewUriBuilder.build();

                    // Define the projection (requested columns) for the database query.
                    String[] listviewProjection = {"_ID", "patientName"};

                    // Create a new CursorLoader that will take care of creating a Cursor for the requested data.
                    cursorLoader = new CursorLoader(getApplicationContext(), listviewQueryURI, listviewProjection, null, null, null);

                    break;
/*
                case UPDATE_BUTTONS_CURSOR_LOADER_ID:
                    // Create the content URI for the database query.
                    Uri.Builder buttonsUriBuilder = new Uri.Builder();
                    buttonsUriBuilder.scheme("content");
                    buttonsUriBuilder.authority("com.huettner.dan.videorentalstore.server.contentprovider.VideoStoreContentProvider");
                    buttonsUriBuilder.path("/movies/" + videoSpinner.getSelectedItemId());
                    Uri buttonsQueryURI = buttonsUriBuilder.build();

                    // Define the projection (requested columns) for the database query.
                    String[] buttonsProjection = {"_ID", "isRented"};

                    // Create a new CursorLoader that will take care of creating a Cursor for the requested data.
                    cursorLoader = new CursorLoader(getApplicationContext(), buttonsQueryURI, buttonsProjection, null, null, null);

                    break;
*/
/*
                default:
                    cursorLoader = null;

            }

            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            switch (loader.getId()) {

                case PATIENT_CURSOR_LOADER_ID:
                    // Check if this is the first time that onLoadFinished() has been called for the
                    // Patient Spinner's Cursor Loader.
                  //  int currentlySelectedListviewItem;
                   // if (patientListViewAdapter.getCursor() == null)
                    ///    currentlySelectedListviewItem = 0;
                   // else
                    //    currentlySelectedListviewItem = patientListView.getSelectedItemPosition();
                    // Swap the new cursor in.
                    // The framework will take care of closing the old cursor once we return.
                    //patientListViewAdapter.swapCursor(data);
//                    patientListView.setSelection(currentlySelectedListviewItem);
                    break;
*/
/*
                case UPDATE_BUTTONS_CURSOR_LOADER_ID:
                    // Enable/disable the buttons, according to whether or not the selected movie is currently rented.
                    data.moveToFirst();
                    if (Long.parseLong(data.getString(0)) == videoSpinner.getSelectedItemId()) {
                        if (data.getString(1).equalsIgnoreCase("n")) {
                            rentButton.setEnabled(true);
                            returnButton.setEnabled(false);
                        } else {
                            rentButton.setEnabled(false);
                            returnButton.setEnabled(true);
                        }
                    }
                    break;
*/
/*
            }

        }

        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            // We need to make sure we are no longer using the cursor anywhere.
            switch (loader.getId()) {

                case PATIENT_CURSOR_LOADER_ID:
                   // patientListViewAdapter.swapCursor(null);
                    break;
*/
/*
                case UPDATE_BUTTONS_CURSOR_LOADER_ID:
                    break;
*/
/*
            }

        }

    }
*/

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedPatient = patients.get(position+1);
            updateButtons();
        }

    }

    private class MedicationLogButtonClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
            openPatientActivity(selectedPatient);
        }

    }
/*
    private class AddNewPatientButtonClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
        }

    }

    private class RenamePatientButtonClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
        }

    }

    private class RemovePatientButtonClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
        }

    }
*/
    /** Defines callbacks for service binding, passed to bindService() */
/*    private ServiceConnection notificationSoundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NotificationSoundService.NotificationSoundServiceBinder binder = (NotificationSoundService.NotificationSoundServiceBinder) service;
            notificationSoundService = binder.getService();
            isNotificationSoundServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isNotificationSoundServiceBound = false;
        }
    };

    private class PowerConnectedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNotificationSoundServiceBound)
                notificationSoundService.playNotificationSound();

        }

    }
*/
}
