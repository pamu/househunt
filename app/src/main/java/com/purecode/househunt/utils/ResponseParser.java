package com.purecode.househunt.utils;

import com.purecode.househunt.model.House;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class ResponseParser {
    public static List<House> getHouses(JSONObject response) {
        List<House> houses = new ArrayList<>();
        if (response != null && response.has("success") && response.has("data")) {
            try {
                JSONObject data = response.getJSONObject("data");
                JSONArray info = data.getJSONArray("info");
                for(int i = 0; i < info.length(); i ++) {
                    JSONObject houseJson = info.getJSONObject(i);
                    House house = new House();
                    try {
                        String title = houseJson.getString("title");
                        house.setmTitle(title);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        String listingId = houseJson.getString("listingId");
                        house.setmListingId(listingId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        int deposit = houseJson.getInt("deposit");
                        house.setmDeposit(deposit);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        int rent = houseJson.getInt("rent");
                        house.setmRent(rent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        List<String> images = new ArrayList<>();
                        JSONObject imagesJson = houseJson.getJSONObject("primaryPhoto");
                        String smallUrl = imagesJson.getString("smallUrl");
                        String mediumUrl = imagesJson.getString("mediumUrl");
                        String largeUrl = imagesJson.getString("largeUrl");
                        images.add(smallUrl);
                        images.add(mediumUrl);
                        images.add(largeUrl);
                        house.setImages(images);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    houses.add(house);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return houses;
    }
}
