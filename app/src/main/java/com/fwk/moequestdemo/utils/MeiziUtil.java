package com.fwk.moequestdemo.utils;

import com.fwk.moequestdemo.entity.gank.GankMeizi;
import com.fwk.moequestdemo.entity.gank.GankMeiziInfo;

import java.util.List;

import io.realm.Realm;

/**
 * Created by wenke on 2018/1/22.
 */

public class MeiziUtil {

    private static MeiziUtil mCache;

    private MeiziUtil(){

    }

    public static MeiziUtil getInstance(){
        if (mCache == null){
            synchronized (MeiziUtil.class){
                if (mCache == null){
                    mCache = new MeiziUtil();
                }
            }
        }
        return mCache;
    }

    /**
     * 保存Gank到数据库
     * @param gankMeiziInfos
     */
    public void putGankMeiziCache(List<GankMeiziInfo> gankMeiziInfos){
        GankMeizi meizi;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < gankMeiziInfos.size(); i++){
            meizi = new GankMeizi();
            String url = gankMeiziInfos.get(i).url;
            String desc = gankMeiziInfos.get(i).desc;
            meizi.setUrl(url);
            meizi.setDesc(desc);
            realm.copyToRealm(meizi);
        }
        realm.commitTransaction();
        realm.close();
    }
}
