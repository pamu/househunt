package com.purecode.househunt.modules;

import android.content.Context;

import com.purecode.househunt.modules.webservices.WebServices;
import com.purecode.househunt.modules.webservices.impl.WebServicesImpl;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class ServiceLoader {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static WebServices getWebServices() {
        return WebServicesImpl.getInstance(mContext);
    }
}
