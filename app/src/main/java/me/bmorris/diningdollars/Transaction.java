package me.bmorris.diningdollars;

import org.json.JSONObject;

/**
 * Created by bmorris on 3/15/15.
 */
public class Transaction {

    // JSON id fields
    private static final String JSON_ID = "id";
    private static final String JSON_VENDOR = "vendor";
    private static final String JSON_DATE = "date";
    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_COMMENT = "comment";

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        // Set all the fields here
        // json.put(**id field**, **value field**)

        return json;
    }
}
