package me.bmorris.diningdollars;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bmorris on 3/15/15.
 * Singleton that stores account settings (username, password, balance, starting balance,
 * start date, end date).
 */
public class AccountInfo {

    private static AccountInfo sAccountInfo;
    private static Context mAppContext;

    // String constants for default start and end date
    public static final String DEFAULT_START_DATE = "01/01/2015";
    public static final String DEFAULT_END_DATE = "12/31/2015";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    // String identifiers for SharedPreferences
    public static final String ACCOUNT_USERNAME = "account_username";
    public static final String ACCOUNT_PASSWORD = "account_password";
    private static final String BALANCE = "balance";
    private static final String START_BALANCE = "start balance";
    private static final String START_DATE = "start date";
    private static final String END_DATE = "end date";

    // Private fields
    private String mUsername;
    private String mPassword;
    private double mBalance;
    private double mStartBalance;
    private Date mStartDate;
    private Date mEndDate;
    
    // Class level SharedPreferences for easy saving
    SharedPreferences mSharedPreferences;


    // JSON fields (Not used (yet))
    private static final String JSON_STARTDATE = "start_date";
    private static final String JSON_ENDDATE = "end_date";
    private static final String JSON_BALANCE = "balance";

    // Private constructor
    private AccountInfo(Context appContext) {
        mAppContext = appContext;

        mSharedPreferences = mAppContext.getSharedPreferences(this.getClass().getName(),
                Context.MODE_PRIVATE);
        mUsername = mSharedPreferences.getString(ACCOUNT_USERNAME, "");
        mPassword = mSharedPreferences.getString(ACCOUNT_PASSWORD, "");
        mBalance = mSharedPreferences.getInt(BALANCE, 0) / 100.0;
        mStartBalance = mSharedPreferences.getInt(START_BALANCE, 0) / 100.0;

        String startDateString = mSharedPreferences.getString(START_DATE, DEFAULT_START_DATE);
        String endDateString = mSharedPreferences.getString(END_DATE, DEFAULT_END_DATE);
        try {
            mStartDate = DATE_FORMAT.parse(startDateString);
            mEndDate = DATE_FORMAT.parse(endDateString);
        } catch (ParseException pe) {
            Log.e(getTag(), "Error loading start/end dates");
            pe.printStackTrace();
        }
    }

    /**
     * Returns single instance of this class, or creates it.
     * @param c Context of calling
     * @return  Reference to the singleton
     */
    public static AccountInfo get(Context c) {
        if (sAccountInfo == null) {
            sAccountInfo = new AccountInfo(c.getApplicationContext());
        } else {
            mAppContext = c.getApplicationContext();
        }
        return sAccountInfo;
    }

    /**
     * Formats this object to a JSON object.
     * @return  a JSON object representing the data of the account.
     * @throws JSONException
     */
    // TODO fill in with extra data
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_STARTDATE, mStartDate);
        json.put(JSON_ENDDATE, mEndDate);
        json.put(JSON_BALANCE, mBalance);

        return json;
    }

    // Tbh, not really sure what this was.
    public void updateBalance(JSONObject json) throws JSONException {
        if (json.has(JSON_STARTDATE)) {
            String dateStr = json.getString(JSON_STARTDATE);
            // Todo: Parse the string to a new date

        }
        if (json.has(JSON_ENDDATE)) {
            String dateStr = json.getString(JSON_ENDDATE);
            // Todo: Parse the string to a new date
        }
        if (json.has(JSON_BALANCE)) {
            mBalance = Float.parseFloat(json.getString(JSON_BALANCE));
        }
    }

    // Convenience method for printing log tags
    private String getTag() { return "AccountInfo"; }


    /** Getters and setters for account fields. Sets automatically saved to SharedPreferences */
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACCOUNT_USERNAME, mUsername);
        editor.apply();
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACCOUNT_PASSWORD, mPassword);
        editor.apply();
    }

    public double getBalance() {
        return mBalance;
    }

    public void setBalance(double balance) {
        mBalance = balance;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(BALANCE, (int)(mBalance*100));
        editor.apply();
    }

    public double getStartBalance() {
        return mStartBalance;
    }

    public void setStartBalance(double startBalance) {
        mStartBalance = startBalance;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(START_BALANCE, (int)(mStartBalance*100));
        editor.apply();
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(START_DATE, DATE_FORMAT.format(mStartDate));
        editor.apply();
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(END_DATE, DATE_FORMAT.format(mEndDate));
        editor.apply();
    }

}
