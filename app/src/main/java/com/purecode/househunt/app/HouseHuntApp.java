package com.purecode.househunt.app;

import android.app.Application;

import com.purecode.househunt.modules.ServiceLoader;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class HouseHuntApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceLoader.init(getApplicationContext());
    }
}
