package com.exmaple.funweather;

/**
 * Created by lenovo on 2018/5/18.
 */

public class HelpActivitySingleton {
    private static HelpActivity helpActivity;

    public static HelpActivity getSingletonHelpActivity() {
        if(helpActivity==null){
            helpActivity = new HelpActivity();
        }
        return helpActivity;
    }
}
