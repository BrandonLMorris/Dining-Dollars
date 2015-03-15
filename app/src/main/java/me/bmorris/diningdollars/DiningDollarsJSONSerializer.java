package me.bmorris.diningdollars;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    public DiningDollarsJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Transaction> transactions) throws JSONException, IOException {
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

    public void saveSettings(JSONObject settings) throws JSONException, IOException {
        // Write the file to the disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(settings.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
