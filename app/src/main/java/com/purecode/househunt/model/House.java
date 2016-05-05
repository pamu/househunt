package com.purecode.househunt.model;

import java.util.List;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class House {

    private String mTitle;
    private List<String> images;
    private String mListingId;
    private int mRent;
    private int mDeposit;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getmListingId() {
        return mListingId;
    }

    public void setmListingId(String mListingId) {
        this.mListingId = mListingId;
    }

    public int getmRent() {
        return mRent;
    }

    public void setmRent(int mRent) {
        this.mRent = mRent;
    }

    public int getmDeposit() {
        return mDeposit;
    }

    public void setmDeposit(int mDeposit) {
        this.mDeposit = mDeposit;
    }
}
