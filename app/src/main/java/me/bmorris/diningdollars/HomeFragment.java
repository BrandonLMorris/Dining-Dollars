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

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;

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
    private static final String START_DATE = "start date";
    private static final String END_DATE = "end date";

    /**
     * Default value for the account balance.
     */
    private static final double DEFAULT_BALANCE = 1000.0;

    /**
     * Integer constant for intent to update the account's balance (through the AccountActivity)
     */
    public static final int UPDATE_BALANCE_REQUEST = 1;

    /**
     * View fields
     */
    private TextView mBalanceView;

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
    private Date mStartDate;
    private Date mEndDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Get the current balance from SharedPreferences. Use the default value if not set up
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mBalance = sharedPrefs.getInt(BALANCE, (int)(DEFAULT_BALANCE*100)) / 100.00;
        // Todo: Decimal part of balance lost between sessions

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
                startActivityForResult(i, UPDATE_BALANCE_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If user saved the (new) balance, reset the balance value on this screen
        if (resultCode == Activity.RESULT_OK) {
            mBalance = data.getDoubleExtra(AccountFragment.BALANCE_EXTRA, -999.99);
            updateUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveAccountBalance();
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
     * Updates the balance value on the screen based on 'mBalance' value
     */
    public void updateUI() {
        mBalanceView = (TextView) getView().findViewById(R.id.balance_display);
        mBalanceView.setText(String.format("$%.2f", mBalance));
    }

    /**
     * Saves the account data to disk in SystemPreferences. To be used: Serailize the data into JSON
     * here
     */
    public void saveAccountBalance() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(BALANCE, (int)mBalance*100);
        editor.commit();
//        DiningDollarsJSONSerializer serializer = new DiningDollarsJSONSerializer(getActivity(), ACCOUNT_FILE);
//        try {
//            serializer.saveAccountBalance(sAccountInfo.toJSON());
//            Log.i(getTag(), "Successfully saved balance to JSON file");
//        } catch (Exception e) {
//            Log.d(getTag(), "Error saving balance to JSON file");
//            e.printStackTrace();
//        }
    }


}
