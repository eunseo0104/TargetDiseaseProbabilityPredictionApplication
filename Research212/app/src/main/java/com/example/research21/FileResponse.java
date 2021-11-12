package com.example.research21;

import com.google.gson.annotations.SerializedName;

public class FileResponse {
    @SerializedName("pMax")
    String pMax;
    @SerializedName("pMin")
    String pMin;
    @SerializedName("gCount")
    String gCount;

    public String getpMax() {
        return pMax;
    }

    public String getpMin() {
        return pMin;
    }

    public String getgCount() {
        return gCount;
    }

    public void setpMax(String pMax) {
        this.pMax = pMax;
    }

    public void setpMin(String pMin) {
        this.pMin = pMin;
    }

    public void setgCount(String gCount) {
        this.gCount = gCount;
    }
}
