package com.fwk.moequestdemo.network.api;

import com.fwk.moequestdemo.entity.gank.GankMeizi;
import com.fwk.moequestdemo.entity.gank.GankMeiziResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wenke on 2018/1/22.
 */

public interface GankMeiziApi {

    @GET("data/福利/{number}/{page}")
    Observable<GankMeiziResult> getGankMeizi(@Path("number") int number, @Path("page") int page);
}
