package com.example.baskestlist.app.adapter;

import com.example.baskestlist.app.helper.global;

import java.util.List;

/**
 * Created by Rhonnel on 12/15/2014.
 */
public interface fetchdataListner {
        public void OnfetchComplete(List<global> data);
        public void OnfetchFailures(String msg);

}
