package com.drei.renovarapp.Models;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Checkout extends AsyncTask<String,Void,String> {

    private static final String TAG = "Checkout";
    private String email;
    private String firstname;
    private String lastname;
    private String street1;
    private String street2;
    private String country;
    private String city;
    private String province;
    private String zip;
    private String orders;
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public Checkout(String email, String firstname, String lastname, String street1, String street2, String country, String city, String province, String zip, String orders, AsyncResponse delegate) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.street1 = street1;
        this.street2 = street2;
        this.country = country;
        this.city = city;
        this.province = province;
        this.zip = zip;
        this.orders = orders;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urls = "http://renovar.health/renovarmobile/transaction.php";
        String text = "";
        try {
            URL url = new URL(urls);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();

            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" +URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8");
            data += "&" +URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lastname, "UTF-8");
            data += "&" +URLEncoder.encode("street1", "UTF-8") + "=" + URLEncoder.encode(street1, "UTF-8");
            data += "&" +URLEncoder.encode("street2", "UTF-8") + "=" + URLEncoder.encode(street2, "UTF-8");
            data += "&" +URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8");
            data += "&" +URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
            data += "&" +URLEncoder.encode("province", "UTF-8") + "=" + URLEncoder.encode(province, "UTF-8");
            data += "&" +URLEncoder.encode("zip", "UTF-8") + "=" + URLEncoder.encode(zip, "UTF-8");
            data += "&" +URLEncoder.encode("orders", "UTF-8") + "=" + URLEncoder.encode(orders, "UTF-8");

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bufferedWriter.write(data);
            bufferedWriter.flush();
            int statusCode = httpURLConnection.getResponseCode();
            if (statusCode == 200) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null)
                    sb.append(line).append("\n");

                text = sb.toString();
                bufferedWriter.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(s.replace("\n",""));
        Log.i(TAG, "RESULT : " + s);
    }
}
