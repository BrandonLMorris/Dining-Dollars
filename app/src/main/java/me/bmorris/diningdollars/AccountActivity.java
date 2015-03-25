package me.bmorris.diningdollars;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by bmorris on 3/25/15.
 */
public class AccountActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        // Get the fragment manager to add fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new AccountFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }
}
