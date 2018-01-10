package com.example.zw.weatherapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.util.LinkedList;

public class City_history extends AppCompatActivity implements city_selected_Adapter.Callback{
    private Toolbar toolbar;
    private ShSwitchView switchView;
    private LinkedList<String>citys;
    private city_selected_Adapter madapter;
    private ListView listview_city_history;
    private AddBtn addBtn;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private TextView textchange;
    //用于定位
    private String currentCity=null;
    private LocationService locationService;

    /*private AdapterView.OnItemSelectedListener  onItemSelectedListener =
            new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    //当此选中的item的子控件需要获得焦点时
                    parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    //else parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    Toast.makeText(City_history.this,"selected",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    Toast.makeText(City_history.this,"selected",Toast.LENGTH_SHORT).show();
                }
            };*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("城市管理");
        setSupportActionBar(toolbar);
        listview_city_history=(ListView)findViewById(R.id.listview_city_history);
        textchange=(TextView)findViewById(R.id.text_change);


        switchView=(ShSwitchView)findViewById(R.id.switch_view);
        switchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {

                if(isOn){
                    locationService.start();
                    if(currentCity!=null){
                        textchange.setText(currentCity);
                    }
                    locationService.stop();

                }else {
                    textchange.setText("自动定位");
                }
            }
        });


      /*  switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchView.isOn()){
                    Toast.makeText(City_history.this,"开",Toast.LENGTH_SHORT).show();;
                    textchange.setText("太原");
                }else {
                    Toast.makeText(City_history.this,"关",Toast.LENGTH_SHORT).show();;
                    textchange.setText("自动定位");
                }
            }
        });*/

        //  Boolean a= switchView.hasFocus();
        citys=new LinkedList<String>();

        dbHelper=new MyDatabaseHelper(City_history.this,"City.db",null,1);
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("city_selected",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String cityName=cursor.getString(cursor.getColumnIndex("cityName"));
                citys.add(cityName);
            }
            while (cursor.moveToNext());

        }
        cursor.close();





    /*    citys.add("南京");
        citys.add("南京");
        citys.add("南京");
        citys.add("南京");
        citys.add("南京");
        citys.add("南京");*/
        madapter=new city_selected_Adapter((LinkedList<String>)citys,this,this);
        listview_city_history.setAdapter(madapter);
//listview_city_history.setOnItemSelectedListener(onItemSelectedListener);
        listview_city_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(City_history.this,citys.get(position),Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.putExtra("city_selected",citys.get(position));
                intent.putExtra("selected_id",String.valueOf(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        addBtn=(AddBtn)findViewById(R.id.btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(City_history.this,Citylist_activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {

        locationService = ((MyApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        super.onStart();

    }
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop();
        super.onStop();
    }

    @Override
    public void click(View v) {
        Toast.makeText(City_history.this,citys.get((Integer)v.getTag()),Toast.LENGTH_SHORT).show();
        String cityName=citys.get((Integer)v.getTag());
        db.delete("city_selected","cityName=?",new String[]{cityName});
        // citys.remove((Integer)v.getTag());
        citys.remove(cityName);
        madapter.notifyDataSetChanged();
    }


    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                currentCity=location.getCity();
            }

        }

    };
}
