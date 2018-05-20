package com.exmaple.funweather.search;

import java.util.ArrayList;

/**
 * Created by EthanWalker on 2017/6/15.
 */

public class LocalGroupSearch {

    static CharacterParser characterParser;

    public static ArrayList<String> searchGroup(ArrayList<String> sourceCityList,String searchStr){

        ArrayList<String> dataList = new ArrayList<>();

        characterParser = CharacterParser.getInstance();
        String result = "";
        characterParser.setResource(searchStr);
        result = characterParser.getSpelling();

        for(String cityName:sourceCityList){
            if(contains(cityName,result)){
                dataList.add(cityName);
            }
        }
        return dataList;
    }
    public static boolean contains(String cityName,String search){

        // 获取首字母
        if(search.length()<4){
            String cityFirstPys = getFirstPy(cityName);
            if(cityFirstPys.contains(search)){
                return true;
            }
        }

        String cityAllPys  = characterParser.getSpelling(cityName);

        return false;
    }

    /**
     * 获取首拼音
     * @param cityName
     * @return
     */
    public static String getFirstPy(String cityName){
        StringBuilder firstPys = new StringBuilder();

        for(int i=0;i<cityName.length();i++){
            String p = characterParser.convert(cityName.substring(i,i+1)).substring(0,1);
            firstPys.append(p);
        }
        return firstPys.toString();
    }
}