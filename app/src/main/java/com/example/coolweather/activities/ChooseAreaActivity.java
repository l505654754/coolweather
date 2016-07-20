package com.example.coolweather.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/7/20.
 */
public class ChooseAreaActivity extends Activity {

    public static final int PROVINCE_LEVEL = 0;
    public static final int CITY_LEVEL = 1;
    public static final int COUNTY_LEVEL = 2;

    private ListView listView;
    private TextView textView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<String>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private CoolWeatherDB coolWeatherDB;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        textView = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.ls);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        /*try {
            coolWeatherDB.initDB();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == PROVINCE_LEVEL) {
                    selectedProvince = provinceList.get(i);
                    queryCity();
                } else if (currentLevel == CITY_LEVEL) {
                    selectedCity = cityList.get(i);
                    queryCounty();
                }
            }
        });
      queryProvince();
    }

    private void queryProvince() {
        provinceList = coolWeatherDB.loadProvince();
        dataList.clear();
        for (Province province : provinceList) {
            dataList.add(province.getProvince_name());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        textView.setText("中国");
        currentLevel = PROVINCE_LEVEL;
    }

    private void queryCity() {
        cityList = coolWeatherDB.loadCity(selectedProvince.getProvince_code());
        dataList.clear();
        for (City city : cityList) {
            dataList.add(city.getCity_name());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        textView.setText(selectedProvince.getProvince_name());
        currentLevel = CITY_LEVEL;

    }

    private void queryCounty() {
        countyList = coolWeatherDB.loadCounty(selectedCity.getCity_code());
        dataList.clear();
        for (County county : countyList) {
            dataList.add(county.getCounty_name());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        textView.setText(selectedCity.getCity_name());
        currentLevel = COUNTY_LEVEL;

    }

    @Override
    public void onBackPressed() {
        if (currentLevel == COUNTY_LEVEL) {
            queryCity();
        } else if (currentLevel ==CITY_LEVEL) {
            queryProvince();
        } else {
            finish();
        }
    }
}
