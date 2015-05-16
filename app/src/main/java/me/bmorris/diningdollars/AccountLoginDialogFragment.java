package me.bmorris.diningdollars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by bmorris on 5/15/15.
 */
public class AccountLoginDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Construct dialog with the Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Build the dialog and return it
        return builder.create();
    }
}
