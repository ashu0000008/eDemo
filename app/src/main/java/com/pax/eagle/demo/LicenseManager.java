package com.pax.eagle.demo;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pax.eagledex.model.LicenseData;
import com.pax.eagledex.util.JsonUtil;

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

    public String getLicenseProductId() {
        try {
            byte[] content = Base64.decode(mLicenseContent, Base64.DEFAULT);
            int contentLen = byteArrayToInt(content, 4);
            String licenseContent = new String(getByteArr(content, 8, 8 + contentLen));
            LicenseData data = JsonUtil.Companion.jsonToObject(licenseContent, LicenseData.class);
            return data.getProducts().get(0).getId();
        } catch (Exception e) {
            return "";
        }
    }

    private static int byteArrayToInt(byte[] b, int index) {
        return b[index + 3] & 0xFF |
                (b[index + 2] & 0xFF) << 8 |
                (b[index + 1] & 0xFF) << 16 |
                (b[index + 0] & 0xFF) << 24;
    }

    private static byte[] getByteArr(byte[] data, int start, int end) {
        if (start >= data.length || end >= data.length) {
            return new byte[0];
        }

        byte[] ret = new byte[end - start];
        for (int i = 0; (start + i) < end; i++) {
            ret[i] = data[start + i];
        }
        return ret;
    }

    public String getCurrentLicense() {
        return mCurrent;
    }

    public String getCurrentLicenseContent() {
        return mLicenseContent;
    }

    public void setCurrentLicense(String name) {
        mCurrent = name;
        new Thread(() -> mLicenseContent = getLicenseContent(mCurrent)).start();
    }

    public List<String> getList() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://47.98.54.147:8888/list")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //Json的解析类对象
                JsonParser parser = new JsonParser();
                //将JSON的String 转成一个JsonArray对象
                JsonArray jsonArray = parser.parse(response.body().string()).getAsJsonArray();

                Gson gson = new Gson();
                ArrayList<String> userBeanList = new ArrayList<>();

                //加强for循环遍历JsonArray
                for (JsonElement user : jsonArray) {
                    //使用GSON，直接转成Bean对象
                    String userBean = gson.fromJson(user, String.class);
                    userBeanList.add(userBean);
                }
                return userBeanList;
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
                .url("http://47.98.54.147:8888/file/" + name)
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
