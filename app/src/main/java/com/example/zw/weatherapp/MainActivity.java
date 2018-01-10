package com.example.zw.weatherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,ViewPager.OnPageChangeListener{

    private LocationService locationService;
    private TextView city_name;
    private ImageView btn_select_city,city_location,city_share,city_list;
    private Intent intent;
    private MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    private String city=null;
    private ViewPager viewPager;
    private FragmentPagerAdapter mAdapter;
    private LinkedList<String> citys;
    private ArrayList<WeatherFragment> fragmentlist;
    public static final int UPDATE_TEXT = 1;
  private  int count=0;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    // 在这里可以进行UI操作
                    city_name.setText(city);

                    Cursor cursor=db.query("city_selected",new String[]{"cityName"},"cityname=?",new String[]{city},null,null,null);
                    if (!cursor.moveToFirst()) {

                        ContentValues values = new ContentValues();
                        values.put("cityName", city);
                        db.insert("city_selected", null, values);

                        Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_LONG).show();

                            citys.add(city);


                        Parameters para=new Parameters();
                        para.put("city",city);
                        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",ApiStoreSDK.GET,
                                para,new ApiCallBack(){
                                    @Override
                                    public void onSuccess(int i, String s) {
                                        super.onSuccess(i, s);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("args",s);
                                        bundle.putString("city",city);
                                        WeatherFragment f=new WeatherFragment();
                                        count++;
                                        f.setArguments(bundle);
                                        fragmentlist.add(f);

                                        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager(),fragmentlist);
                                        viewPager.setAdapter(mAdapter);
                                        viewPager.setCurrentItem(0);
                                        viewPager.addOnPageChangeListener(MainActivity.this);

                                    }

                                });
                    }
                  cursor.close();

                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_main);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(10);
        city_name=(TextView)findViewById(R.id.city_name);
        btn_select_city=(ImageView)findViewById(R.id.btn_select_city);
        city_location=(ImageView)findViewById(R.id.city_location);
        city_share=(ImageView)findViewById(R.id.city_share);
        city_list=(ImageView)findViewById(R.id.city_list);
        btn_select_city.setOnClickListener(this);
        city_location.setOnClickListener(this);
        city_share.setOnClickListener(this);
        city_list.setOnClickListener(this);
        btn_select_city.setOnTouchListener(this);
        city_location.setOnTouchListener(this);
        city_share.setOnTouchListener(this);
        city_list.setOnTouchListener(this);

        fragmentlist=new ArrayList<WeatherFragment>();
        dbHelper=new MyDatabaseHelper(MainActivity.this,"City.db",null,1);
       db= dbHelper.getWritableDatabase();
        citys=new LinkedList<String>();
       //取得数据库中城市列表
        dbHelper=new MyDatabaseHelper(MainActivity.this,"City.db",null,1);
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("city_selected",null,null,null,null,null,null);

       count=0;
        if(cursor.moveToFirst()){
            do {
                count++;
            }
            while (cursor.moveToNext());
        }
        if(cursor.moveToFirst()){
            do {

                final String cityName=cursor.getString(cursor.getColumnIndex("cityName"));
                citys.add(cityName);
                Parameters para=new Parameters();
                para.put("city",cityName);
                ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",ApiStoreSDK.GET,
                        para,new ApiCallBack(){
                            @Override
                            public void onSuccess(int i, String s) {
                                super.onSuccess(i, s);
                                 Bundle bundle=new Bundle();
                                bundle.putString("args",s);
                                bundle.putString("city",cityName);
                                WeatherFragment f=new WeatherFragment();
                                f.setArguments(bundle);
                                fragmentlist.add(f);
                               if(fragmentlist.size()==count){
                                   Toast.makeText(MainActivity.this,"count="+count,Toast.LENGTH_SHORT).show();
                                   mAdapter=new FragmentPagerAdapter(getSupportFragmentManager(),fragmentlist);
                                   viewPager.setAdapter(mAdapter);
                                   viewPager.setCurrentItem(0);
                                   viewPager.addOnPageChangeListener(MainActivity.this);
                               }

                            }

                        });

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        locationService = ((MyApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

        new Thread(new Runnable() {
            @Override
            public void run() {
                locationService.start();
                while (city==null){

                };
                locationService.stop();
                Message message=new Message();
                message.what=UPDATE_TEXT;
                handler.sendMessage(message);
            }
        }).start();
    }


/*
    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
      //  int type = getIntent().getIntExtra("from", 0);
       // if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
       // } else if (type == 1) {
         //   locationService.setLocationOption(locationService.getOption());
       // }
    }
*/
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop();
        super.onStop();
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                city=location.getCity();
              if (city.contains("市")){
                 city=city.replace("市","");
              }
            }

        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_city:
               // locationService.start();
                intent=new Intent(MainActivity.this,Citylist_activity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.city_location:


                break;
            case R.id.city_share:

                break;
            case R.id.city_list:
                intent=new Intent(MainActivity.this,City_history.class);
                startActivityForResult(intent,2);
                //startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case 1:
               if(resultCode==RESULT_OK){
                   final String city_selected=data.getStringExtra("city_selected");
                   city_name.setText(city_selected);
                   Cursor cursor=db.query("city_selected",new String[]{"cityName","id"},"cityname=?",new String[]{city_selected},null,null,null);
                   if (!cursor.moveToFirst()) {
                       ContentValues values=new ContentValues();
                       values.put("cityName",city_selected);
                       db.insert("city_selected",null,values);
                       Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_LONG).show();
                       citys.add(city_selected);
                       Parameters para=new Parameters();
                       para.put("city",city_selected);
                       ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",ApiStoreSDK.GET,
                               para,new ApiCallBack(){
                                   @Override
                                   public void onSuccess(int i, String s) {
                                       super.onSuccess(i, s);
                                       Bundle bundle=new Bundle();
                                       bundle.putString("args",s);
                                       bundle.putString("city",city_selected);
                                       WeatherFragment f=new WeatherFragment();
                                       f.setArguments(bundle);
                                       fragmentlist.add(f);
                                       mAdapter=new FragmentPagerAdapter(getSupportFragmentManager(),fragmentlist);
                                       viewPager.setAdapter(mAdapter);
                                       viewPager.setCurrentItem(count);
                                       Toast.makeText(MainActivity.this,"count="+count,Toast.LENGTH_SHORT).show();
                                       count++;
                                       viewPager.addOnPageChangeListener(MainActivity.this);

                                   }

                               });
                   }else{

                       int i=0;
                      for(WeatherFragment f:fragmentlist){
                         if(f.getArguments().getString("city").equals(city_selected)){
                             Toast.makeText(MainActivity.this,"i="+String.valueOf(i),Toast.LENGTH_SHORT).show();
                             viewPager.setCurrentItem(i);

                              break;
                          }
                          i++;
                       }
                      /* cursor.moveToFirst();
                       String id=cursor.getString(cursor.getColumnIndex("id"));
                       Toast.makeText(MainActivity.this,"id="+String.valueOf(id),Toast.LENGTH_SHORT).show();
                       viewPager.setCurrentItem(Integer.parseInt(id)-1);*/
                   }
                 cursor.close();
               }

               break;
           case 2:
               if(resultCode==RESULT_OK){
                   String city_selected=data.getStringExtra("city_selected");
                   // Toast.makeText(MainActivity.this,city_selected,Toast.LENGTH_LONG).show();
                   city_name.setText(city_selected);
                   int i=0;
                   for(WeatherFragment f:fragmentlist){
                       if(f.getArguments().getString("city").equals(city_selected)){
                           Toast.makeText(MainActivity.this,"i="+String.valueOf(i),Toast.LENGTH_SHORT).show();
                           viewPager.setCurrentItem(i);

                           break;
                       }
                       i++;
                   }
                 /* int id=Integer.parseInt(data.getStringExtra("selected_id"));

                   viewPager.setCurrentItem(id);*/
               }
               break;
       }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.city_location:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    locationService.start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                  Toast.makeText(MainActivity.this,city,Toast.LENGTH_LONG).show();
                    city_name.setText(city);
                  locationService.stop();
                }
                break;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
       if(state==2){
    int i=viewPager.getCurrentItem();
      String current=fragmentlist.get(i).getArguments().getString("city");
           city_name.setText(current);
}
    }
}
