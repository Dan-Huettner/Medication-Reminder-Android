package com.huettner.dan.medicationreminder.client.userinterface;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.huettner.dan.medicationreminder.R;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.MedicationController;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.PatientController;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Medication;

import java.util.Random;

public class EditMedicationActivity extends AppCompatActivity {

    private EditText editMedicationNameEditText;
    private CheckedTextView enableReminderCheckedTextView;
    private ScrollableTimePicker medicationReminderTimePicker;
    private EditText medicationNotesEditText;
    private MedicationController medicationController;
    private PatientController patientController;
    private Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medication);

        editMedicationNameEditText = (EditText) findViewById(R.id.edit_medication_name_edittext);
        enableReminderCheckedTextView = (CheckedTextView) findViewById(R.id.enable_reminder_checkedtextview);
        medicationReminderTimePicker = (ScrollableTimePicker) findViewById(R.id.medication_reminder_timepicker);
        medicationNotesEditText = (EditText) findViewById(R.id.edit_medication_notes_edittext);
        medicationController = MedicationController.getSingleton(this.getApplicationContext());
        medication = medicationController.getMedication((String) this.getIntent().getExtras().get("medicationID"));
        patientController = PatientController.getSingleton(this.getApplicationContext());

        editMedicationNameEditText.setText(medication.getName());
        enableReminderCheckedTextView.setChecked(medication.getReminderEnabled());
        enableReminderCheckedTextView.setOnClickListener(new EnableReminderCheckedTextViewOnClickListener());
        medicationReminderTimePicker.setVisibility(medication.getReminderEnabled() ? View.VISIBLE : View.GONE);
        if (Build.VERSION.SDK_INT < 23) {
            medicationReminderTimePicker.setCurrentHour(medication.getReminderHour());
            medicationReminderTimePicker.setCurrentMinute(medication.getReminderMinute());
        }
        else {
            medicationReminderTimePicker.setHour(medication.getReminderHour());
            medicationReminderTimePicker.setMinute(medication.getReminderMinute());
        }
        medicationNotesEditText.setText(medication.getNotes());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.edit_medication_activity_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        setTitle("Medication Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            case R.id.action_settings:
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
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        String ID;
        String patientID;
        String medicationName;
        boolean reminderEnabled;
        int reminderHour;
        int reminderMinute;
        String reminderID;
        String notes;

        ID = medication.getID();
        patientID = medication.getPatientID();
        if (editMedicationNameEditText.getText().toString().length() > 0)
            medicationName = editMedicationNameEditText.getText().toString();
        else
            medicationName = medication.getName();
        reminderEnabled = enableReminderCheckedTextView.isChecked();
        if (Build.VERSION.SDK_INT < 23) {
            reminderHour = medicationReminderTimePicker.getCurrentHour();
            reminderMinute = medicationReminderTimePicker.getCurrentMinute();
        }
        else {
            reminderHour = medicationReminderTimePicker.getHour();
            reminderMinute = medicationReminderTimePicker.getMinute();
        }
        Random rand = new Random();
        reminderID = String.valueOf(rand.nextInt(Integer.MAX_VALUE));
        notes = medicationNotesEditText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("ID", ID);
        intent.putExtra("patientID", patientID);
        intent.putExtra("medicationName", medicationName);
        intent.putExtra("reminderEnabled", reminderEnabled ? "0" : "1");
        intent.putExtra("reminderHour", String.valueOf(reminderHour));
        intent.putExtra("reminderMinute", String.valueOf(reminderMinute));
        intent.putExtra("reminderID", String.valueOf(reminderID));
        intent.putExtra("notes", notes);
        intent.putExtra("class", "EditMedicationActivity");
        setResult(RESULT_OK, intent);
        finish();
    }

    private class EnableReminderCheckedTextViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            enableReminderCheckedTextView.toggle();
            medicationReminderTimePicker.setVisibility(enableReminderCheckedTextView.isChecked() ? View.VISIBLE : View.GONE);
        }

    }

}
