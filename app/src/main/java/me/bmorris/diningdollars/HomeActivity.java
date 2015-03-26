package me.bmorris.diningdollars;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class HomeActivity extends ActionBarActivity implements HomeFragment.OnAccountInfoSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        // Get the fragment manager to add fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("HomeActivity", "onStart() called");
        HomeFragment hf = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        hf.updateUI();
    }

    public void onAccountInfoSelected() {
        Toast.makeText(getApplicationContext(), "WHOOP WHOOP", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, AccountActivity.class);
        startActivity(i);
    }

}
