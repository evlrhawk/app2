package com.example.evlrhawk.digitaljukebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

/**
 * HostVsGuestDialogFragment
 * Asks the user if they are hosting a party or joining a party and then sets
 * the corresponding Host boolean in MainActivity
 *
 * Written by Christopher Wilson (Tyler Elkington helped a little, I guess)
 */
public class HostVsGuestDialogFragment extends DialogFragment {

    final String TAG = "HostVsGuestDialog";
    final String dialog_Host_Vs_Guest = "Do you want to be a host a guest?";
    final String Host = "Host";
    final String Guest = "Guest";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialog_Host_Vs_Guest);
        builder.setPositiveButton(Guest, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "about to set to true");
                ((MainActivity) getActivity()).setHost(true);
                Log.d(TAG, "just set to true");
            }
        });
        builder.setNegativeButton(Host, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "about to set to false");
                ((MainActivity) getActivity()).setHost(false);
                Log.d(TAG, "just set to false");
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
