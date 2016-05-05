package com.purecode.househunt.model;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public interface Callback<T> {
    public void onSuccess(T t);
    public void onFailure(Exception ex);
}
