package com.example.coolweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.coolweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yang on 2016/7/19.
 */
public class Utility {

    public static Boolean copyDatabase(Context context,
                                       String dbName, String dbPath) {
        String dbFileName = dbPath + dbName;
        File file = new File(dbPath);
        //File dbFile = new File((dbFileName));
        if (!file.exists()) {
            file.mkdir();
            FileOutputStream os = null;
            InputStream is = null;
            try {
                os = new FileOutputStream(new File(dbFileName));
                is = context.getResources().openRawResource(R.raw.cool_weather);
                byte[] buffer = new byte[139264];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    os.write(buffer,0,count);
                    os.flush();
                    L.d("COPY");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        L.d("WELL");
        return true;

    }

    public static void handleWeatherResponseAndSafe(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject retDataObj = jsonObject.getJSONObject("retData");
            String city = retDataObj.getString("city");
            String cityid = retDataObj.getString("cityid");
            JSONObject todayObj = retDataObj.getJSONObject("today");
            String date = todayObj.getString("date");
            String curTemp = todayObj.getString("curTemp");
            String aqi = todayObj.getString("aqi");
            String fengxiang = todayObj.getString("fengxiang");
            String fengli = todayObj.getString("fengli");
            String hightemp = todayObj.getString("hightemp");
            String lowtemp = todayObj.getString("lowtemp");
            String type = todayObj.getString("type");
            JSONArray forecastList = retDataObj.getJSONArray("forecast");
            JSONObject tmrObj = forecastList.getJSONObject(0);
            String typeTmr = tmrObj.getString("type");
            String hightempTmr = tmrObj.getString("hightemp");
            String lowtempTmr = tmrObj.getString("lowtemp");
            JSONObject afterTmrObj = forecastList.getJSONObject(1);
            String typeAfterTmr = afterTmrObj.getString("type");
            String hightempAfterTmr = afterTmrObj.getString("hightemp");
            String lowtempAfterTmr = afterTmrObj.getString("lowtemp");

            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(context).edit();
            editor.putBoolean("city_selected", true);
            editor.putString("city",city);
            editor.putString("cityid",cityid);
            editor.putString("date",date);
            editor.putString("curTemp",curTemp);
            editor.putString("fengxiang",fengxiang);
            editor.putString("fengli",fengli);
            editor.putString("hightemp",hightemp);
            editor.putString("lowtemp",lowtemp);
            editor.putString("type",type);
            editor.putString("typeTmr",typeTmr);
            editor.putString("hightempTmr",hightempTmr);
            editor.putString("lowtempTmr",lowtempTmr);
            editor.putString("typeAfterTmr",typeAfterTmr);
            editor.putString("hightempAfterTmr",hightempAfterTmr);
            editor.putString("lowtempAfterTmr",lowtempAfterTmr);
            if (!"null".equals(aqi.trim())) {
                editor.putString("aqi",aqi);
                int n = Integer.parseInt(aqi.trim());
                if (n < 51) {
                    editor.putString("kongqi","优");
                } else if (n < 101) {
                    editor.putString("kongqi","良");
                } else  if (n < 151) {
                    editor.putString("kongqi","轻度污染");
                } else if (n < 201) {
                    editor.putString("kongqi","中度污染");
                }  else if (n < 301) {
                    editor.putString("kongqi","重度污染");
                } else {
                    editor.putString("kongqi","严重污染");
                }
            } else {
                editor.putString("aqi", "未知");
            }
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
