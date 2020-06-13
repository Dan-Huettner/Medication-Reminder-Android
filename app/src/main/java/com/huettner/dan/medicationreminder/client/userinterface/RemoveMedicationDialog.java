package com.huettner.dan.medicationreminder.client.userinterface;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.huettner.dan.medicationreminder.R;

/**
 * Created by Liana on 2018-05-08.
 */

public class RemoveMedicationDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public RemoveMedicationDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.remove_patient_dialog);
        yes = (Button) findViewById(R.id.remove_btn_yes);
        no = (Button) findViewById(R.id.remove_btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_btn_yes:
                ((PatientActivity) c).removeMedicationDialogOnOkCallback();
                break;
            case R.id.remove_btn_no:
                ((PatientActivity) c).removeMedicationDialogOnCancelCallback();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}