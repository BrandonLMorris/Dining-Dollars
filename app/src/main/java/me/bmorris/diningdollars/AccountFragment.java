package me.bmorris.diningdollars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by bmorris on 3/25/15.
 */
public class AccountFragment extends Fragment {

    EditText mBalanceEdit;
    AccountInfo sAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sAccount = AccountInfo.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, parent, false);

        mBalanceEdit = (EditText) v.findViewById(R.id.balance_edit);
        mBalanceEdit.setText(String.format("%.2f", sAccount.getBalance()));
        mBalanceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
                //mBalanceEdit.setText(String.format("%.2f", sAccount.getBalance()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sAccount.setBalance(Float.parseFloat(s.toString()));
            }
        });

        return v;
    }
}
