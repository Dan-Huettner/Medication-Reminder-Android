package com.huettner.dan.medicationreminder.client.userinterface;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import com.huettner.dan.medicationreminder.R;

/**
 * Created by Liana on 2018-05-08.
 */

public class SetMedicationTakenTimeDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public TimePicker timePicker;

    public SetMedicationTakenTimeDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_medication_taken_time_dialog);
        timePicker = (TimePicker) findViewById(R.id.set_medication_taken_time_timepicker);
        yes = (Button) findViewById(R.id.btn_ok3);
        no = (Button) findViewById(R.id.btn_cancel3);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok3:
                if (Build.VERSION.SDK_INT < 23)
                    ((PatientActivity) c).setMedicationTakenTimeCallback(timePicker.getCurrentHour().intValue(), timePicker.getCurrentMinute().intValue());
                else
                    ((PatientActivity) c).setMedicationTakenTimeCallback(timePicker.getHour(), timePicker.getMinute());
                break;
            case R.id.btn_cancel3:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}