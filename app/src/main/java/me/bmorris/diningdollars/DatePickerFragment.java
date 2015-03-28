package me.bmorris.diningdollars;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by bmorris on 3/25/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /** String constant for arugment tag. */
    public static final String IS_START_ARG = "is start argument";

    /** Local field for which date is being set by the dialog */
    boolean mIsStartDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use current date as default for date in the picker
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        // Get the argument for which date is being set
        mIsStartDate = getArguments().getBoolean(IS_START_ARG);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public static DatePickerFragment newInstance(boolean isStartDate) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_START_ARG, isStartDate);
        frag.setArguments(args);
        return frag;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date picked by the user
        try {
            DatePickerCallback callback = (DatePickerCallback) getActivity();
            callback.updateDateCallback(year, month, day, mIsStartDate);
        } catch (ClassCastException e) {
            Log.e("DatePickerFragment", "Hosting activity must implement DatePickerCallback interface");
        }
    }

    /**
     * Callbacks interface for the hosting activity. Activity must implement this method(s) to
     * receive data on updated date.
     */
    public interface DatePickerCallback {
        public void updateDateCallback(int year, int month, int day, boolean isStartDate);
    }

}
