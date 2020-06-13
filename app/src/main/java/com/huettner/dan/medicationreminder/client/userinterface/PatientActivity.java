package com.huettner.dan.medicationreminder.client.userinterface;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.huettner.dan.medicationreminder.R;
import com.huettner.dan.medicationreminder.client.applicationlogic.ReminderBroadcastReceiver;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationTakenController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.PatientController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.SettingsController;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Medication;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.MedicationTaken;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Patient;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class PatientActivity extends AppCompatActivity {

    private Patient patient;
    private PatientController patientController;
    private ArrayList<Medication> medications;
    private MedicationController medicationController;
    private ListView medicationsListView;
    private TextView medicationsListViewEmpty;
    private ListViewAdapterDouble medicationsListViewAdapter;
    private ImageButton addNewMedicationButton;
    private ImageButton removeMedicationButton;
    private ImageButton medicationDetailsButton;
    private boolean itemSelected;
    private boolean multiPatientMode;
    private Menu menu;

    private AddNewMedicationDialog addNewMedicationDialog;
    private RemoveMedicationDialog removeMedicationDialog;

    private ImageButton medicationTakenButton;
    private ImageButton timeButton;
    private Date date;
    private Medication medication;
    private MedicationTakenController medicationTakenController;
    private MedicationTaken medicationTaken;
    private SetMedicationTakenTimeDialog setMedicationTakenTimeDialog;
    private PatientActivity patientActivity;
    private SettingsController settingsController;
    private boolean backBecauseOfSettings;

    private TextView selectedDateTextView;
    private ImageButton prevButton;
    private ImageButton nextButton;

    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String SUBTITLE="Subtitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientActivity = this;
        itemSelected = false;
        setContentView(R.layout.activity_patient);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.patient_activity_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        backBecauseOfSettings = false;

//        addNewMedicationButton = (ImageButton) findViewById(R.id.addmedication_button);
//        removeMedicationButton = (ImageButton) findViewById(R.id.removemedication_button);
//        medicationDetailsButton = (ImageButton) findViewById(R.id.medicationdetails_button);

//        addNewMedicationButton.setOnClickListener(new AddNewMedicationButtonOnClickListener());
//        removeMedicationButton.setOnClickListener(new RemoveMedicationButtonOnClickListener());
//        medicationDetailsButton.setOnClickListener(new MedicationDetailsButtonOnClickListener());

        medicationsListView = (ListView) findViewById(R.id.medications_listview);
        medicationsListViewEmpty = (TextView) findViewById(R.id.medications_listview_empty);
        patientController = PatientController.getSingleton(this.getApplicationContext());
        medicationController = MedicationController.getSingleton(this.getApplicationContext());
        settingsController = SettingsController.getSingleton(this.getApplicationContext());
        patient = patientController.getPatient((String) this.getIntent().getExtras().get("patientID"));
        medications = medicationController.getMedications(patient.getID());

        Settings currentSettings = settingsController.getSettings();
        multiPatientMode = currentSettings.getEnableMultiPatientMode();
        if (multiPatientMode) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        }
        setTitle("Medication Log");
        medicationTakenController = MedicationTakenController.getSingleton(this.getApplicationContext());
        medicationTakenButton = (ImageButton) findViewById(R.id.medication_taken_button);
        timeButton = (ImageButton) findViewById(R.id.adjust_medication_time_taken_button);
        medicationTakenButton.setEnabled(false);
        timeButton.setEnabled(false);
        medicationTakenButton.setOnClickListener(new MedicationTakenButtonClickListener());
        timeButton.setOnClickListener(new TimeButtonClickListener());
        medication = null;
        medicationTaken = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {}
        date = c.getTime();
        prevButton = (ImageButton) findViewById(R.id.prev_day_button);
        nextButton = (ImageButton) findViewById(R.id.next_day_button);
        nextButton.setEnabled(false);
        nextButton.setImageResource(R.drawable.next_date_disabled_button);
        selectedDateTextView = (TextView) findViewById(R.id.selected_date_textview);
        selectedDateTextView.setText((new SimpleDateFormat("yyyy-MM-dd")).format(getDate()));
        prevButton.setOnClickListener(new PrevButtonClickListener());
        nextButton.setOnClickListener(new NextButtonClickListener());
        populateMedicationsListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_activity_menu, menu);
        this.menu = menu;
        updateButtons();
        return true;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    public Date getDate() {
        return date;
    }

    public void setMedicationTakenTimeCallback(int hour, int minute) {
        medicationTaken = new MedicationTaken(medicationTaken.getID(), medication.getID(), date, hour, minute);
        medicationTakenController.setMedicationTaken(medicationTaken);
        medicationTaken = medicationTakenController.getMedicationTaken(medicationTaken.getID());
        populateMedicationsListView();
        medication = null;
        updateButtons();
    }

    private void updateButtons() {
        MenuItem medicationRemove = menu.findItem(R.id.patient_action_remove_medication);
        MenuItem medicationDetails = menu.findItem(R.id.patient_action_medication_details);
        if (date == null || medication == null) {
            medicationTakenButton.setEnabled(false);
            medicationTakenButton.setImageResource(R.drawable.medication_taken_disabled_button);
            timeButton.setEnabled(false);
            timeButton.setImageResource(R.drawable.adjust_medication_time_taken_disabled_button);
        }
        else {
            if (medicationTaken == null) {
                medicationTakenButton.setEnabled(true);
                medicationTakenButton.setImageResource(R.drawable.medication_taken_button);
                timeButton.setEnabled(false);
                timeButton.setImageResource(R.drawable.adjust_medication_time_taken_disabled_button);
            }
            else {
                medicationTakenButton.setEnabled(true);
                medicationTakenButton.setImageResource(R.drawable.medication_taken_undo_button);
                timeButton.setEnabled(true);
                timeButton.setImageResource(R.drawable.adjust_medication_time_taken_button);
            }
        }
        if (medication == null) {
            medicationRemove.setEnabled(false);
            medicationDetails.setEnabled(false);
//            removeMedicationButton.setEnabled(false);
//            removeMedicationButton.setImageResource(R.drawable.remove_medication_disabled_button);
//            medicationDetailsButton.setEnabled(false);
//            medicationDetailsButton.setImageResource(R.drawable.medication_details_disabled_button);
        }
        else {
            medicationRemove.setEnabled(true);
            medicationDetails.setEnabled(true);
//            removeMedicationButton.setEnabled(true);
//            removeMedicationButton.setImageResource(R.drawable.remove_medication_button);
//            medicationDetailsButton.setEnabled(true);
//            medicationDetailsButton.setImageResource(R.drawable.medication_details_button);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            case R.id.patient_action_add_medication:
                addNewMedicationDialog = new AddNewMedicationDialog(patientActivity);
                addNewMedicationDialog.show();
                return true;
            case R.id.patient_action_remove_medication:
                removeMedicationDialog = new RemoveMedicationDialog(patientActivity);
                removeMedicationDialog.show();
                return true;
            case R.id.patient_action_medication_details:
                openMedicationDetailsActivity();
                return true;
            case R.id.patient_action_settings:
                openSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dateChanged() {
        medication = null;
        Date selectedDate = getDate();
        setDateAndMedication(selectedDate, medication);
        populateMedicationsListView();
    }

    private void populateMedicationsListView() {
        list=new ArrayList<HashMap<String,String>>();

        for (int i = 0; i < medications.size(); i++) {
            HashMap<String,String> hashmap = new HashMap<String, String>();
            MedicationTaken currentMedicationTaken = medicationTakenController.getMedicationTaken(medications.get(i).getID(), date);
            hashmap.put(FIRST_COLUMN, medications.get(i).getName());
            if (currentMedicationTaken == null)
                hashmap.put(SECOND_COLUMN, "");
            else {
                String hour = String.valueOf(currentMedicationTaken.getHour());
                if (currentMedicationTaken.getHour() < 10)
                    hour = "0" + String.valueOf(currentMedicationTaken.getHour());
                String minute = String.valueOf(currentMedicationTaken.getMinute());
                if (currentMedicationTaken.getMinute() < 10)
                    minute = "0" + String.valueOf(currentMedicationTaken.getMinute());
                hashmap.put(SECOND_COLUMN, "(Taken " + hour + ":" + minute + ")");
            }
            hashmap.put(SUBTITLE, medications.get(i).getNotes());
            list.add(hashmap);
        }

        medicationsListViewAdapter = new ListViewAdapterDouble(this, list);

        // Set the OnSelectionListener.
        medicationsListView.setOnItemClickListener(new ListViewItemClickListener());
        // Apply the adapter to the spinner.
        medicationsListView.setAdapter(medicationsListViewAdapter);
        if (medicationsListView.getCount() == 0) {
            medicationsListView.setVisibility(View.INVISIBLE);
            medicationsListViewEmpty.setVisibility(View.VISIBLE);
        }
        else {
            medicationsListView.setVisibility(View.VISIBLE);
            medicationsListViewEmpty.setVisibility(View.INVISIBLE);
        }
    }

    private void openMedicationDetailsActivity() {
        Intent intent = new Intent(this, EditMedicationActivity.class);
        intent.putExtra("medicationID", medication.getID());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("class", "PatientActivity");
        if (backBecauseOfSettings) {
            intent.putExtra("becauseOfSettings", "true");
            backBecauseOfSettings = false;
        }
        else
            intent.putExtra("becauseOfSettings", "false");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setDateAndMedication(Date date, Medication medication) {
        this.date = date;
        this.medication = medication;
        if (date != null && medication != null)
            medicationTaken = medicationTakenController.getMedicationTaken(medication.getID(), date);
        else
            medicationTaken = null;
        updateButtons();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (data.getExtras().getString("class").equals("EditMedicationActivity")) {
            String ID = data.getExtras().getString("ID");
            String patientID = data.getExtras().getString("patientID");
            String medicationName = data.getExtras().getString("medicationName");
            boolean reminderEnabled = Integer.parseInt(data.getExtras().getString("reminderEnabled")) == 0;
            int reminderHour = Integer.parseInt(data.getExtras().getString("reminderHour"));
            int reminderMinute = Integer.parseInt(data.getExtras().getString("reminderMinute"));
            int reminderID = Integer.parseInt(data.getExtras().getString("reminderID"));
            String notes = data.getExtras().getString("notes");

            Medication oldMedication = medicationController.getMedication(ID);
            Medication newMedication = new Medication(ID, patientID, medicationName, reminderEnabled, reminderHour, reminderMinute, reminderID, notes);
            medicationController.setMedication(newMedication);
            medicationListViewChangedCallback();
            updateReminders(oldMedication, newMedication);
        }
        Settings newSettings = SettingsController.getSingleton(this.getApplicationContext()).getSettings();
        if (newSettings.getEnableMultiPatientMode() != multiPatientMode) {
            if (!newSettings.getEnableMultiPatientMode()) {
                backBecauseOfSettings = true;
                onBackPressed();
            }
            else {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
                onBackPressed();
            }
        }
    }

    public void medicationListViewChangedCallback() {
        medications = medicationController.getMedications(patient.getID());
        populateMedicationsListView();
        if (medicationsListView.getSelectedItemPosition() < 0)
            setDateAndMedication(getDate(), null);
        else
            setDateAndMedication(getDate(), medications.get(medicationsListView.getSelectedItemPosition()));
    }

    public void addNewMedicationDialogOnOkCallback() {
        String newMedicationName = addNewMedicationDialog.getNewMedicationName();
        if (newMedicationName != null && newMedicationName.length() > 0) {
            Random rand = new Random();
            int reminderID = rand.nextInt(Integer.MAX_VALUE);
            Medication newMedication = new Medication("", patient.getID(), newMedicationName, false, 0, 0, reminderID, "Notes...");
            medicationController.addMedication(newMedication);
            medications = medicationController.getMedications(patient.getID());
            populateMedicationsListView();
        }
        addNewMedicationDialog.hide();
        addNewMedicationDialog = null;
        medication = null;
        updateButtons();
    }

    public void addNewMedicationDialogOnCancelCallback() {
        addNewMedicationDialog.hide();
        addNewMedicationDialog = null;
    }

    public void removeMedicationDialogOnOkCallback() {
        if (medication.getReminderEnabled())
            removeReminder(medication);
        medicationController.deleteMedication(medication.getID());
        medication = null;
        medications = medicationController.getMedications(patient.getID());
        populateMedicationsListView();
        removeMedicationDialog.hide();
        removeMedicationDialog = null;
        updateButtons();
    }

    public void removeMedicationDialogOnCancelCallback() {
        removeMedicationDialog.hide();
        removeMedicationDialog = null;
    }

    private void removeReminder(Medication medication) {
        Intent alarmIntent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(null);
        uriBuilder.path("com.huettner.dan.videorentalstore.client.applicationlogic.ReminderBroadcastReceiver");
        uriBuilder.appendQueryParameter("patientID", patient.getID());
        uriBuilder.appendQueryParameter("medicationID", medication.getID());
        alarmIntent.setData(uriBuilder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        manager.cancel(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(medication.getReminderID());
    }

    private void addReminder(Medication medication) {
        Intent alarmIntent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(null);
        uriBuilder.path("com.huettner.dan.videorentalstore.client.applicationlogic.ReminderBroadcastReceiver");
        uriBuilder.appendQueryParameter("patientID", patient.getID());
        uriBuilder.appendQueryParameter("medicationID", medication.getID());
        alarmIntent.setData(uriBuilder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {}
        long millisAtMidnight = c.getTime().getTime();
        long millisSinceMidnight = (medication.getReminderHour() * 60 + medication.getReminderMinute()) * 60 * 1000;
        long timeOfFirstAlarm = millisAtMidnight + millisSinceMidnight;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, timeOfFirstAlarm, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void updateReminders(Medication oldMedication, Medication newMedication) {
        // REMOVING OLD ALARM, IF THERE WAS ONE
        if (oldMedication.getReminderEnabled())
            removeReminder(oldMedication);

        // CREATING NEW ALARM, IF THERE IS ONE
        if (newMedication.getReminderEnabled())
            addReminder(newMedication);
    }

    private class PrevButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            date = cal.getTime();
            nextButton.setEnabled(true);
            nextButton.setImageResource(R.drawable.next_date_button);
            selectedDateTextView.setText((new SimpleDateFormat("yyyy-MM-dd")).format(getDate()));
            dateChanged();
        }
    }

    private class NextButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, +1);
            date = cal.getTime();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Date today = c.getTime();

            if (date.equals(today) || date.after(today)) {
                nextButton.setEnabled(false);
                nextButton.setImageResource(R.drawable.next_date_disabled_button);
            }
            selectedDateTextView.setText((new SimpleDateFormat("yyyy-MM-dd")).format(getDate()));
            dateChanged();
        }
    }

    private class MedicationTakenButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (medicationTaken == null) {
                Date currentDate = new Date();
                int hour = Integer.parseInt((new SimpleDateFormat("HH")).format(currentDate));
                int minute = Integer.parseInt((new SimpleDateFormat("mm")).format(currentDate));
                medicationTaken = new MedicationTaken("", medication.getID(), date, hour, minute);
                medicationTaken = medicationTakenController.addMedicationTaken(medicationTaken);
                updateReminders(medication, medication);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(medication.getReminderID());
            }
            else {
                medicationTakenController.deleteMedicationTaken(medicationTaken.getID());
                updateReminders(medication, medication);
                medicationTaken = null;
            }
            populateMedicationsListView();
            medication = null;
            updateButtons();
        }

    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            itemSelected = true;
            Date selectedDate = getDate();
            medicationsListView.setSelection(position);
            Medication medication = medications.get(position);
            setDateAndMedication(selectedDate, medication);
        }

    }

    private class TimeButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setMedicationTakenTimeDialog = new SetMedicationTakenTimeDialog(patientActivity);
            setMedicationTakenTimeDialog.show();
        }

    }
/*
    private class AddNewMedicationButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
        }

    }

    private class RemoveMedicationButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
        }

    }

    private class MedicationDetailsButtonOnClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
        }

    }
*/
    private class RemovePatientButtonOnClickListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
            patientController.deletePatient(patient.getID());
            onBackPressed();
        }

    }


}
