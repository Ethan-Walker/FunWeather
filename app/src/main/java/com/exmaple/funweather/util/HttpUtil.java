package com.exmaple.funweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by EthanWalker on 2017/6/13.
 */

public class HttpUtil {
    public static void sendOkHttpReq(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).addHeader("Connection","close").build();
        client.newCall(request).enqueue(callback);           // 如果请求成功返回结果，将回调callback中重写的 onResponse 方法
    }
}
