package com.example.baskestlist.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.baskestlist.app.R;
import com.example.baskestlist.app.model.listMapmodel;
import com.example.baskestlist.app.model.productmodel;

import java.util.ArrayList;

/**
 * Created by Rhonnel on 1/8/2015.
 */
public class infowindowadapter extends BaseAdapter {
    Context context;
    ArrayList<listMapmodel> models;

    public infowindowadapter(Context context, ArrayList<listMapmodel> models) {
        this.context = context;
        this.models = models;
    }
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.data_map, null);

            holder.prodName = (TextView) convertView.findViewById(R.id.productNameMap);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.prodName.setText(models.get(position).productNameMap);

        return convertView;

    }
    static class ViewHolder{
        TextView prodName;
    }
}
