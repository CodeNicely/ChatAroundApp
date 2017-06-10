package com.fame.plumbum.chataround.shouts.model;

import com.google.gson.annotations.SerializedName;

public class Like {
    @SerializedName("LikerName")
    private String LikerName;
    @SerializedName("LikerId")
    private String LikerId;
    @SerializedName("TimeEpoch")
    private int TimeEpoch;

    public String getLikerName() {
        return LikerName;
    }

    public String getLikerId() {
        return LikerId;
    }

    public int getTimeEpoch() {
        return TimeEpoch;
    }
}
