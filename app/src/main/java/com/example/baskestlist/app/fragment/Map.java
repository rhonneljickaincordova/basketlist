package com.example.baskestlist.app.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import com.example.baskestlist.app.R;
import com.example.baskestlist.app.adapter.infowindowadapter;
import com.example.baskestlist.app.helper.MarkerJSONParser;
import com.example.baskestlist.app.helper.RqstHelper;
import com.example.baskestlist.app.model.listMapmodel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rhonnel on 12/17/2014.
 */
public class Map extends Fragment {

    private static final String LOG_TAG = "JsOn ErRoR";

    private static final String SERVICE_URL = "";

    private static final String LOCATION_SERVICE = null;
    private View view;
    private GoogleMap googleMap;
    private Fragment fragment;
    private static View v = null;
    private LocationListener mListener;
    ListView productname;
    infowindowadapter iwa;
    String name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        try {
            v = inflater.inflate(R.layout.map, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if (status == ConnectionResult.SUCCESS) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMyLocationEnabled(true);
      /*    googleMap.setInfoWindowAdapter(new CustomInfoWindowadapter());*/

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View DialogList = getActivity().getLayoutInflater().inflate(R.layout.dialog_list,null);
                    builder.setView(DialogList).setTitle("Choose Product")
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create().show();







                    FragmentManager fragment_dailog_list = getFragmentManager();
                    Dialog_list dialog_list = new Dialog_list();
                    dialog_list.setRetainInstance(true);
                    dialog_list.show(fragment_dailog_list,"dialog");
                }
            });
        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            // deal with error
        } else {
            // maps is not available
        }

        new RetrieveTask().execute();
    }

    // Background task to retrieve locations from remote mysql server
    private class RetrieveTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            String strUrl = "http://192.168.2.195/basketlist/retrieve.php";
            URL url = null;
            StringBuffer sb = new StringBuffer();
            try {
                url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream iStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
                String line = "";
                while( (line = reader.readLine()) != null){
                    sb.append(line);
                }

                reader.close();
                iStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }

    // Background thread to parse the JSON data retrieved from MySQL server
    private class ParserTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{
        @Override
        protected List<HashMap<String,String>> doInBackground(String... params) {
            MarkerJSONParser markerParser = new MarkerJSONParser();
            JSONObject json = null;
            try {
                json = new JSONObject(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<HashMap<String, String>> markersList = markerParser.parse(json);
            return markersList;
        }
        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            for(int i=0; i<result.size();i++){
                HashMap<String, String> marker = result.get(i);
                LatLng latlng = new LatLng(Double.parseDouble(marker.get("lat")),Double.parseDouble(marker.get("lng")));
                name = marker.get("name");
                addMarker(latlng,name);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (v != null) {
            ViewGroup viewGroup = (ViewGroup) v.getParent();
            if (viewGroup != null) {
                viewGroup.removeAllViews();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleMap != null) {
            googleMap = null;
        }
    }

    // Adding marker on the GoogleMaps
    private void addMarker(LatLng latlng , String name) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title(name.toString());
        googleMap.addMarker(markerOptions);
    }

   // Invoking background thread to store the touched location in Remove MySQL server
    private void sendToServer(LatLng latlng) {
        new SaveTask().execute(latlng);
    }

    // Background thread to save the location in remove MySQL server
    private class SaveTask extends AsyncTask<LatLng, Void, Void> {
        @Override
        protected Void doInBackground(LatLng... params) {
            String lat = Double.toString(params[0].latitude);
            String lng = Double.toString(params[0].longitude);

            String strUrl = "http://192.168.2.195/basketlist/save.php";
            URL url = null;
            try {
                url = new URL(strUrl);

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        connection.getOutputStream());

                outputStreamWriter.write(/*"name=" + name +*/"lat=" + lat + "&lng="+lng);
                outputStreamWriter.flush();
                outputStreamWriter.close();

                InputStream iStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";

                while( (line = reader.readLine()) != null){
                    sb.append(line);
                }
                reader.close();
                iStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
    class CustomInfoWindowadapter implements GoogleMap.InfoWindowAdapter{

        private  final View  myListcontent;
        CustomInfoWindowadapter() {
            myListcontent = getActivity().getLayoutInflater().inflate(R.layout.list_data_map,null);
            myListcontent.setLayoutParams(new LinearLayout.LayoutParams(500,500,LinearLayout.LayoutParams.WRAP_CONTENT));
        }


        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
      /*     ((TextView)myListcontent.findViewById(R.id.productNameMap)).setText("name");
           return myListcontent;
       */
            productname = ((ListView)myListcontent.findViewById(R.id.list_data_map));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("RESULT", "-first");
                    String res = RqstHelper.SendHttpPostRequest1("http://192.168.2.195/basketlist/gallery_get_product.php");
                    Log.e("RESULT", "-second-" + res);
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
                        productname.setAdapter(iwa);
                        productname.invalidate();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.e("ERROR JSON PARSE", e.getMessage().toString());
                    }
                }
            }).start();

            return myListcontent;
        }
    }


}