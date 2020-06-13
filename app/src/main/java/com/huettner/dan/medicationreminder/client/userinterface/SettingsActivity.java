package com.huettner.dan.medicationreminder.client.userinterface;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.huettner.dan.medicationreminder.R;
import com.huettner.dan.medicationreminder.client.applicationlogic.control.SettingsController;
import com.huettner.dan.medicationreminder.client.applicationlogic.entity.Settings;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat enableMultiPatientModeSwitch;
    private SwitchCompat enableReminderSoundSwitch;
//    private SwitchCompat enableReminderVibrateSwitch;
    private SettingsController settingsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsController = SettingsController.getSingleton(this.getApplicationContext());

        enableMultiPatientModeSwitch = (SwitchCompat) findViewById(R.id.enable_multi_patient_mode_switch);
        enableReminderSoundSwitch = (SwitchCompat) findViewById(R.id.enable_reminder_sound_switch);
//        enableReminderVibrateSwitch = (SwitchCompat) findViewById(R.id.enable_reminder_vibrate_switch);

        Settings currentSettings = settingsController.getSettings();
        enableMultiPatientModeSwitch.setChecked(currentSettings.getEnableMultiPatientMode());
        enableReminderSoundSwitch.setChecked(currentSettings.getEnableReminderSound());
//        enableReminderVibrateSwitch.setChecked(currentSettings.getEnableReminderVibrate());

        enableMultiPatientModeSwitch.setOnCheckedChangeListener(new EnableMultiPatientModeSwitchOnCheckedChangeListener());
        enableReminderSoundSwitch.setOnCheckedChangeListener(new EnableReminderSoundSwitchOnCheckedChangeListener());
//        enableReminderVibrateSwitch.setOnCheckedChangeListener(new EnableReminderVibrateSwitchOnCheckedChangeListener());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_activity_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        setTitle("Settings");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("class", "SettingsActivity");
        setResult(RESULT_OK, intent);
        finish();
    }

    private class EnableMultiPatientModeSwitchOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Settings oldSettings = settingsController.getSettings();
            Settings newSettings = new Settings(isChecked,
                                                oldSettings.getEnableReminderSound(),
                                                oldSettings.getEnableReminderVibrate());
            settingsController.setSettings(newSettings);
        }

    }

    private class EnableReminderSoundSwitchOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Settings oldSettings = settingsController.getSettings();
            Settings newSettings = new Settings(oldSettings.getEnableMultiPatientMode(),
                                                isChecked,
                                                oldSettings.getEnableReminderVibrate());
            settingsController.setSettings(newSettings);
        }

    }
/*
    private class EnableReminderVibrateSwitchOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Settings oldSettings = settingsController.getSettings();
            Settings newSettings = new Settings(oldSettings.getEnableMultiPatientMode(),
                                                oldSettings.getEnableReminderSound(),
                                                isChecked);
            settingsController.setSettings(newSettings);
        }

    }
*/
}
