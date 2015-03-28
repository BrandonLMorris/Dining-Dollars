package me.bmorris.diningdollars;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by bmorris on 3/25/15.
 * Screen to edit the Account information. Hosts a fragment to do all the work.
 * Imlements the DatePickerCallback interface to handle date updates.
 */
public class AccountActivity extends FragmentActivity
        implements DatePickerFragment.DatePickerCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Call the super implementation like a good little child
        super.onCreate(savedInstanceState);

        // Set the content view to fill with a Fragment
        setContentView(R.layout.activity_generic);

        // Get the fragment manager to add fragment
        FragmentManager fm = getSupportFragmentManager();
        // fragmentContainer is FrameLayout that holds the fragment in XML layout
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        // Set the fragment if not there
        if (fragment == null) {
            fragment = new AccountFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    public void updateDateCallback(int year, int month, int day, boolean isStartDate) {
        AccountFragment frag = (AccountFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        frag.setDate(year, month, day, isStartDate);
    }
}
