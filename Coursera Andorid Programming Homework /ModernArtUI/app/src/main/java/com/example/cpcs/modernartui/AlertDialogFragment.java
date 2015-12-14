package com.example.cpcs.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by cpcs on 11/10/15.
 */
// Class that creates the AlertDialog
public class AlertDialogFragment extends DialogFragment {
    private static final String NOT_NOW = "Not Now";
    private static final String VISIT_MOMA = "Visit MOMA";
    private static final String MOMA_URL = "http://www.moma.org";
    private static final String CHOOSER_TITLE = "Choose an application";
    private static final String DIALOG_MESSAGE = "Inspired by the works of artist such as\n"
                                                 + "    Piet Mondrian and Ben Nicholson.\n\n"
                                                 + "             Click below to learn more!";

    public static AlertDialogFragment newInstance() {
        return new AlertDialogFragment();
    }

    // Build AlertDialog using AlertDialog.Builder
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setMessage(DIALOG_MESSAGE)
                .setCancelable(false)
                .setNegativeButton(NOT_NOW,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int id) {
                                dialog.dismiss();

                            }
                        })

                .setPositiveButton(VISIT_MOMA,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int id) {
                                startActivity(Intent.createChooser(
                                        new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(MOMA_URL)),
                                        CHOOSER_TITLE));


                                dialog.dismiss();
                            }
                        })
               .create();


    }
}