package com.callhippo.bueno.callhippo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.callhippo.bueno.callhippo.R;

public class GridViewAdpter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] friuits;
    private static String TAG="gridAdapter";

    public GridViewAdpter(Context context, String[] friuits) {
        this.context = context;
        this.friuits = friuits;
    }

    @Override
    public int getCount() {
        return friuits.length;
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
        if (inflater== null){
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.row_item,null);
        }
        Log.e(TAG,"tagNames "+friuits[position]);

//        for (int i=0;i<friuits.length;i++){
//            String temp = String.valueOf(friuits[position]);
//            String temp2 = String.valueOf(temp.replace("[","").replace("]","").split(","));
//            Log.e(TAG,"temp2 "+temp2);
//        }
        TextView textView = convertView.findViewById(R.id.text_tag);
        textView.setText(friuits[position]);
        return convertView;
    }
}
