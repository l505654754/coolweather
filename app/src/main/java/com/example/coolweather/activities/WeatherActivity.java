package com.example.coolweather.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;
import com.example.coolweather.utils.HttpCallbackListener;
import com.example.coolweather.utils.HttpUtil;
import com.example.coolweather.utils.Utility;

/**
 * Created by yang on 2016/7/21.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private static String httpUrl = "http://apis.baidu.com/apistore/" +
            "weatherservice/recentweathers?cityid=";

    private RelativeLayout todayRl;
    private LinearLayout nextTwoDayLl;

    private TextView cityNameTv;
    private TextView curTempTv;
    private TextView dateTV;
    private TextView typeTv;
    private TextView tempLowTv;
    private TextView tempHighTv;
    private TextView fengxiangTv;
    private TextView fengliTv;
    private TextView kongqiTv;
    private TextView aqiTv;
    private TextView typeTmr;
    private TextView tempHighTmrTv;
    private TextView tempLowTmrTv;
    private TextView typeAfterTmrTv;
    private TextView tempHighAfterTmrTv;
    private TextView tempLowAfterTmrTv;
    private Button switchCityBtn;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        todayRl = (RelativeLayout) findViewById(R.id.rl_today);
        nextTwoDayLl = (LinearLayout) findViewById(R.id.ll_next_two_day);
        cityNameTv = (TextView) findViewById(R.id.city_name);
        curTempTv = (TextView) findViewById(R.id.current_temp);
        dateTV = (TextView) findViewById(R.id.current_date);
        typeTv = (TextView) findViewById(R.id.weather_desp);
        tempLowTv = (TextView) findViewById(R.id.temp_low);
        tempHighTv = (TextView) findViewById(R.id.temp_high);
        fengxiangTv = (TextView) findViewById(R.id.fengxiang);
        fengliTv = (TextView) findViewById(R.id.fengli);
        kongqiTv = (TextView) findViewById(R.id.kongqi);
        aqiTv = (TextView) findViewById(R.id.aqi);
        typeTmr = (TextView) findViewById(R.id.weather_desp_tmr);
        tempHighTmrTv = (TextView) findViewById(R.id.temp_high_tmr);
        tempLowTmrTv = (TextView) findViewById(R.id.temp_low_tmr);
        typeAfterTmrTv = (TextView) findViewById(R.id.weather_desp_after_tmr);
        tempHighAfterTmrTv = (TextView) findViewById(R.id.temp_high_after_tmr);
        tempLowAfterTmrTv = (TextView) findViewById(R.id.temp_low_after_tmr);

        switchCityBtn = (Button) findViewById(R.id.btn_switch_city);
        switchCityBtn.setOnClickListener(this);

        refreshWeather = (Button) findViewById(R.id.btn_refresh);
        refreshWeather.setOnClickListener(this);
        showWeather();
        if (!getIntent().getBooleanExtra("isBackFromSwitchCity", false)) {
            String weatherCode = getIntent().getStringExtra("weatherCode");
            String address = httpUrl + weatherCode;
            queryWeatherInfoFromServer(address);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_switch_city:
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("isFromWeather", true);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_refresh:
                String weatherCode = getIntent().getStringExtra("weatherCode");
                String address = httpUrl + weatherCode;
                queryWeatherInfoFromServer(address);
                break;
            default:
        }

    }

    private void queryWeatherInfoFromServer(final String address) {
        cityNameTv.setText("同步中···");
        //   todayRl.setVisibility(View.VISIBLE);
        // nextTwoDayLl.setVisibility(View.VISIBLE);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponseAndSafe(WeatherActivity.this,
                        response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                        Toast.makeText(WeatherActivity.this, "同步失败",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        cityNameTv.setText(prefs.getString("city", ""));
        curTempTv.setText(prefs.getString("curTemp", ""));
        dateTV.setText(prefs.getString("date", ""));
        typeTv.setText(prefs.getString("type", ""));
        tempLowTv.setText(prefs.getString("lowtemp", ""));
        tempHighTv.setText(prefs.getString("hightemp", ""));
        fengxiangTv.setText(prefs.getString("fengxiang", ""));
        fengliTv.setText(prefs.getString("fengli", ""));
        kongqiTv.setText(prefs.getString("kongqi", ""));
        aqiTv.setText(prefs.getString("aqi", "未知"));
        typeTmr.setText(prefs.getString("typeTmr", ""));
        tempHighTmrTv.setText(prefs.getString("hightempTmr", ""));
        tempLowTmrTv.setText(prefs.getString("lowtempTmr", ""));
        typeAfterTmrTv.setText(prefs.getString("typeAfterTmr", ""));
        tempHighAfterTmrTv.setText(prefs.getString("hightempAfterTmr", ""));
        tempLowAfterTmrTv.setText(prefs.getString("lowtempAfterTmr", ""));

        //  todayRl.setVisibility(View.GONE);
        //nextTwoDayLl.setVisibility(View.GONE);
    }

    public static void actionStart(Context context, String weatherCode, Boolean isBackFromSwitchCity) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra("weatherCode", weatherCode);
        intent.putExtra("isBackFromSwitchCity", isBackFromSwitchCity);
        context.startActivity(intent);
    }


}
