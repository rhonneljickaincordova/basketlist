package com.example.baskestlist.app.helper;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Rhonnel on 12/17/2014.
 */
public class RqstHelper  {
    public static String SendHttpPostRequest1(String function) {
        BufferedReader reader = null;
        String data = "";
        // Send data
        try {

            // Defined URL where to send data
            URL url = new URL(function);
            Log.v("send data :--", function);
            // Send POST data request

            StringBuilder builder = new StringBuilder();
            Log.e("StringBuilder","----");
            HttpClient client = new DefaultHttpClient();
            Log.e("HttpClient","----");
            HttpGet httpGet = new HttpGet(function);
            Log.e("ccccc","----");
            HttpResponse response = client.execute(httpGet);
            Log.e("dddd","----");
            StatusLine statusLine = response.getStatusLine();
            Log.e("StatusLine","----");
            int statusCode = statusLine.getStatusCode();
            Log.e("1231231",""+statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
//                Log.e("aaa", "-" + reader.readLine());
                Log.e("bbb", "-" + builder);
                while ((line = reader.readLine()) != null) {
                    Log.e("hgjh", "-" + line);
                    builder.append(line);
                }
            } else {
                Log.e("SERVER CONNECTION", "Failed to read configuration");
            }
            return builder.toString();
        } catch (Exception ex) {
        }

        return "";

    }

}
