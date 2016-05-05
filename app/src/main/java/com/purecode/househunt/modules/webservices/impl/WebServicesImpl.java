package com.purecode.househunt.modules.webservices.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purecode.househunt.model.Callback;
import com.purecode.househunt.model.House;
import com.purecode.househunt.modules.webservices.WebServices;
import com.purecode.househunt.utils.ResponseParser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class WebServicesImpl implements WebServices {

    private Context mContext;

    private static WebServicesImpl mWebServicesImpl;

    private static RequestQueue mRequestQueue;

    private WebServicesImpl(Context context) {
        this.mContext = context;
    }

    public static WebServices getInstance(Context context) {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        if (mWebServicesImpl == null) {
            mWebServicesImpl = new WebServicesImpl(context);
        }

        return mWebServicesImpl;
    }

    private String getURL(int pageNumber, int pageSize) {
        final String url = "http://api.grabhouse.com/search/listing/web/572b2ef82d79f3324c9d3a74?from=" + pageNumber + "&size=" + pageSize;
        Log.e("requesting url", url);
        return url;
    }

    @Override
    public void fetchHouses(int pageNumber, int pageSize, final Callback<List<House>> callback) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, getURL(pageNumber, pageSize), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(ResponseParser.getHouses(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error);
            }
        });
        req.setShouldCache(true);
        mRequestQueue.add(req);
    }
}
