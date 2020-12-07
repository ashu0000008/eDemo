package com.pax.eagle.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.tv_auth).setOnClickListener(v -> {
            ApiDemo.authorize(this);
        });
        findViewById(R.id.tv_status).setOnClickListener(v -> {
            ApiDemo.checkAuth(MainActivity.this);
        });
        findViewById(R.id.tv_device_id).setOnClickListener(v -> {
            ApiDemo.showDeviceId(this);
        });

        findViewById(R.id.tv_select_license).setOnClickListener(v -> {
            LicenseListActivity.start(this);
        });
    }

    @Override
    protected void onResume() {
        String licenseName = LicenseManager.getInstance().getCurrentLicense();
        TextView tvCurrent = findViewById(R.id.tv_current_license);
        tvCurrent.setText("current:" + licenseName);
        super.onResume();
    }
}