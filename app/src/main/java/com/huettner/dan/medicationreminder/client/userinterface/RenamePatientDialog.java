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

public class RenamePatientDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText nameEditText;
    private String oldName;

    public RenamePatientDialog(Activity a, String oldName) {
        super(a);
        this.oldName = oldName;
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rename_patient_dialog);
        nameEditText = (EditText) findViewById(R.id.rename_edittext);
        nameEditText.setText(oldName);
        yes = (Button) findViewById(R.id.rename_btn_ok);
        no = (Button) findViewById(R.id.rename_btn_cancel);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public String getNewPatientName() {
        return nameEditText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rename_btn_ok:
                ((MainActivity) c).renamePatientDialogOnOkCallback();
                break;
            case R.id.rename_btn_cancel:
                ((MainActivity) c).renamePatientDialogOnCancelCallback();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}