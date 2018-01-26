package com.fwk.moequestdemo.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    /**
     * 分类查询
     */
    @Headers({"User-Agent: a",
            "Cache-Control: max-stale=1800"})
    @GET("{type}")
    Observable<ResponseBody> getMeiziTuType(@Path("type") String type);

    /**
     * 查询组内妹子图
     */
    @Headers({"User-Agent: a",
            "Cache-Control: max-stale=1800"})
    @GET("{type}/{id}")
    Observable<ResponseBody> getMeiziTuMeiziList(@Path("type") String type, @Path("id") String id);
}
