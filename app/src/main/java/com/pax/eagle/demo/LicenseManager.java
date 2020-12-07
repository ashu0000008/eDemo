package com.pax.eagle.demo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LicenseManager {
    //创建 SingleObject 的一个对象
    private static LicenseManager instance = new LicenseManager();
    private String mCurrent = "";

    //让构造函数为 private，这样该类就不会被实例化
    private LicenseManager() {
    }

    //获取唯一可用的对象
    public static LicenseManager getInstance() {
        return instance;
    }

    public String getCurrentLicense() {
        return mCurrent;
    }

    public void setCurrentLicense(String name) {
        mCurrent = name;
    }

    public List<String> getList() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://119.45.254.226:8888/list")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                List<String> result = JSON.parseArray(response.body().string(), String.class);
                return result;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
