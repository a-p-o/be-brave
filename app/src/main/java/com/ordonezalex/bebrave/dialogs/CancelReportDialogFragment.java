package com.ordonezalex.bebrave.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ordonezalex.bebrave.MainActivity;
import com.ordonezalex.bebrave.R;
import com.ordonezalex.bebrave.services.LocationService;

public class CancelReportDialogFragment extends DialogFragment {

    public static final String TAG = "BeBrave";
    private NotificationManager notificationManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        final AlertDialog dialog;
        final Button cancelButton;
        final EditText securityPinEditText;

        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_cancel_report, null))
                .setTitle(R.string.title_dialog_cancel_report)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_report, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Using inline OnClickListener below to perform input validation
                    }
                });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        // Disable cancel button, until the security PIN is filled
        cancelButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        cancelButton.setEnabled(false);

        securityPinEditText = (EditText) dialog.findViewById(R.id.security_pin_edit_text);

        try {
            securityPinEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    if (charSequence.length() == 4) {
                        cancelButton.setEnabled(true);
                    } else {
                        cancelButton.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String securityPin = prefs.getString("pref_pin", getResources().getString(R.string.pref_default_pin));
                String inputPin = securityPinEditText.getText().toString();
                boolean pinIsCorrect = inputPin.equals(securityPin);

                if (pinIsCorrect) {
                    // Cancel report
                    Intent stopLocationServiceIntent = new Intent(getActivity(), LocationService.class);
                    getActivity().stopService(stopLocationServiceIntent);

                    notificationManager =
                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(MainActivity.NOTIFICATION_EMERGENCY_REPORT_ID);

                    dialog.cancel();
                } else {
                    Toast.makeText(getActivity(), "PIN is incorrect. Try again.", Toast.LENGTH_SHORT).show();
                    securityPinEditText.setText("");
                }
            }
        });

        return dialog;
    }
}
