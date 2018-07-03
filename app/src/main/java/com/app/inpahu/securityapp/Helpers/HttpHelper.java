package com.app.inpahu.securityapp.Helpers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    private static final String HOST_URL = "https://security-server-inpahu.herokuapp.com";

    public static String executePost(String urlr, JSONObject data) {

        try {

            URL url = new URL(HOST_URL + urlr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            // is output buffer writter
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            //set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(data.toString());

            // json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();

            //input stream
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            String JsonResponse = buffer.toString();

            //response data
            Log.i("Response",JsonResponse);

            return JsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
