package com.example.coolweather.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.utils.Utility;

/**
 * Created by yang on 2016/7/22.
 */
public class FirstRunActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_run);

        if (getIntent().getBooleanExtra("isError", false)) {
            error();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Utility.copyDatabase(FirstRunActivity.this,
                            CoolWeatherDB.DB_NAME, CoolWeatherDB.DB_PATH)){
                        Intent intent = new Intent(FirstRunActivity.this, ChooseAreaActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                error();
                            }
                        });
                    }
                }
            },2000);

        }
    }
    private void error() {
        ((TextView) findViewById(R.id.tv_fist_run1)).setText("初始化失败");
        ((TextView) findViewById(R.id.tv_fist_run2)).setText("请删除后重新下载安装");
    }
    static public void actionStart(Context context, Boolean isError) {
        Intent intent = new Intent(context, FirstRunActivity.class);
        intent.putExtra("isError", isError);
        context.startActivity(intent);
    }
}
