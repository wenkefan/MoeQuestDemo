package com.fwk.moequestdemo.entity.gank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wenke on 2018/1/22.
 */

public class GankMeiziResult {

    public boolean error;

    @SerializedName("results")
    public List<GankMeiziInfo> gankMeizis;

}
