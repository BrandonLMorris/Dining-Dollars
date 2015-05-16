package me.bmorris.diningdollars;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by bmorris on 3/15/15.
 * Fragment for containing the Home screen.
 * Displays the account's current balance and budget and is the launch screen for the app
 */
public class HomeFragment extends Fragment {

    /**
     * String constants for intent extras and JSON file name.
     */
    private static final String ACCOUNT_FILE = "account.json";
    private static final String BALANCE = "balance";
    private static final String START_BALANCE = "start balance";
    private static final String START_DATE = "start date";
    private static final String END_DATE = "end date";

    /**
     * Default values for the account.
     */
    private static final double DEFAULT_BALANCE = 1000.0;
    public static final String DEFAULT_START_DATE = "01/01/2015";
    public static final String DEFAULT_END_DATE = "12/31/2015";
    private static final DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Integer constant for intent to update the account's balance (through the AccountActivity)
     */
    public static final int UPDATE_ACCOUNT_REQUEST = 1;

    /**
     * View fields
     */
    private TextView mBalanceView;
    private TextView mBudgetBalanceView;

    /**
     * Holds singleton for account values. (UNUSED)
     */
    private AccountInfo sAccountInfo;


    /**
     * Object for serializing data into JSON format. (UNUSED)
     */
    private DiningDollarsJSONSerializer mSerializer;

    /**
     * Values for the account.
     */
    private double mBalance;
    private double mStartBalance;
    private String mStartDateString;
    private String mEndDateString;
    private Date mStartDate;
    private Date mEndDate;
    private double mBudgetToDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Get the current balance from SharedPreferences. Use the default value if not set up
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        mBalance = sharedPrefs.getInt(BALANCE, (int)(DEFAULT_BALANCE*100)) / 100.00;
        mStartBalance = sharedPrefs.getInt(START_BALANCE, (int)(DEFAULT_BALANCE*100)) / 100.00;

        // Get the start/end date from SharedPreferences. Use SimpleDateFormat to change from String
        mStartDateString = sharedPrefs.getString(START_DATE, DEFAULT_START_DATE);
        mEndDateString = sharedPrefs.getString(END_DATE, DEFAULT_END_DATE);
        try {
            mStartDate = mDateFormat.parse(mStartDateString);
            mEndDate = mDateFormat.parse(mEndDateString);
        } catch (ParseException pe) {
            Log.e(getTag(), "Error loading start/end dates");
            pe.printStackTrace();
        }

        // Use helper method to calculate what the budget says the balance should be
        mBudgetToDate = calculateBudgetToDate(mStartDate, mEndDate, mStartBalance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate the view
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        // To be used: sAccountInfo is Singleton to store Account data
        //             mSerializer is utility class to serialize Account data to JSON
        //sAccountInfo = AccountInfo.get(getActivity());
        //mSerializer = new DiningDollarsJSONSerializer(getActivity(), ACCOUNT_FILE);

        // Get reference to the text view that holds the balance and set its display
        mBalanceView = (TextView)v.findViewById(R.id.balance_display);
        mBalanceView.setText(String.format("$%.2f", mBalance));

        mBudgetBalanceView = (TextView) v.findViewById(R.id.budget_display);
        mBudgetBalanceView.setText(String.format("$%.2f", mBudgetToDate));

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current account info
        outState.putDouble(BALANCE, mBalance);
    }

    /**
     * Makes the options menu that allows user to edit the account data
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Take action specific to the item from the options menu selected
        switch (id) {
            case R.id.action_settings:
                // Start a new Intent to go to the Account edit Screen. Pass int the current
                // balance as an extra to display on the TextEdit. Activity should result with
                // a new balance value.
                Intent i = new Intent(getActivity(), AccountActivity.class);
                i.putExtra(AccountFragment.BALANCE_EXTRA, mBalance);
                i.putExtra(AccountFragment.START_BALANCE_EXTRA, mStartBalance);
                i.putExtra(AccountFragment.START_DATE_EXTRA, mStartDateString);
                i.putExtra(AccountFragment.END_DATE_EXTRA, mEndDateString);
                startActivityForResult(i, UPDATE_ACCOUNT_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If user saved the (new) balance, reset the balance value on this screen
        if (resultCode == Activity.RESULT_OK) {

            // Get the account data via extras, if they exist
            mBalance = data.getDoubleExtra(AccountFragment.BALANCE_EXTRA, -999.99);
            mStartBalance = data.getDoubleExtra(AccountFragment.START_BALANCE_EXTRA, 0.00);
            if (data.hasExtra(AccountFragment.START_DATE_EXTRA)) {
                mStartDateString = data.getStringExtra(AccountFragment.START_DATE_EXTRA);
                try {
                    mStartDate = mDateFormat.parse(mStartDateString);
                } catch (ParseException pe) {
                    Log.e("HomeFragment", "Error parsing the start date from string");
                    pe.printStackTrace();
                }
            }
            if (data.hasExtra(AccountFragment.END_DATE_EXTRA)) {
                mEndDateString = data.getStringExtra(AccountFragment.END_DATE_EXTRA);
                Log.i("HomeFragment", "End date string received from result: " + mEndDateString);
                try {
                    mEndDate = mDateFormat.parse(mEndDateString);
                    Log.i("HomeFragment", "mEndDate: " + mEndDate);
                } catch (ParseException pe) {
                    Log.e("HomeFragment", "Error parsing the end date from string");
                    pe.printStackTrace();
                }
            }

            updateUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveAccountData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // To be used: Load the account info from JSON file.
        //try {
        //    mSerializer.loadBalance();
        //    Log.d(getTag(), " balance successfully loaded");
        //} catch (Exception e) {
        //    Log.d(getTag(), "Error loading the balance");
        //}
        updateUI();
    }

    /**
     * Defines callbacks for hosting activity (UNUSED)
     */
    public interface OnAccountInfoSelectedListener {
        //Container activity must implement this interface
        public void onAccountInfoSelected();
    }


    /**
     * Updates the elements on the screen based on the current values of the member fields
     */
    public void updateUI() {
        mBalanceView = (TextView) getView().findViewById(R.id.balance_display);
        if (mBalanceView != null) mBalanceView.setText(String.format("$%.2f", mBalance));

        mBudgetBalanceView = (TextView) getView().findViewById(R.id.budget_display);
        if (mBudgetBalanceView != null) {
            mBudgetToDate = calculateBudgetToDate(mStartDate, mEndDate, mStartBalance);
            mBudgetBalanceView.setText(String.format("%.2f", mBudgetToDate));
        }
    }

    /**
     * Saves the account data to disk in SystemPreferences. To be used: Serialize the data into JSON
     * here
     */
    private void saveAccountData() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(BALANCE, (int)(mBalance*100));
        editor.putInt(START_BALANCE, (int)(mStartBalance*100));
        editor.putInt(START_DATE, (int)(mStartBalance*100));
        editor.putString(START_DATE, mStartDateString);
        editor.putString(END_DATE, mEndDateString);

        //IntelliJ wants me to use apply() instead of commit()
        editor.apply();

//        DiningDollarsJSONSerializer serializer = new DiningDollarsJSONSerializer(getActivity(), ACCOUNT_FILE);
//        try {
//            serializer.saveAccountBalance(sAccountInfo.toJSON());
//            Log.i(getTag(), "Successfully saved balance to JSON file");
//        } catch (Exception e) {
//            Log.d(getTag(), "Error saving balance to JSON file");
//            e.printStackTrace();
//        }
    }

    /**
     * Helper method to calculate the budget balance given the start and end dates and the current
     * balance.
     * @param start the start date for the semester
     * @param end the end date for the semester
     * @param startBalance the balance at the start of the semester
     * @return the balance at the current date if budgeted evenly
     */
    private double calculateBudgetToDate(Date start, Date end, double startBalance) {
        long diff = mStartDate.getTime() - mEndDate.getTime();
        long totalDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        double budgetPerDay = mStartBalance / totalDays;
        long daysLeft = TimeUnit.DAYS.convert(Calendar.getInstance().getTime().getTime() - mEndDate.getTime() , TimeUnit.MILLISECONDS);
        return daysLeft * budgetPerDay;
    }


}
