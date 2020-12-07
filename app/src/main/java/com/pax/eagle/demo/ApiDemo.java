package com.pax.eagle.demo;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.pax.eagle.api.IAuth;
import com.pax.eagle.api.OnAuthListener;
import com.pax.eagle.auth.EagleApi;


public class ApiDemo {
    static private final String PRODUCT_NAME = "e3c5f41fb6084c26ab23138c320a9f99";

    static void authorize(Activity activity) {
        IAuth iAuth = EagleApi.getInstance().getAuth(activity);
        iAuth.authorize(PRODUCT_NAME, new OnAuthListener() {
            @Override
            public void onSuccess(String productId, String deviceId) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "auth Success!----productId:"
                                + productId + "--deviceId:" + deviceId, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String productId, int errorCode, String message) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "auth error!----productId:"
                                + productId + "--errorCode" + errorCode + "--msg:" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public byte[] getLicenseFileContent() {
                return LicenseReader.getFromServer();
            }
        });
    }

    static void checkAuth(Activity activity) {
        /* It is recommended to call the isAuthorized method in a non-main thread  */
        new Thread(new Runnable() {
            @Override
            public void run() {
                IAuth eagleApi = EagleApi.getInstance().getAuth(activity);
                boolean result = eagleApi.isAuthorized(PRODUCT_NAME);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "authed:" + result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    static void showDeviceId(Context context) {
        IAuth eagleApi = EagleApi.getInstance().getAuth(context);
        String deviceId = eagleApi.getDeviceId();
        Toast.makeText(context, "deviceId:" + deviceId, Toast.LENGTH_LONG).show();
    }
}
