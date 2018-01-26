package com.fwk.moequestdemo.utils;

import com.fwk.moequestdemo.entity.gank.GankMeizi;
import com.fwk.moequestdemo.entity.gank.GankMeiziInfo;
import com.fwk.moequestdemo.entity.meizitu.MeiziTu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by wenke on 2018/1/22.
 */

public class MeiziUtil {

    private static MeiziUtil mCache;

    private MeiziUtil() {

    }

    public static MeiziUtil getInstance() {
        if (mCache == null) {
            synchronized (MeiziUtil.class) {
                if (mCache == null) {
                    mCache = new MeiziUtil();
                }
            }
        }
        return mCache;
    }

    /**
     * 保存Gank到数据库
     *
     * @param gankMeiziInfos
     */
    public final void putGankMeiziCache(List<GankMeiziInfo> gankMeiziInfos) {
        GankMeizi meizi;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < gankMeiziInfos.size(); i++) {
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

    /**
     * 解析妹子图html
     */
    public final List<MeiziTu> parserMeiziTuHtml(String html, String type) {

        List<MeiziTu> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("li");

        Element aelement;
        Element imgelement;
        for (int i = 7; i < links.size(); i++) {
            imgelement = links.get(i).select("img").first();
            aelement = links.get(i).select("a").first();
            MeiziTu bean = new MeiziTu();
            bean.setOrder(i);
            bean.setTitle(imgelement.attr("alt"));
            bean.setType(type);
            bean.setHeight(354);
            bean.setWidth(236);
            bean.setImageurl(imgelement.attr("data-original"));
            bean.setUrl(aelement.attr("href"));
            bean.setGroupid(url2groupid(bean.getUrl()));
            list.add(bean);
        }
        return list;
    }

    /**
     * 解析妹子图html
     */
    public final MeiziTu parserMeiziTuListHtml(String html, String type) {

        Document doc = Jsoup.parse(html);
        Elements links = doc.select("p");
//        LogUtil.d("links______________"+links.toString());


        Element aelement;
        Element imgelement;
        imgelement = links.select("img").first();
        aelement = links.select("a").first();
        MeiziTu bean = new MeiziTu();
        bean.setOrder(1);
        bean.setTitle(imgelement.attr("alt"));
        bean.setType(type);
        bean.setHeight(354);
        bean.setWidth(236);
        bean.setImageurl(imgelement.attr("src"));
        bean.setUrl(aelement.attr("href"));
        bean.setGroupid(url2groupid(bean.getUrl()));
        return bean;
    }

    /**
     * 获取妹子图的GroupId
     */
    private int url2groupid(String url) {

        return Integer.parseInt(url.split("/")[3]);
    }

    /**
     * 保存妹子图数据到数据库中
     */
    public final void putMeiziTuCache(List<MeiziTu> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();
        realm.close();
    }
}
