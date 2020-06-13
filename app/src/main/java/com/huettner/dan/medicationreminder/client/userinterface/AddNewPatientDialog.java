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

public class AddNewPatientDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public AddNewPatientDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_new_patient_dialog);
        yes = (Button) findViewById(R.id.new_patient_btn_ok);
        no = (Button) findViewById(R.id.new_patient_btn_cancel);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public String getNewPatientName() {
        return ((EditText) findViewById(R.id.new_patient_edittext)).getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_patient_btn_ok:
                ((MainActivity) c).addNewPatientDialogOnOkCallback();
                break;
            case R.id.new_patient_btn_cancel:
                ((MainActivity) c).addNewPatientDialogOnCancelCallback();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}