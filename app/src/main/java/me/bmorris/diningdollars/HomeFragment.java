package me.bmorris.diningdollars;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by bmorris on 3/15/15.
 * Fragment for containing the Home screen
 */
public class HomeFragment extends Fragment {
    private TextView mBalanceView;
    private AccountInfo sAccountInfo;
    private OnAccountInfoSelectedListener mAccountListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);
        sAccountInfo = AccountInfo.get(getActivity());

        mBalanceView = (TextView)v.findViewById(R.id.balance_display);
        mBalanceView.setText(String.format("$%.2f", sAccountInfo.getBalance()));

        return v;
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
                mAccountListener.onAccountInfoSelected();
        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnAccountInfoSelectedListener {
        //Container activity must implement this interface
        public void onAccountInfoSelected();
    }

    public void updateUI() {
        mBalanceView = (TextView) getView().findViewById(R.id.balance_display);
        mBalanceView.setText(String.format("$%.2f", sAccountInfo.getBalance()));
    }
}
