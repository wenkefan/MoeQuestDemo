package com.fwk.moequestdemo.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wenke on 2018/1/24.
 */

public interface MeiziTuApi {


    /**
     * 根据类型查询对应的妹子图
     */
    @GET("{type}/page/{pageNum}")
    Observable<ResponseBody> getMeiziTuApi(@Path("type") String type, @Path("pageNum") int pageNum);

    /**
     * 分页查询对应的妹子图
     */
    @GET("{type}/comment-page-{page}#comments")
    Observable<ResponseBody> getHomeMeiziApi(@Path("type") String type, @Path("page") int page);
}
