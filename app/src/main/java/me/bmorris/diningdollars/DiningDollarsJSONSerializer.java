package me.bmorris.diningdollars;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bmorris on 3/15/15.
 */
public class DiningDollarsJSONSerializer {
    private Context mContext;
    private String mFilename;
    private AccountInfo sAccount;

    public DiningDollarsJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
        sAccount = AccountInfo.get(mContext);
    }

    public void saveTransactions(ArrayList<Transaction> transactions) throws JSONException, IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Transaction t : transactions) {
            array.put(t.toJSON());
        }

        // Write the file to the disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    // Takes the JSON object form of the account balance and writes it to local disc
    public void saveAccountBalance(JSONObject accountBalance) throws JSONException, IOException {
        // Write the file to the disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(accountBalance.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void loadBalance() throws IOException, JSONException {
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONObject obj = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();
            sAccount.updateBalance(obj);
        } catch (FileNotFoundException e) {
            // Ignore. Happens on first load
        }
    }
}
