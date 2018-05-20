package com.exmaple.funweather;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/6/16.
 */

public class MyArrayAdapter extends ArrayAdapter<ProvinceCityCounty> {


    public static int IS_PROVINCE = 0x111;    /*直辖市 ：北京、重庆等*/
    public static int IS_CITY = 0x112;  /* 市区*/
    public static int IS_COUNTY = 0x113;   /*县、区*/


    private Context mContext;
    private int resourceId;
    private List<ProvinceCityCounty> list;
    private View convertView;

    public MyArrayAdapter(@NonNull Context context, @LayoutRes int resource, List<ProvinceCityCounty> list) {
        super(context, resource, list);
        mContext = context;
        resourceId = resource;
        this.list = list;
    }

    class ViewHolder {
        public TextView provinceTextView;
        public TextView countyTextView;
        public TextView cityTextView;

        public ViewHolder() {

        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ProvinceCityCounty item = list.get(position);
        View v = null;

        ViewHolder viewHolder;
        if (convertView == null) {
            v = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.provinceTextView = (TextView) v.findViewById(R.id.province_name);
            viewHolder.countyTextView = (TextView) v.findViewById(R.id.county_name);
            viewHolder.cityTextView = (TextView) v.findViewById(R.id.city_name);
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
        }
        String provinceName = item.getProvince();
        String cityName = item.getCity();
        String countyName = item.getCounty();

        /*如果不在搜索状态*/
        if (!ChooseAreaFragment.is_search) {
            viewHolder.provinceTextView.setText(item.getProvince());
            viewHolder.cityTextView.setText(item.getCity());
            viewHolder.countyTextView.setText(item.getCounty());
        } else {
            /**
             * 仅显示省份
             */
            if (item.getType() == IS_PROVINCE) {
                viewHolder.countyTextView.setText(provinceName);
                viewHolder.cityTextView.setText("");
                viewHolder.provinceTextView.setText("");
            }
            /**
             * 显示城市和 省份
             */
            else if (item.getType() == IS_CITY) {
                viewHolder.countyTextView.setText(cityName);
                viewHolder.provinceTextView.setText(provinceName);
                viewHolder.cityTextView.setText("");
            }
            /**
             * 显示 地区 城市  省份
             */
            else if (item.getType() == IS_COUNTY) {
                viewHolder.provinceTextView.setText(item.getProvince());
                viewHolder.cityTextView.setText(item.getCity());
                viewHolder.countyTextView.setText(item.getCounty());
            } else {
                Log.e(TAG, "显示出现错误");
            }

        }

        return v;
    }

    private static final String TAG = "MyArrayAdapter";

}
