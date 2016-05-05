package com.purecode.househunt.modules.webservices;

import com.purecode.househunt.model.Callback;
import com.purecode.househunt.model.House;

import java.util.List;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public interface WebServices {
    void fetchHouses(int pageNumber, int pageSize, Callback<List<House>> callback);
}
