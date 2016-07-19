package com.example.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/7/19.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province) {

        if (province != null) {
            int id = province.getId();
            String name = province.getName();
            String code = province.getCode();
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("province_name", name);
            values.put("province_code", code);
            db.insert("Province",null,values);
        }
    }

    public List<Province> loadProvince() {
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
       List<Province> list = new ArrayList<Province>();
        if(cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city) {
        if (city != null) {
            int id = city.getId();
            String name = city.getName();
            String code = city.getCode();
            int provinceId = city.getProvinceId();
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("city_name", name);
            values.put("city_code", code);
            values.put("province_id", provinceId);
            db.insert("City", null, values);
        }
    }

    public List<City> loadCity() {
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        List<City> list = new ArrayList<City>();
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;

    }

    public void saveCounty(County county) {
        if(county != null) {
            ContentValues values = new ContentValues();
            values.put("id", county.getId());
            values.put("county_name", county.getName());
            values.put("county_code", county.getCode());
            values.put("city_id", county.getCityId());
            db.insert("City", null, values);
        }
    }

    public List<County> loadCounty() {
        Cursor cursor = db.query("County", null, null, null, null, null, null);
        List<County> list = new ArrayList<County>();
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;

    }



}
