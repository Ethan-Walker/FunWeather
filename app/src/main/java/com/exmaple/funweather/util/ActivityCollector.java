package com.exmaple.funweather.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/6/14.
 */

public class ActivityCollector {

    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
        activity.finish();
    }

    public static void removeAll(){
        if(activityList!=null){
            for(int i=0;i<activityList.size();i++){
                activityList.get(i).finish();
                activityList.remove(activityList.get(i));
            }
        }


    }

}
