package me.bmorris.diningdollars;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by bmorris on 3/15/15.
 * Singleton that stores app-level settings and data like balance, start/end date, etc.
 */
public class AccountInfo {

    private static AccountInfo sAccountInfo;
    private static Context mAppContext;

    // Private fields
    private Date mStartDate;
    private Date mEndDate;
    private double mBalance = 335.0;

    // JSON fields
    private static final String JSON_STARTDATE = "start_date";
    private static final String JSON_ENDDATE = "end_date";
    private static final String JSON_BALANCE = "balance";

    // Private constructor
    private AccountInfo(Context appContext) {
        mAppContext = appContext;
    }

    public static AccountInfo get(Context c) {
        if (sAccountInfo == null) {
            sAccountInfo = new AccountInfo(c.getApplicationContext());
        }
        return sAccountInfo;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_STARTDATE, mStartDate);
        json.put(JSON_ENDDATE, mEndDate);
        json.put(JSON_BALANCE, mBalance);

        return json;
    }

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


    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public double getBalance() {
        return mBalance;
    }

    public void setBalance(Float balance) {
        mBalance = balance;
    }
}
