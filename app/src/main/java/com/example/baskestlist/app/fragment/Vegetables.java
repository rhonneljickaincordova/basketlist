package com.example.baskestlist.app.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.baskestlist.app.R;

/**
 * Created by Rhonnel on 12 /21/2014.
 */
public class Vegetables extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vegetables,container,false);
        return view;
    }
}
