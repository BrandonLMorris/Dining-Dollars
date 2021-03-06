package me.bmorris.diningdollars;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Call the onCreate of our forefathers.
        super.onCreate(savedInstanceState);

        // Set the layout of the generic fragment-holder.
        setContentView(R.layout.activity_generic);

        // Get the fragment manager to add fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        // Set the fragment if nonexistent (like unicorns).
        if (fragment == null) {
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update the fragment UI whenever this activity starts (Really necessary?)
        HomeFragment hf = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        hf.updateUI();
    }

}
