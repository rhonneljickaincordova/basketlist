package com.example.baskestlist.app.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baskestlist.app.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rhonnel on 12/28/2014.
 */

public class UploadProduct extends Fragment {
    private static final int RESULT_OK = 1;
    TextView edate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private Spinner spinner1,spinner2;
    InputStream is = null;
    String result = null;
    HttpClient httpclient;
    HttpPost httpPost;
    HttpResponse response;
    HttpEntity entity;
    ImageView imageCaptured;


    //upload image//
    /**
     * *******  File Path ************
     */
    private String uploadimagePath;
    private String uploadserverURI = null;
    private ImageView imageView;
    ProgressDialog dialog = null;
    final String uploadFileName = "yyyymmdd_hhmmss.jpg";
    TextView messageText;
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uploadproduct, container, false);
        uploadimagePath = Environment.getExternalStorageDirectory().toString() + "/BasketList";
        File uploadImage = new File(uploadimagePath);
        if(!uploadImage.isDirectory()){
            uploadImage.mkdir();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("ACTIVITY_RESULT", ""+ requestCode + " " + resultCode);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.e("IMAGE PATH",fileUri.getPath());
            startUpload(fileUri.getPath());
        }

    }

    private void startUpload(String fileDir){
        Bitmap photo = BitmapFactory.decodeFile(fileDir);
        imageCaptured.setImageBitmap(photo);
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Cursor managedQuery(Uri uir, String[] projection, Object o, Object o1, Object o2) {
        return null;
    }


    public int uploadFile(String sourceFileUri) {

        if (sourceFileUri == null) {

        } else {
            String fileName = sourceFileUri;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (!sourceFile.isFile()) {

                //dialog.dismiss();

                Log.e("uploadFile", "Source File not exist :"
                        + uploadimagePath);

                new Thread(new Runnable() {
                    public void run() {
                        messageText.setText("Source File not exist :"
                                + uploadimagePath);

                    }
                });

                return 0;

            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\" ; filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {

                        new Thread(new Runnable() {
                            public void run() {

                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                        + "C:/xampp/htdocs/basketlist/uploads";


                                messageText.setText(msg);
                                Toast.makeText(getActivity(), "File Upload Complete.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    dialog.dismiss();
                    ex.printStackTrace();

                    new Thread(new Runnable() {
                        public void run() {
                            messageText.setText("MalformedURLException Exception : check script url.");
                            Toast.makeText(getActivity(), "MalformedURLException",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    dialog.dismiss();
                    e.printStackTrace();

                    new Thread(new Runnable() {
                        public void run() {
                            messageText.setText("Got Exception : see logcat ");
                            Toast.makeText(getActivity(), "Got Exception : see logcat ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("Upload file to server Exception", "Exception : "
                            + e.getMessage(), e);
                }
                dialog.dismiss();
                return serverResponseCode;

            } // End else block

        }
        return 0;
    }

    private void addItemsOnSpinner1() {
        spinner1 = (Spinner) getActivity().findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("Category");
        list.add("Produce");
        list.add("Bakery");
        list.add("Canned Goods");
        list.add("Dairy");
        list.add("Frozen");
        list.add("Dry Goods");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
    }
    private void addItemsOnSpinner2() {
        spinner2 = (Spinner) getActivity().findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("Location");
        list.add("Toril Public Market");
        list.add("Bangkerohan Public Market");
        list.add("calinan Pulic Market");
        list.add("Panacan Public Market");
        list.add("Mabini Public Market");
        list.add("Agdao Public Market");
        list.add("Robinson Supermarket");
        list.add("Felcris Supermarket");
        list.add("Davao Central Warehouse Club");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            addItemsOnSpinner1();
            addItemsOnSpinner2();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        upLoadServerUri = "http://192.168.2.195/basketlist/UploadToServer.php";
        imageCaptured = (ImageView) view.findViewById(R.id.imageCaptured);
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);

        final EditText eproductname = (EditText) view.findViewById(R.id.edtproductname);
     /*   final EditText ecategory = (EditText) view.findViewById(R.id.edtcategory);
     */   final EditText eprice = (EditText) view.findViewById(R.id.edtprice);
        final TextView edate = (TextView) view.findViewById(R.id.edtdate);
       /* final EditText elocation = (EditText) view.findViewById(R.id.edtlocation);
*/
        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        Button btnback = (Button) view.findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home home = new Home();
                android.support.v4.app.FragmentManager support = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransactionhome = support.beginTransaction();
                fragmentTransactionhome.replace(R.id.container, home);
                fragmentTransactionhome.addToBackStack(null);
                fragmentTransactionhome.commit();
            }
        });
        Button camera = (Button) view.findViewById(R.id.btncamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image = null;

                try{
                    image = File.createTempFile("" + System.currentTimeMillis(), ".jpg", new File(uploadimagePath));
                } catch (IOException e){
                    e.printStackTrace();
                }
                Log.e("TEMP FILE", image.getPath());
                fileUri = Uri.fromFile(image);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        Button btnsave = (Button) view.findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile(uploadimagePath);


        String productname = eproductname.getText().toString();
        String location = spinner2.getSelectedItem().toString();
        String date = edate.getText().toString();
        String category = spinner1.getSelectedItem().toString();
        String price = eprice.getText().toString();


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("productName", productname));
        nameValuePairs.add(new BasicNameValuePair("category", category));
        nameValuePairs.add(new BasicNameValuePair("price", price));
        nameValuePairs.add(new BasicNameValuePair("location", location));
        nameValuePairs.add(new BasicNameValuePair("date", date));

        try {
            httpclient = new DefaultHttpClient();
            httpPost = new HttpPost("http://192.168.2.195/basketlist/gallery_add_product.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httpPost);
            entity = response.getEntity();
            is = entity.getContent();
            Toast.makeText(getActivity(), "Data Save",
                    Toast.LENGTH_LONG).show();

          /*          eproductname.setText("");
                    elocation.setText("");
                    edate.setText("");

          */
        } catch (Exception e) {

            Log.e("Fail 1", e.toString());
            Toast.makeText(getActivity(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }
        try {

            JSONObject json_data = new JSONObject(result);
            int code = (json_data.getInt("code"));

            if (code == 1) {
                Toast.makeText(getActivity(), "Inserted Successfully",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
            }
        });
    }


}