package com.pax.eagle.demo;

import android.content.Context;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LicenseReader {
    static byte[] getLicense(Context context) {
        try (InputStream inputStream = context.getApplicationContext().getAssets()
                .open("peter1210_Android_UTC20201210072202.license")) {
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }

    static byte[] getFromServer() {
        String content = LicenseManager.getInstance().getCurrentLicenseContent();
        return content.getBytes();
    }

    static private String getLicenseContent(String name) {
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
