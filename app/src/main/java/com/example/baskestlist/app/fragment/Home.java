package com.example.baskestlist.app.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baskestlist.app.R;
import com.example.baskestlist.app.helper.ImageGrabHelper;

/**
 * Created by Rhonnel on 12/20/2014.
 */
public class Home extends Fragment /*implements ActionBar.TabListener */ {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.recent_post);

        mTabHost.addTab(mTabHost.newTabSpec("Recent Post").setIndicator("Recent Post"),
                Recent_post.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Fruits").setIndicator("Fruits"),
                Fruits.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Vegetables").setIndicator("Vegetables"),
                Vegetables.class, null);

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                ImageGrabHelper imgGrab = new ImageGrabHelper(getActivity().getApplicationContext(), "http://192.168.2.183/img/slide.jpg");
                Bitmap bmp = imgGrab.grabImage();
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ImageView imgView = new ImageView(getActivity().getApplicationContext());
                imgView.setImageBitmap(bitmap);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                builder.setView(imgView);
                builder.create();

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }.execute();

        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}
