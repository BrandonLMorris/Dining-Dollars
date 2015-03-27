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
 * Fragment for containing the Home screen
 */
public class HomeFragment extends Fragment {
    private static final String ACCOUNT_FILE = "account.json";
    private static final String BALANCE = "balance";
    private static final String START_DATE = "start date";
    private static final String END_DATE = "end date";
    private static final double DEFAULT_BALANCE = 1000.0;
    public static final int UPDATE_BALANCE_REQUEST = 1;

    private TextView mBalanceView;
    private AccountInfo sAccountInfo;
    private OnAccountInfoSelectedListener mAccountListener;
    private DiningDollarsJSONSerializer mSerializer;
    private double mBalance;
    private Date mStartDate;
    private Date mEndDate;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Ensure hosting activity implements the interfaces
        try {
            mAccountListener = (OnAccountInfoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAccountInfoSelectedListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        if (savedInstanceState != null) {
//            // Restore members from previous state
//            mBalance = savedInstanceState.getDouble(BALANCE);
//        } else {
//            // Otherwise set the default value
//            mBalance = DEFAULT_BALANCE;
//            Log.d(getTag(), "savedInstanceState == null");
//        }

        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mBalance = sharedPrefs.getInt(BALANCE, 333) / 100.00;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);
        sAccountInfo = AccountInfo.get(getActivity());
        mSerializer = new DiningDollarsJSONSerializer(getActivity(), ACCOUNT_FILE);

        mBalanceView = (TextView)v.findViewById(R.id.balance_display);
        mBalanceView.setText(String.format("$%.2f", mBalance));

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current account info
        outState.putDouble(BALANCE, mBalance);
        Log.d(getTag(), "onSaveInstanceState(Bundle) called");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                //Toast.makeText(getActivity(), "You pressed something!", Toast.LENGTH_SHORT).show();
                //mAccountListener.onAccountInfoSelected();
                Intent i = new Intent(getActivity(), AccountActivity.class);
                i.putExtra(AccountFragment.BALANCE_EXTRA, mBalance);
                startActivityForResult(i, UPDATE_BALANCE_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getTag(), "onActivityResult called with " + requestCode + " " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            Log.d(getTag(), "onActivityResult with requestCode == Activity.RESULT_OK");
            mBalance = data.getDoubleExtra(AccountFragment.BALANCE_EXTRA, 999.99);
            updateUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveAccountBal();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getTag(), "onResume called from fragment");
        try {
            mSerializer.loadBalance();
            Log.d(getTag(), " balance successfully loaded");
        } catch (Exception e) {
            Log.d(getTag(), "Error loading the balance");
        }
        updateUI();
    }

    public interface OnAccountInfoSelectedListener {
        //Container activity must implement this interface
        public void onAccountInfoSelected();
    }

    public void updateUI() {
        mBalanceView = (TextView) getView().findViewById(R.id.balance_display);
        mBalanceView.setText(String.format("$%.2f", mBalance));
    }

    public void saveAccountBal() {
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
