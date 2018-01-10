package com.example.zw.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zw.weatherapp.view.CharacterParser;
import com.example.zw.weatherapp.view.CitySortModel;
import com.example.zw.weatherapp.view.PinyinComparator;
import com.example.zw.weatherapp.view.SideBar;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Citylist_activity extends AppCompatActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<CitySortModel> SourceDateList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//要放在setSupportActionBar(toolbar);之前才能生效
        toolbar.setTitle("选择城市");
        setSupportActionBar(toolbar);
        initViews();

    }

    private void initViews() {
        characterParser = CharacterParser.getInstance();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("city_selected", ((CitySortModel) adapter.getItem(position )).getName());
                setResult(RESULT_OK,intent);
                finish();

               /* Toast.makeText(getApplication(),
                        ((CitySortModel) adapter.getItem(position )).getName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
      //  sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
    }


    private View initHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.item_select_city, null);
        TextView tv_catagory = (TextView) headView.findViewById(R.id.tv_catagory);
        TextView tv_city_name = (TextView) headView.findViewById(R.id.tv_city_name);
        tv_catagory.setText("自动定位");
        tv_city_name.setText("北京");
        tv_city_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_city_location), null, null, null);
        tv_city_name.setCompoundDrawablePadding(24);
        return headView;
    }

    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;

    }
}
