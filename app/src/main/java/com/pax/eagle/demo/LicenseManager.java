package com.pax.eagle.demo;

import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.pax.eagledex.model.LicenseData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LicenseManager {
    //创建 SingleObject 的一个对象
    private static LicenseManager instance = new LicenseManager();
    private String mCurrent = "";
    private String mLicenseContent = "";

    //让构造函数为 private，这样该类就不会被实例化
    private LicenseManager() {
    }

    //获取唯一可用的对象
    public static LicenseManager getInstance() {
        return instance;
    }

    public String getLicenseProductId(){
        try {
            byte[] content = Base64.decode(mLicenseContent, Base64.DEFAULT);
            int contentLen = byteArrayToInt(content, 4);
            String licenseContent = new String(getByteArr(content, 8, 8+contentLen));
            LicenseData data = JSON.parseObject(licenseContent, LicenseData.class);
            return data.getProducts().get(0).getId();
        }catch (Exception e){
            return "";
        }
    }

    private static int byteArrayToInt(byte[] b, int index){
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }

    private static byte[] getByteArr(byte[]data,int start ,int end){
        byte[] ret=new byte[end-start];
        for(int i=0;(start+i)<end;i++){
            ret[i]=data[start+i];
        }
        return ret;
    }

    public String getCurrentLicense() {
        return mCurrent;
    }

    public String getCurrentLicenseContent(){
        return mLicenseContent;
    }

    public void setCurrentLicense(String name) {
        mCurrent = name;
        new Thread(() -> mLicenseContent = getLicenseContent(mCurrent)).start();
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

    private String getLicenseContent(String name) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://119.45.254.226:8888/file/" + name)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
