package com.example.baskestlist.app.adapter;

import android.os.AsyncTask;
import com.example.baskestlist.app.helper.global;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rhonnel on 12/15/2014.
 */
public class fetchdata extends AsyncTask<String, Void, String> {
    private final fetchdataListner listener;
    private String msg;

    public fetchdata (fetchdataListner listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params == null) return null;

        String url = params[0];

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                msg = "No reponse  from the server ";
                return null;
            }
            InputStream is = entity.getContent();
            return streamToString(is);
        } catch (IOException e) {
            msg = "No Network Connection";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String sJson) {
        if (sJson == null) {
            if (listener != null) listener.OnfetchFailures(msg);
            return;
        }

        try {

            JSONArray aJson = new JSONArray(sJson);
            List<global> globals = new ArrayList<global>();

            for (int i = 0; i < aJson.length(); i++) {
                JSONObject object = aJson.getJSONObject(i);
                global app = new global();
                app.setProductname(object.getString("productName"));
                app.setLocation(object.getString("location"));
                app.setDated(object.getString("date"));
                globals.add(app);
            }
            if (listener != null) listener.OnfetchComplete(globals);
        } catch (JSONException e) {
            msg = "Invalid reponse";
            if (listener != null) listener.OnfetchFailures(msg);
        }
    }

    public String streamToString(final InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");

            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw e;
            }
        }
        return builder.toString();
    }
}



