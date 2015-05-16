package me.bmorris.diningdollars;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bmorris on 3/25/15.
 * Fragment for editing account information (specifically balance, semester start/end dates).
 */
public class AccountFragment extends Fragment {

    /**
     * String constants for Intent Extras
     */
    public static final String BALANCE_EXTRA = "me.bmorris.diningdollars.balance_extra";
    public static final String START_BALANCE_EXTRA = "me.bmorris.diningdollar.start_balance_extra";
    public static final String START_DATE_EXTRA = "me.bmorris.diningdollars.start_date_extra";
    public static final String END_DATE_EXTRA = "me.bmorris.diningdollars.end_date_extra";

    // String constant for the account username from SharedPreferences
    public static final String ACCOUNT_USERNAME = "account_username";
    public static final String ACCOUNT_PASSWORD = "account_password";

    /**
     * Public date format constant to keep parsing/formatting consistent
     */
    public static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * View fields.
     * To add: Start/end date fields or pickers or something
     */
    EditText mUsernameEdit;
    EditText mPasswordEdit;
    EditText mBalanceEdit;
    Button mUpdateButton;
    EditText mStartBalanceEdit;
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
    double mStartBalance;
    String mStartDateString;
    String mEndDateString;
    Date mStartDate;
    Date mEndDate;

    // Store the username and password of the account
    String mUsername;
    String mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // To use: get account data singleton object
        //sAccount = AccountInfo.get(getActivity());

        // Get the balance from the Intent that started this (host's) activity.
        // Default value of -999.99 in case something goes horribly wrong...
        Intent intent = getActivity().getIntent();
        mBalance = intent.getDoubleExtra(BALANCE_EXTRA, -999.99);
        mStartBalance = intent.getDoubleExtra(START_BALANCE_EXTRA, -999.99);

        // Get the date strings from the intent. If they're there, parse them into date objects.
        // If not, it messed up...
        mStartDateString = intent.getStringExtra(START_DATE_EXTRA);
        mEndDateString = intent.getStringExtra(END_DATE_EXTRA);
        //Log.i("AccountFragment", "Start/end strings: " + mStartDateString + " " + mEndDateString);
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

        // Get the account username from SystemPreferences
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mUsername = sharedPrefs.getString(ACCOUNT_USERNAME, null);
        mPassword = sharedPrefs.getString(ACCOUNT_PASSWORD, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, parent, false);

        /**
         * Reference and listener for the account username EditText. If username
         * is saved fill it in, otherwise leave with hints.
         */
        mUsernameEdit = (EditText) v.findViewById(R.id.username_edit);
        if (mUsername != null) {
            mUsernameEdit.setText(mUsername);
        }
        mUsernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mUsername = s.toString();
                }
            }
        });


        /**
         * Reference and listeners for the account password EditText. Field clears upon selection
         * by the user. Self-fills with the saved password if non-null, hints otherwise.
         */
        mPasswordEdit = (EditText) v.findViewById(R.id.password_edit);
        if (mPassword != null) {
            mPasswordEdit.setText(mPassword);
        }
        mPasswordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordEdit.setText("");
            }
        });
        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(s.toString().length() == 0)) {
                    mPassword = s.toString();
                }
            }
        });


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

        /**
         * The reference and listener for the Update Balance button. Calls network to update the
         * user's balance over the internet via BalanceFetcher class.
         */
        mUpdateButton = (Button) v.findViewById(R.id.update_balance);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername != null && mPassword != null) {
                    new BalanceFetchTask().execute(mUsername, mPassword);
                    updateUI();
                }
            }
        });


        /**
         * The reference and listener for the start balance EditText. Text is set to the
         * 'mStartBalance' value and updated to match text inputted.
         */
        mStartBalanceEdit = (EditText) v.findViewById(R.id.start_balance_edit);
        mStartBalanceEdit.setText(String.format("%.2f", mStartBalance));
        mStartBalanceEdit.addTextChangedListener(new TextWatcher() {
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
                if (s.length() > 0) {
                    mStartBalance = Double.parseDouble(s.toString());
                } else {
                    mStartBalance = 0.0;
                }
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
        saveAccountData();
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

        // Put the account values as extra into the intent
        result.putExtra(BALANCE_EXTRA, mBalance);
        result.putExtra(START_BALANCE_EXTRA, mStartBalance);
        result.putExtra(START_DATE_EXTRA, mStartDateString);
        result.putExtra(END_DATE_EXTRA, mEndDateString);

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
            mStartDateString = (month+1) + "/" + day + "/" + year;

            // Try parsing string into date, catch exception if fails
            try {
                mStartDate = dateFormat.parse(mStartDateString);
            } catch (ParseException pe) {
                Log.e("AccountFragment", "Error updating date: unable to parse mStartDateString");
                pe.printStackTrace();
            }
        } else {
            // Build new string from arguments
            mEndDateString = (month+1) + "/" + day + "/" + year;

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

            mEndDateView = (TextView) v.findViewById(R.id.end_date);
            mEndDateView.setText(mEndDateString);

            mBalanceEdit = (EditText) v.findViewById(R.id.balance_edit);
            mBalanceEdit.setText(Double.toString(mBalance));
        }
    }


    /**
     * Writes the account username and password to SharedPreferences
     */
    private void saveAccountData() {
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if (mUsername != null) {
            editor.putString(ACCOUNT_USERNAME, mUsername);
        }

        if (mPassword != null) {
            editor.putString(ACCOUNT_PASSWORD, mPassword);
        }

        editor.apply();

    }

    private class BalanceFetchTask extends AsyncTask<String, Void, Double> {

        protected Double doInBackground(String... strings) {

            // Check the correct number of arguments
            if (strings.length != 2) return null;

            // Turn cookies on
            CookieHandler.setDefault(new CookieManager());

            try {

                // Send a GET to the login page
                BalanceFetcher.getNoContent(BalanceFetcher.LOGIN_URL);

                // Send a POST and log in, using arguments as credentials
                BalanceFetcher.sendPost(BalanceFetcher.LOGIN_URL, BalanceFetcher.getParameters(strings[0], strings[1]));

                // Send a GET to welcome page, store result
                String welcomePage = BalanceFetcher.getWithContent(BalanceFetcher.WELCOME_URL);
                Log.i("BalanceFetchTask.doInBackgroun", "Login: " + welcomePage.contains("Required Participation"));

                // Regex to find the balance in the page
                Pattern r = Pattern.compile("<td>\\$(\\d*\\.\\d*)</td>");
                Matcher m = r.matcher(welcomePage);
                // Eat the first result
                m.find();
                if (!m.find()) {
                    Log.i("BalanceFetcher.doInBackground()", "Regex didn't find the pattern");
                    return null;
                }
                String result = m.group(1);
                Double balance = Double.parseDouble(result);

                Log.i("BalanceFetch.doInBackground()", "Balance: "+balance);

                return balance;

            } catch (Exception e) {
                Log.e("BalanceFetchTask", "Something went horribly wrong");
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Double result) {
            Log.i("BalanceFetcherTask.onPostExecute()", "Result: " + result);
            mBalance = result;
            Log.i("BalanceFetcherTask.onPostExecute()", "mBalance = " + result);
        }
    }
}
