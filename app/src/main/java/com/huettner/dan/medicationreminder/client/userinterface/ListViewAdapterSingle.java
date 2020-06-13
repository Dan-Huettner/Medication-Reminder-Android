package com.huettner.dan.medicationreminder.client.userinterface;



import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huettner.dan.medicationreminder.R;

public class ListViewAdapterSingle extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    public static final String FIRST_COLUMN="First";

    public ListViewAdapterSingle(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        TextView holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.colmn_row_single, null);
            holder = (TextView) convertView.findViewById(R.id.TextFirstSingle);

            convertView.setTag(holder);
        }else{

            holder=(TextView) convertView.getTag();
        }

        HashMap<String, String> map=list.get(position);
        holder.setText(map.get(FIRST_COLUMN));

        return convertView;
    }

}