package me.bmorris.diningdollars;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by bmorris on 3/15/15.
 */
public class TransactionStorage {
    private ArrayList<Transaction> mTransactions;

    private static TransactionStorage sTransactionStorage;
    private Context mAppContext;

    private TransactionStorage(Context appContext) {
        mAppContext = appContext;
        mTransactions = new ArrayList<Transaction>();
    }

    public static TransactionStorage get(Context c) {
        if (sTransactionStorage == null) {
            sTransactionStorage = new TransactionStorage(c.getApplicationContext());
        }
        return sTransactionStorage;
    }

    public ArrayList<Transaction> getTransactions() {
        return mTransactions;
    }

    public Transaction getTransaction(UUID id) {
        for (Transaction t : mTransactions) {
            if (t.getId().equals(id))
                return t;
        }
        return null;
    }


}
