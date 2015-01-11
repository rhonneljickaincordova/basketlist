package com.example.baskestlist.app.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.baskestlist.app.R;
import com.example.baskestlist.app.adapter.listadapter;
import com.example.baskestlist.app.helper.RqstHelper;
import com.example.baskestlist.app.model.productmodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rhonnel on 12/13/2014.
 */
public class Gallery extends Fragment {
    ListView listView;
    listadapter ladapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        listView = (ListView) getActivity().findViewById(R.id.list);
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Log.e("RESULT", "-first");
                String res = RqstHelper.SendHttpPostRequest1("http://192.168.2.195/basketlist/gallery_get_product.php");
                Log.e("R    ESULT", "-second-" + res);
                return res;
            }
            @Override
            protected void onPostExecute(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    productmodel model = null;
                    ArrayList<productmodel> models = new ArrayList<productmodel>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        Log.e("JSON OBJECT", jsonObj.toString());

                        model = new productmodel();
                        model.pID = jsonObj.getString("product_id");
                        model.productName = jsonObj.getString("productName");
                        model.productLocation = jsonObj.getString("location");
                        model.date = jsonObj.getString("date");
                        models.add(model);
                    }
                    ladapter = new listadapter(getActivity(), models);
                    listView.setAdapter(ladapter);
                    listView.invalidate();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("ERROR JSON PARSE", e.getMessage().toString());
                }
            }
        }.execute();
       super.onActivityCreated(savedInstanceState);
    }


}