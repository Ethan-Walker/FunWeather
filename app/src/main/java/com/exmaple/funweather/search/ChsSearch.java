package com.exmaple.funweather.search;

import com.exmaple.funweather.db.County;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/6/16.
 */

public class ChsSearch {

    public static List<County> countyLocDataList = new ArrayList<>();


//    public static List<String> search()
    public static List<County> search(String searchStr){
        List<County> arrayList = new ArrayList<>();
        for(County county:countyLocDataList){
            String countyName = county.getCountyName();
            if(countyName.contains(searchStr)){
                arrayList.add(county);
            }
        }
        return arrayList;

    }
}
