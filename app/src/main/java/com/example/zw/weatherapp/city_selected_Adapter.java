package com.example.zw.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by zw on 2016/9/23.
 */
public class city_selected_Adapter extends BaseAdapter implements View.OnClickListener{
    private LinkedList<String>cityNames;
    private Context mContext;
    private Callback mCallback;




    public interface Callback{
        public void click(View v);
    }

    public city_selected_Adapter(LinkedList<String> cityNames, Callback mCallback, Context mContext) {
        this.cityNames = cityNames;
        this.mCallback = mCallback;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return cityNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_city_selected,null);
            holder=new ViewHolder();
            holder.city_name=(TextView)convertView.findViewById(R.id.item_cityName);
            holder.deleteBtn=(ImageButton)convertView.findViewById(R.id.deleteBtn);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.city_name.setText(cityNames.get(position));
        holder.deleteBtn.setOnClickListener(this);
        holder.deleteBtn.setTag(position);
        return convertView;
    }



    static class ViewHolder{
        TextView city_name;
        ImageButton deleteBtn;
    }
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }
}
