package com.purecode.househunt.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.purecode.househunt.R;
import com.purecode.househunt.ui.fragment.HouseListFragment;

public class MainActivity extends AppCompatActivity {

    private HouseListFragment mHouseListFragment = new HouseListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.house_list_container, mHouseListFragment).commit();
    }

}
