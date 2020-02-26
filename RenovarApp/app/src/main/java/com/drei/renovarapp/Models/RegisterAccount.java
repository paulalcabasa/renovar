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

public class RegisterAccount extends AsyncTask<String, Void, String> {

    private static final String TAG = "RegisterAccount";


    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate;

    public RegisterAccount(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        String survey_url = "http://renovar.health/renovarmobile/register_account.php";
        String text = "";
        try {
            URL url = new URL(survey_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();

            String data =
                    URLEncoder.encode("fullname", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8");
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
        delegate.processFinish(s);
        Log.i(TAG, "onPostExecute: " + s);
    }
}
