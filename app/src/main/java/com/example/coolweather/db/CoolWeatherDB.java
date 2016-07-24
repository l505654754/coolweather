package com.example.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/7/19.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    public static final String DB_PATH = "/data/data/com.example.coolweather/databases/";
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

    public static Boolean checkDB() {
        SQLiteDatabase db = null;
        String dbFileName = DB_PATH + DB_NAME;
        try {
            db = SQLiteDatabase.openDatabase(dbFileName,null,SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {

        } finally {
            if (db != null) {
                db.close();
            }
            return db == null?false:true;
        }


    }


    public void saveProvince(Province province) {

        if (province != null) {
            String name = province.getProvince_name();
            String code = province.getProvince_code();
            ContentValues values = new ContentValues();
            values.put("province_name", name);
            values.put("province_code", code);
            db.insert("Province",null,values);
        }
    }

    public List<Province> loadProvince() throws Exception{
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
       List<Province> list = new ArrayList<Province>();
        if(cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvince_name(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvince_code(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city) {
        if (city != null) {
            String name = city.getCity_name();
            String code = city.getCity_code();
            String provinceCode = city.getProvince_code();
            ContentValues values = new ContentValues();
            values.put("city_name", name);
            values.put("city_code", code);
            values.put("province_code", provinceCode);
            db.insert("City", null, values);
        }
    }

    public List<City> loadCity(String provinceCode) throws Exception{
        Cursor cursor = db.query("City", null, "province_code = ?",
                new String[]{provinceCode}, null, null, null);
        List<City> list = new ArrayList<City>();
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvince_code(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;

    }

    public void saveCounty(County county) {
        if(county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCounty_name());
            values.put("county_code", county.getCounty_code());
            values.put("weather_code", county.getWeather_code());
            values.put("city_code", county.getCity_code());
            db.insert("County", null, values);
        }
    }

    public List<County> loadCounty(String cityCode) throws Exception{
        Cursor cursor = db.query("County", null, "city_code = ?", new String[] {cityCode}, null, null, null);
        List<County> list = new ArrayList<County>();
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCounty_name(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCounty_code(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setWeather_code(cursor.getString(cursor.getColumnIndex("weather_code")));
                county.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;

    }

    public void initDB() throws Exception {

        db.delete("Province", null, null);
        db.delete("City", null, null);
        db.delete("County", null, null);

        File file = new File("/data/data/com.example.coolweather/id.xml");
        FileInputStream in = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
        String line = "";
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line.toString());

        }
        String cont = content.toString();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = factory.newPullParser();
        xmlPullParser.setInput(new StringReader(cont));
        int eventType = xmlPullParser.getEventType();
        String provinceCode = "";
        String cityCode = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                switch (xmlPullParser.getName()) {
                    case "province":
                       provinceCode = xmlPullParser.getAttributeValue(null,"id");
                       Province province = new Province();
                        province.setProvince_name(xmlPullParser.getAttributeValue(null, "name"));
                        province.setProvince_code(provinceCode);
                        saveProvince(province);
                        break;
                    case "city":
                        cityCode = xmlPullParser.getAttributeValue(null, "id");
                        City city = new City();
                        city.setCity_name(xmlPullParser.getAttributeValue(null, "name"));
                        city.setCity_code(cityCode);
                        city.setProvince_code(provinceCode);
                        saveCity(city);
                        break;
                    case "county":
                        County county = new County();
                        county.setCounty_name(xmlPullParser.getAttributeValue(null, "name"));
                        county.setCounty_code(xmlPullParser.getAttributeValue(null, "id"));
                        county.setWeather_code(xmlPullParser.getAttributeValue(null,
                                "weatherCode"));
                        county.setCity_code(cityCode);
                        saveCounty(county);

                        break;
                    default:

                }

            }
            eventType = xmlPullParser.next();
        }
    }
    }




