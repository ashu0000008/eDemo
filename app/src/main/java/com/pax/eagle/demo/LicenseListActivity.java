package com.pax.eagle.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LicenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LicenseFileAdapter adapter;
    private final List<String> mData = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, LicenseListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_recycle_list);
        recyclerView = findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LicenseFileAdapter(this, R.layout.item_license_file, mData);
        recyclerView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            List<String> data = LicenseManager.getInstance().getList();
            LicenseListActivity.this.runOnUiThread(() -> {
                mData.clear();
                mData.addAll(data);
                adapter = new LicenseFileAdapter(this, R.layout.item_license_file, mData);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    public void closeSelf() {
        finish();
    }
}
