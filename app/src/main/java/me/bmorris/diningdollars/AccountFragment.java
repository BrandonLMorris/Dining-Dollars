package me.bmorris.diningdollars;

import android.app.Activity;
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

import org.json.JSONException;

/**
 * Created by bmorris on 3/25/15.
 * Fragment for editing account information (specifically balance, semester start/end dates).
 */
public class AccountFragment extends Fragment {

    /**
     * String constants for Intent Extras
     */
    public static final String BALANCE_EXTRA = "me.bmorris.diningdollars.balance_extra";

    /**
     * View fields.
     * To add: Start/end date fields or pickers or something
     */
    EditText mBalanceEdit;
    Button mSaveButton;

    /**
     * Singleton for holding account data. (UNUSED)
     */
    AccountInfo sAccount;

    /**
     * Field that holds the current account balance. Used both to display balance on the EditText
     * view and to pass back to the HomeActivity via result.
     */
    double mBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // To use: get account data singleton object
        //sAccount = AccountInfo.get(getActivity());

        // Get the balance from the Intent that started this (host's) activity.
        // Default value of -999.99 in case something goes horribly wrong...
        Intent intent = getActivity().getIntent();
        mBalance = intent.getDoubleExtra(BALANCE_EXTRA, -999.99);
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
}
