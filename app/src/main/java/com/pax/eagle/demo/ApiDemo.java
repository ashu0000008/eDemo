package com.pax.eagle.demo;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.pax.eagle.api.IAuth;
import com.pax.eagle.api.OnAuthListener;
import com.pax.eagle.auth.EagleApi;


public class ApiDemo {
    static void authorize(Activity activity) {
        String productId = LicenseManager.getInstance().getLicenseProductId();
        IAuth iAuth = EagleApi.getInstance().createAuth(activity, new SNFetcher(activity.getApplicationContext()));
        iAuth.authorize(productId, new OnAuthListener() {
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
                String productId = LicenseManager.getInstance().getLicenseProductId();
                IAuth eagleApi = EagleApi.getInstance().getAuth(activity);
                boolean result = eagleApi.isAuthorized(productId);

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

    static void showSDKVersion(Context context) {
        IAuth eagleApi = EagleApi.getInstance().getAuth(context);
        String sdkVersion = eagleApi.getVersion();
        Toast.makeText(context, "SDK Version:" + sdkVersion, Toast.LENGTH_LONG).show();
    }
}
