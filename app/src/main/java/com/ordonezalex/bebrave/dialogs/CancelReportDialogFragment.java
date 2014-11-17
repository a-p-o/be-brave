package com.ordonezalex.bebrave.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.ordonezalex.bebrave.R;

/**
 * Created by Sebastian Florez on 11/17/2014.
 */
public class CancelReportDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_cancel_report, null))

                .setPositiveButton(R.string.cancel_report, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){

                    }
                })
                .setTitle(R.string.title_dialog_cancel_report);

        return builder.create();
    }
}
