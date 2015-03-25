package me.bmorris.diningdollars;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by bmorris on 3/15/15.
 * Class for managing individual transaction data
 */
public class Transaction {

    // Local fields
    private UUID mId;
    private String mVendor;
    private Float mAmount;
    private Date mDate;
    private String mComment;

    // JSON id fields
    private static final String JSON_ID = "id";
    private static final String JSON_VENDOR = "vendor";
    private static final String JSON_DATE = "date";
    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_COMMENT = "comment";


    // Converts current object to JSON object
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        // Set all the fields here
        // json.put(**id field**, **value field**)
        json.put(JSON_ID, mId);
        json.put(JSON_VENDOR, mVendor);
        json.put(JSON_AMOUNT, mAmount);
        json.put(JSON_DATE, mDate);
        json.put(JSON_COMMENT, mComment);

        return json;
    }

    // Getters and setters

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getVendor() {
        return mVendor;
    }

    public void setVendor(String vendor) {
        mVendor = vendor;
    }

    public Float getAmount() {
        return mAmount;
    }

    public void setAmount(Float amount) {
        mAmount = amount;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}
