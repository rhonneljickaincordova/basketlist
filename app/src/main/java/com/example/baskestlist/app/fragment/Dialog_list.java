package com.example.baskestlist.app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.example.baskestlist.app.R;
import com.example.baskestlist.app.adapter.infowindowadapter;
import com.example.baskestlist.app.adapter.listadapter;
import com.example.baskestlist.app.helper.RqstHelper;
import com.example.baskestlist.app.model.listMapmodel;
import com.example.baskestlist.app.model.productmodel;
import org.apache.http.auth.NTUserPrincipal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rhonnel on 1/9/2015.
 */
public class Dialog_list extends DialogFragment implements AdapterView.OnItemClickListener{

    infowindowadapter iwa;
    ListView listView;
    private View view;
    private String res,location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
          view = inflater.inflate(R.layout.dialog_list,container,false);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  /*      dismiss();
        Toast.makeText(getActivity(),listen[position],Toast.LENGTH_SHORT).show();
  */
    }
/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    }
*/

    @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

        listView = (ListView)view.findViewById(R.id.dialog_list);

        listView.setLayoutParams(new LinearLayout.LayoutParams(1000,LinearLayout.LayoutParams.MATCH_PARENT));
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Log.e("RESULT", "-first");
                res = RqstHelper.SendHttpPostRequest1("http://192.168.2.195/basketlist/gallery_get_product.php");
                Log.e("RESULT", "-second-" + res);
                return res;
            }
            @Override
            protected void onPostExecute(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(res);
                    listMapmodel model = null;
                    ArrayList<listMapmodel> models = new ArrayList<listMapmodel>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        Log.e("JSON OBJECT", jsonObj.toString());

                        model = new listMapmodel();
                        model.productNameMap = jsonObj.getString("productName");
                        models.add(model);
                    }
                    iwa = new infowindowadapter(getActivity(), models);
                    listView.setAdapter(iwa);
                    listView.invalidate();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("ERROR JSON PARSE", e.getMessage().toString());
                }
            }
        }.execute();
        super.onViewCreated(view, savedInstanceState);
    }
}
