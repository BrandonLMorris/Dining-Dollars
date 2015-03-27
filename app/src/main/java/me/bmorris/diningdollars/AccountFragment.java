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
 */
public class AccountFragment extends Fragment {
    public static final String BALANCE_EXTRA = "me.bmorris.diningdollars.balance_extra";
    public static final String RESULT = "result";

    EditText mBalanceEdit;
    AccountInfo sAccount;
    Button mSaveButton;

    double mBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sAccount = AccountInfo.get(getActivity());
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mBalance = intent.getDoubleExtra(BALANCE_EXTRA, 999.99);
        } else {
            mBalance = 0.01;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, parent, false);

        mBalanceEdit = (EditText) v.findViewById(R.id.balance_edit);
        mBalanceEdit.setText(String.format("%.2f", mBalance));
        mBalanceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
                //mBalanceEdit.setText(String.format("%.2f", mBalance));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) mBalance = Double.parseDouble(s.toString());
                else mBalance = 0.0;
            }
        });

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
//        Log.i("AccountFragment", "onPause() called");
//        DiningDollarsJSONSerializer serializer = new DiningDollarsJSONSerializer(getActivity(), "account.json");
//        try {
//            serializer.saveAccountBalance(sAccount.toJSON());
//            Log.i("AccountFragment", "Successfully saved account balance");
//        } catch (Exception e) {
//            Log.d("AccountFragment: ", " Error saving account balance");
//        }
    }

    private void returnResult() {
        Intent result = new Intent(RESULT);
        result.putExtra(BALANCE_EXTRA, mBalance);
        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
    }
}
