package com.huettner.dan.medicationreminder.client.userinterface;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.huettner.dan.medicationreminder.R;

/**
 * Created by Liana on 2018-05-08.
 */

public class AddNewMedicationDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public AddNewMedicationDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_new_medication_dialog);
        yes = (Button) findViewById(R.id.btn_ok2);
        no = (Button) findViewById(R.id.btn_cancel2);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public String getNewMedicationName() {
        return ((EditText) findViewById(R.id.medication_name_edittext)).getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok2:
                ((PatientActivity) c).addNewMedicationDialogOnOkCallback();
                break;
            case R.id.btn_cancel2:
                ((PatientActivity) c).addNewMedicationDialogOnCancelCallback();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}