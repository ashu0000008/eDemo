package com.pax.eagle.demo;

import android.content.Context;

import com.pax.dal.ISys;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.eagle.api.IDeviceSNFetcher;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class SNFetcher implements IDeviceSNFetcher {
    private final Context mContext;

    public SNFetcher(Context context) {
        mContext = context;
    }

    @Override
    public String getDeviceSN() {
        try {
            ISys iSys = NeptuneLiteUser.getInstance().getDal(mContext).getSys();
            return iSys.getTermInfo().get(ETermInfoKey.SN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
