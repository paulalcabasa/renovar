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

public class Authentication extends AsyncTask<String,Void,String> {

    private static final String TAG = "Authentication";
    
    public interface AsyncResponse {
        void processFinish(String output);
    }

    private String email;
    private String password;
    public AsyncResponse delegate = null;

    public Authentication(String email, String password, AsyncResponse delegate) {
        this.email = email;
        this.password = password;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        String survey_url = "http://renovar.health/renovarmobile/auth.php";
        String text = "";
        try {
            URL url = new URL(survey_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();

            String data =
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
        Log.i(TAG, "AUTHENTICATION RESULT : " + s);
    }
}
