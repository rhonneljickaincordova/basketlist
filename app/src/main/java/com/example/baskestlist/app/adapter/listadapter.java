package com.example.baskestlist.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.baskestlist.app.R;
import com.example.baskestlist.app.model.productmodel;

import java.util.ArrayList;

/**
 * Created by Rhonnel on 12/17/2014.
 */
public class listadapter extends BaseAdapter {
    Context context;
    ArrayList<productmodel> models;

    public listadapter(Context context, ArrayList<productmodel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.gallery_list, null);

            holder.pID = (TextView) convertView.findViewById(R.id.pID);
            holder.prodName = (TextView) convertView.findViewById(R.id.productName);
            holder.prodLoc = (TextView) convertView.findViewById(R.id.productLocation);
            holder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pID.setText(models.get(position).pID);
        holder.prodName.setText(models.get(position).productName);
        holder.prodLoc.setText(models.get(position).productLocation);
        holder.date.setText(models.get(position).date);

        return convertView;

    }
    static class ViewHolder{
        TextView pID;
        TextView prodName;
        TextView prodLoc;
        TextView date;
    }
}
