package me.bmorris.diningdollars;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bmorris on 3/25/15.
 * Fragment for editing account information (specifically balance, semester start/end dates).
 */
public class AccountFragment extends Fragment {

    /**
     * String constants for Intent Extras
     */
    public static final String BALANCE_EXTRA = "me.bmorris.diningdollars.balance_extra";
    public static final String START_DATE_EXTRA = "me.bmorris.diningdollars.start_date_extra";
    public static final String END_DATE_EXTRA = "me.bmorris.diningdollars.end_date_extra";

    /**
     * Public date format constant to keep parsing/formatting consistent
     */
    public static final DateFormat dateFormat = new SimpleDateFormat("MM/DD/yyyy");

    /**
     * View fields.
     * To add: Start/end date fields or pickers or something
     */
    EditText mBalanceEdit;
    Button mSaveButton;
    TextView mStartDateView;
    TextView mEndDateView;
    Button mStartDateEdit;
    Button mEndDateEdit;

    /**
     * Singleton for holding account data. (UNUSED)
     */
    AccountInfo sAccount;

    /**
     * Fields that hold the current account data. Used both to display on the views and to pass
     * back to the HomeActivity via result.
     */
    double mBalance;
    String mStartDateString;
    String mEndDateString;
    Date mStartDate;
    Date mEndDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // To use: get account data singleton object
        //sAccount = AccountInfo.get(getActivity());

        // Get the balance from the Intent that started this (host's) activity.
        // Default value of -999.99 in case something goes horribly wrong...
        Intent intent = getActivity().getIntent();
        mBalance = intent.getDoubleExtra(BALANCE_EXTRA, -999.99);

        // Get the date strings from the intent. If they're there, parse them into date objects.
        // If not, it messed up...
        mStartDateString = intent.getStringExtra(START_DATE_EXTRA);
        mEndDateString = intent.getStringExtra(END_DATE_EXTRA);
        Log.i("AccountFragment", "Start/end strings: " + mStartDateString + " " + mEndDateString);
        if (mStartDateString != null && mEndDateString != null) {
            try {
                mStartDate = dateFormat.parse(mStartDateString);
                mEndDate = dateFormat.parse(mEndDateString);
            } catch (ParseException pe) {
                Log.e(getTag(), "Parse error converting date strings to Dates");
                pe.printStackTrace();
            }
        } else {
            Log.e(getTag(), "Error loading dates from intent");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, parent, false);

        /**
         * The reference and listener for the balance EditText. Text is set as 'mBalance' value to
         * start and 'mBalance' is updated to reflect the inputted value.
         */
        mBalanceEdit = (EditText) v.findViewById(R.id.balance_edit);
        mBalanceEdit.setText(String.format("%.2f", mBalance));
        mBalanceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This space is intentionally left blank
            }

            @Override
            public void afterTextChanged(Editable s) {
                // If empty string, set balance to 0.00 to avoid a parseDouble() exception
                if(s.length() > 0) mBalance = Double.parseDouble(s.toString());
                else mBalance = 0.0;
            }
        });

         /** Start date text reference */
        mStartDateView = (TextView) v.findViewById(R.id.start_date);
        mStartDateView.setText(mStartDateString);

        /**
         * Start date edit button reference. Set on-click listener to launch a date picker dialog
         * to adjust the date.
         */
        mStartDateEdit = (Button) v.findViewById(R.id.edit_start_state);
        mStartDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(true);
                newFragment.show(getActivity().getSupportFragmentManager(), "startDatePicker");
            }
        });

        /** End date text reference. */
        mEndDateView = (TextView) v.findViewById(R.id.end_date);
        mEndDateView.setText(mEndDateString);

        /**
         * End date edit button reference. Set on-click listener to launch a date picker dialog
         * to adjust the date
         */
        mEndDateEdit = (Button) v.findViewById(R.id.edit_end_date);
        mEndDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(false);
                newFragment.show(getActivity().getSupportFragmentManager(), "endDatePicker");
            }
        });

        /**
         * Save button reference and listener. Hitting the button will send the balance value as
         * result back to the activity that started this one (HomeActivity).
         */
        mSaveButton = (Button) v.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });


        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        // To use??: Save the account info as JSON
//        Log.i("AccountFragment", "onPause() called");
//        DiningDollarsJSONSerializer serializer = new DiningDollarsJSONSerializer(getActivity(), "account.json");
//        try {
//            serializer.saveAccountBalance(sAccount.toJSON());
//            Log.i("AccountFragment", "Successfully saved account balance");
//        } catch (Exception e) {
//            Log.d("AccountFragment: ", " Error saving account balance");
//        }
    }

    /**
     * Returns the balance value as a result to the activity that started this one (HomeActivity).
     */
    private void returnResult() {
        Intent result = new Intent();

        // Put the balance value as extra into the intent
        result.putExtra(BALANCE_EXTRA, mBalance);

        // Set the intent's result as OK, and ship it back
        getActivity().setResult(Activity.RESULT_OK, result);

        // "Always leave things cleaner than when you found them."
        getActivity().finish();
    }

    /**
     * Updates the String and Date member fields that represent semester start/end days for the
     * account. Called by hosting activity as part of callbacks for the DatePicker Dialog. May
     * throw ParseException if trouble converting String dates to Date objects.
     * @param year      The year the updated date is being set to.
     * @param month     The month the updated date is being set to.
     * @param day       The day the updated date is being set to.
     * @param isStartDay    Indicated if the date being updated is the semester start date (true)
     *                      or the semester end date (false).
     */
    public void setDate(int year, int month, int day, boolean isStartDay) {

        // Set the date field based on which date was updated
        if (isStartDay) {
            // Build new string from arguments
            mStartDateString = month + "/" + day + "/" + year;

            // Try parsing string into date, catch exception if fails
            try {
                mStartDate = dateFormat.parse(mStartDateString);
            } catch (ParseException pe) {
                Log.e("AccountFragment", "Error updating date: unable to parse mStartDateString");
                pe.printStackTrace();
            }
        } else {
            // Build new string from arguments
            mEndDateString = month + "/" + day + "/" + year;

            // Try parsing string into date, catch exception if fails
            try {
                mEndDate = dateFormat.parse(mEndDateString);
            } catch (ParseException pe) {
                Log.e("AccountFragment", "Error updating date: unable to parse mEndDateString");
            }
        }

        // Update the UI to reflect the change
        updateUI();
    }


    /**
     * Updates the user interface to match the member field of the account. Helper function to
     * ensure data matches its on-screen representation. Will not work if unable to get the
     * view from the getView() function.
     */
    private void updateUI() {
        // Obtain the view
        View v = getView();

        // Null check the view object to avoid NullPointerException
        if (v != null) {
            // Update both text fields
            mStartDateView = (TextView) v.findViewById(R.id.start_date);
            mStartDateView.setText(mStartDateString);

            mEndDateView = (TextView) v.findViewById(R.id.end_date);
            mEndDateView.setText(mEndDateString);
        }
    }

}
