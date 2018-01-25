package com.fwk.moequestdemo.module.meizitu;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.adapter.MeiziTuAdapter;
import com.fwk.moequestdemo.base.RxBaseFragment;
import com.fwk.moequestdemo.entity.meizitu.MeiziTu;
import com.fwk.moequestdemo.network.RetrofitHelper;
import com.fwk.moequestdemo.utils.MeiziUtil;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by wenke on 2018/1/24.
 */

public class MeiziTuSimpleFragment extends RxBaseFragment {

    @Bind(R.id.recycle)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String EXTRA_TYPE = "extra_type";

    private String type;

    private boolean mIsRefreshing = false;

    private Realm realm;

    private int pageName = 24;

    private int page = 1;

    private MeiziTuAdapter mAdapter;

    private List<MeiziTu> list;

    private StaggeredGridLayoutManager mLayoutManager;

    public static MeiziTuSimpleFragment getInstance(String type) {
        MeiziTuSimpleFragment mFragment = new MeiziTuSimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TYPE, type);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_meizi;
    }

    @Override
    protected void initViews() {
        type = getArguments().getString(EXTRA_TYPE);
        showProgress();
        realm = Realm.getDefaultInstance();
        list = realm.where(MeiziTu.class)
                .equalTo("type",type)
                .findAll();
        initRecycleView();
    }

    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MeiziTuAdapter(mRecyclerView,list);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showProgress() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mIsRefreshing = true;
                clearCache();
                getMeiziTu();
            }
        });

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                clearCache();
                getMeiziTu();
            }
        },500);
    }

    private void clearCache() {
        try {
            realm.beginTransaction();
            realm.where(MeiziTu.class)
                    .equalTo("type", type)
                    .findAll()
                    .clear();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMeiziTu() {
        RetrofitHelper.getMeiziTuApi()
                .getMeiziTuType(type)
                .compose(this.<ResponseBody>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            String html = responseBody.string();
                            List<MeiziTu> list = MeiziUtil.getInstance().parserMeiziTuHtml(html,type);
                            MeiziUtil.getInstance().putMeiziTuCache(list);
                            findisTask();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private void findisTask(){
        mAdapter.notifyDataSetChanged();
        if (mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mIsRefreshing = false;
    }
}
