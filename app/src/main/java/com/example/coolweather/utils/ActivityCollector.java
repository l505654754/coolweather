package com.example.coolweather.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		if (!activities.contains(activity)) {
			activities.add(activity);
		}	
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finshAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
