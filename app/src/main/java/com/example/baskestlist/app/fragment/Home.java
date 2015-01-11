package com.example.baskestlist.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.baskestlist.app.R;

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

        return mTabHost;
   }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}
