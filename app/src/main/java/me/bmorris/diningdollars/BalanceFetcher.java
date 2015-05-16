package me.bmorris.diningdollars;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by bmorris on 4/10/15.
 * Networking wrapper class to retrieve account balance data
 */
public class BalanceFetcher {

    public static final String LOGIN_URL = "https://auburn.managemyid.com/student/login.php";
    public static final String WELCOME_URL = "https://auburn.managemyid.com/student/welcome.php";

    private Double mBalance;

    /**
     * Wrapper method for acquiring the balance from the internet. Abstracts the networking
     * component to facilitate future refactoring and debugging.
     * @param username  the user's username to the account
     * @param password  the user's password to the account
     * @return the user's current balance
     */
    public Double getBalance(String username, String password) {
        new BalanceFetchTask().execute(username, password);
        Log.i("BalanceFetcher.getBalance()", "mBalance: " + mBalance);
        return (mBalance == null ? -1.0 : mBalance);
    }

    /**
     * Sends a GET request method to the specified URL, but doesn't get the result HTML.
     * @param urlArg the URL to send the request
     * @throws Exception
     */
    public static void getNoContent(String urlArg) throws Exception {

        URL url = new URL(urlArg);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        Log.i("BalanceFetcher.getNoContent()", "GET Response code: " + conn.getResponseCode());

    }

    /**
     * Sends a GET request method and keeps the result HTML.
     * @param urlArg the URL to send the request.
     * @return String of the resulting HTML
     * @throws Exception
     */
    public static String getWithContent(String urlArg) throws IOException {

        URL url = new URL(urlArg);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        Log.i("BalanceFetcher.getWithContent()", "GET Response code: " + conn.getResponseCode());

        StringBuilder sb = new StringBuilder();
        Scanner input = new Scanner(conn.getInputStream());
        while (input.hasNextLine()) {
            sb.append(input.nextLine());
        }

        return sb.toString();

    }

    /**
     * Sends a POST request method to the specified URL. Used to log into the website.
     * @param urlArg    the URL to send the request
     * @param parameters    the encoded parameters (username and password) to log in
     * @throws Exception
     */
    public static void sendPost(String urlArg, String parameters) throws Exception {

        URL url = new URL(urlArg);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(parameters);
        writer.close();

        Log.i("BalanceFetcher.sendPost()", "POST Response code: " + conn.getResponseCode());

    }

    /**
     * Helper method to encode the username and password.
     * @param username the username of the user.
     * @param password the password of the user.
     * @return the encoded string of the username and the password.
     * @throws UnsupportedEncodingException
     */
    public static String getParameters(String username, String password)
            throws UnsupportedEncodingException {
        return "user=" + URLEncoder.encode(username, "UTF-8")
                + "&pwd=" + URLEncoder.encode(password, "UTF-8");
    }


    /**
     * Private AsyncTask class to handle the actual balance retrieval from the internet.
     * Not used (currently).
     */
    private class BalanceFetchTask extends AsyncTask<String, Void, Double> {

        protected Double doInBackground(String... strings) {

            // Check the correct number of arguments
            if (strings.length != 2) return null;

            // Turn cookies on
            CookieHandler.setDefault(new CookieManager());

            try {

                // Send a GET to the login page
                getNoContent(LOGIN_URL);

                // Send a POST and log in, using arguments as credentials
                sendPost(LOGIN_URL, getParameters(strings[0], strings[1]));

                // Send a GET to welcome page, store result
                String welcomePage = getWithContent(WELCOME_URL);
                Log.i("BalanceFetchTask.doInBackgroun", "Login: " + welcomePage.contains("Required Participation"));

                // Regex to find the balance in the page
                Pattern r = Pattern.compile("<td>\\$(\\d*\\.\\d*)</td>");
                Matcher m = r.matcher(welcomePage);
                // Eat the first result
                m.find();
                if (!m.find()) {
                    Log.i("BalanceFetcher.doInBackground()", "Regex didn't find the pattern");
                    return null;
                }
                String result = m.group(1);
                Double balance = Double.parseDouble(result);

                Log.i("BalanceFetch.doInBackground()", "Balance: "+balance);

                return balance;

            } catch (Exception e) {
                Log.e("BalanceFetchTask", "Something went horribly wrong");
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Double result) {
            Log.i("BalanceFetcherTask.onPostExecute()", "Result: " + result);
            mBalance = result;
            Log.i("BalanceFetcherTask.onPostExecute()", "mBalance = " + result);
        }
    }
}
