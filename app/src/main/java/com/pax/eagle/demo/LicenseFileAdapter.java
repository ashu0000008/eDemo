package com.pax.eagle.demo;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class LicenseFileAdapter extends CommonAdapter<String> {
    private final LicenseListActivity activity;

    public LicenseFileAdapter(LicenseListActivity context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        activity = context;
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_name, s);
        holder.setOnClickListener(R.id.tv_name, v -> {
            LicenseManager.getInstance().setCurrentLicense(s);
            activity.closeSelf();
        });
    }
}
