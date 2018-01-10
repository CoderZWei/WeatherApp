package com.example.zw.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zw on 2016/9/25.
 */
public class WeatherFragment extends android.support.v4.app.Fragment{
    private JSONTokener json;
    private ImageView imageviewNow,imageview1,imageview2,imageview3;
    private TextView now,temperature,Wind,temperature1,temperature2,temperature3,City;
    private String t,city;
    public WeatherFragment() {
         //t=getArguments().getString("args");

        icons.put("晴",R.mipmap.t1);
        icons.put("多云",R.mipmap.t2);
        icons.put("晴间多云",R.mipmap.t2);
        icons.put("阴",R.mipmap.t3);
        icons.put("阵雨",R.mipmap.t4);
        icons.put("雷雨",R.mipmap.t5);
        icons.put("雷阵雨伴有冰雹",R.mipmap.t6);
        icons.put("雨夹雪",R.mipmap.t7);
        icons.put("小雨",R.mipmap.t8);
        icons.put("中雨",R.mipmap.t9);
        icons.put("大雨",R.mipmap.t10);
        icons.put("暴雨",R.mipmap.t11);
        icons.put("大暴雨",R.mipmap.t12);
        icons.put("特大暴雨",R.mipmap.t13);
        icons.put("阵雪",R.mipmap.t14);
        icons.put("小雪",R.mipmap.t15);
        icons.put("中雪",R.mipmap.t16);
        icons.put("大雪",R.mipmap.t17);
        icons.put("暴雪",R.mipmap.t18);
        icons.put("雾",R.mipmap.t19);
        icons.put("霾",R.mipmap.t19);
        icons.put("冻雨",R.mipmap.t20);
        icons.put("沙尘暴",R.mipmap.t21);
        icons.put("毛毛雨/细雨",R.mipmap.t22);
        icons.put("小雨-大雨",R.mipmap.t22);
        icons.put("中雨-大雨",R.mipmap.t23);
        icons.put("大雨-暴雨",R.mipmap.t24);
        icons.put("暴雨-大暴雨",R.mipmap.t25);
        icons.put("大暴雨-特大暴雨",R.mipmap.t26);
        icons.put("小雪-中雪",R.mipmap.t27);
        icons.put("中雪-大雪",R.mipmap.t28);
        icons.put("大雪-暴雪",R.mipmap.t29);
        icons.put("浮尘",R.mipmap.t30);
        icons.put("扬沙",R.mipmap.t31);
        icons.put("强沙尘暴",R.mipmap.t32);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // t=savedInstanceState.getString("args");
        t=getArguments().getString("args");
        city=getArguments().getString("city");
       json = new JSONTokener(t);
      // Toast.makeText(getActivity(),"传进来的"+t, Toast.LENGTH_SHORT).show();


    }

    /*private int[]  icons={R.mipmap.t1,R.mipmap.t2,R.mipmap.t3,R.mipmap.t4,R.mipmap.t5,
            R.mipmap.t6,R.mipmap.t7,R.mipmap.t8,R.mipmap.t9,R.mipmap.t10,R.mipmap.t11,
            R.mipmap.t12,R.mipmap.t13,R.mipmap.t14,R.mipmap.t15,R.mipmap.t16,R.mipmap.t17,
            R.mipmap.t18,R.mipmap.t19,R.mipmap.t20,R.mipmap.t21,R.mipmap.t22,R.mipmap.t23,
            R.mipmap.t24,R.mipmap.t25,R.mipmap.t26,R.mipmap.t27,R.mipmap.t28,R.mipmap.t29,
            R.mipmap.t30,R.mipmap.t31,R.mipmap.t32};*/
  /*  public WeatherFragment(JSONTokener json) {
        this.json = json;
    }*/
private Map<String,Integer> icons=new HashMap<String, Integer>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.weatherlayout,container,false);
        imageviewNow=(ImageView)view.findViewById(R.id.imageviewNow);
        imageview1=(ImageView)view.findViewById(R.id.imageview1);
        imageview2=(ImageView)view.findViewById(R.id.imageview2);
        imageview3=(ImageView)view.findViewById(R.id.imageview3);
        now=(TextView)view.findViewById(R.id.now);
        Wind=(TextView)view.findViewById(R.id.wind);
        temperature=(TextView)view.findViewById(R.id.temperature);
        temperature1=(TextView)view.findViewById(R.id.temperature1);
        temperature2=(TextView)view.findViewById(R.id.temperature2);
        temperature3=(TextView)view.findViewById(R.id.temperature3);
       // City=(TextView)view.findViewById(R.id.city);
      //  City.setText(city);
       // view.setTag(city);
     if(t!=null) {
          try {
              JSONObject one = (JSONObject) json.nextValue();
              JSONArray roots = null;
              roots = one.getJSONArray("HeWeather data service 3.0");
              JSONObject root = (JSONObject) roots.get(0);
              JSONArray hourly_forecasts = root.getJSONArray("hourly_forecast");
              JSONObject hourly_forecast = (JSONObject) hourly_forecasts.get(0);
              String tmp = hourly_forecast.getString("tmp");//当前温度
              JSONObject wind = (JSONObject) hourly_forecast.getJSONObject("wind");
              String dir = wind.getString("dir");//风向
              String sc = wind.getString("sc");//风力
              JSONArray daily_forecasts = root.getJSONArray("daily_forecast");
              //今天
              JSONObject day1 = (JSONObject) daily_forecasts.get(0);
              JSONObject tmpRange1 = (JSONObject) day1.getJSONObject("tmp");
              String min1 = tmpRange1.getString("min");
              String max1 = tmpRange1.getString("max");
              JSONObject cond1 = (JSONObject) day1.getJSONObject("cond");
              String weather1 = cond1.getString("txt_d");
              //明天
              JSONObject day2 = (JSONObject) daily_forecasts.get(1);
              JSONObject tmpRange2 = (JSONObject) day2.getJSONObject("tmp");
              String min2 = tmpRange2.getString("min");
              String max2 = tmpRange2.getString("max");
              JSONObject cond2 = (JSONObject) day2.getJSONObject("cond");
              String weather2 = cond2.getString("txt_d");
              //后天
              JSONObject day3 = (JSONObject) daily_forecasts.get(2);
              JSONObject tmpRange3 = (JSONObject) day3.getJSONObject("tmp");
              String min3 = tmpRange3.getString("min");
              String max3 = tmpRange3.getString("max");
              JSONObject cond3 = (JSONObject) day3.getJSONObject("cond");
              String weather3 = cond3.getString("txt_d");
              //大后天
              JSONObject day4 = (JSONObject) daily_forecasts.get(3);
              JSONObject tmpRange4 = (JSONObject) day4.getJSONObject("tmp");
              String min4 = tmpRange4.getString("min");
              String max4 = tmpRange4.getString("max");
              JSONObject cond4 = (JSONObject) day4.getJSONObject("cond");
              String weather4 = cond4.getString("txt_d");


              now.setText(tmp+"°");
              Wind.setText(dir + sc);
              temperature.setText(weather1 + min1 + "°到" + max1 + "°");
              temperature1.setText(max2 + "°/" + min2 + "°");
              temperature2.setText(max3 + "°/" + min3 + "°");
              temperature3.setText(max4 + "°/" + min4 + "°");
              imageviewNow.setImageResource(R.mipmap.t1);
              imageviewNow.setImageResource(icons.get(weather1));
              imageview1.setImageResource(icons.get(weather2));
              imageview2.setImageResource(icons.get(weather3));
              imageview3.setImageResource(icons.get(weather4));

          } catch (JSONException e) {
              e.printStackTrace();
          }

      }

        return view;

    }
}
