package com.huettner.dan.medicationreminder.client.applicationlogic.entity;

/**
 * Created by Liana on 2018-05-05.
 */

public class Settings {

    private boolean enableMultiPatientMode;
    private boolean enableReminderSound;
    private boolean enableReminderVibrate;

    public Settings(boolean enableMultiPatientMode, boolean enableReminderSound, boolean enableReminderVibrate) {
        this.enableMultiPatientMode = enableMultiPatientMode;
        this.enableReminderSound = enableReminderSound;
        this.enableReminderVibrate = enableReminderVibrate;
   }

   public boolean getEnableMultiPatientMode() {
       return enableMultiPatientMode;
   }

   public boolean getEnableReminderSound() {
       return enableReminderSound;
   }

   public boolean getEnableReminderVibrate() {
       return enableReminderVibrate;
   }

}
