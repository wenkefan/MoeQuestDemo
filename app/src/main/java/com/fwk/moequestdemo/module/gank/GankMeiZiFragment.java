package com.fwk.moequestdemo.module.gank;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.adapter.GankMeiziAdapter;
import com.fwk.moequestdemo.adapter.helper.AbsRecyclerViewAdapter;
import com.fwk.moequestdemo.base.RxBaseFragment;
import com.fwk.moequestdemo.entity.gank.GankMeizi;
import com.fwk.moequestdemo.entity.gank.GankMeiziInfo;
import com.fwk.moequestdemo.entity.gank.GankMeiziResult;
import com.fwk.moequestdemo.network.RetrofitHelper;
import com.fwk.moequestdemo.utils.MeiziUtil;
import com.fwk.moequestdemo.utils.SnackbarUtil;
import com.fwk.moequestdemo.widget.LinearLayoutManagerWrapper;

import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenke on 2018/1/18.
 */

public class GankMeiZiFragment extends RxBaseFragment {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipe;
    @Bind(R.id.recycle)
    RecyclerView mRecyclerView;

    private Realm realm;

    private boolean mIsRefreshing = false;

    private int pageNum = 20;//每页的数量

    private int page = 1;//第几页

    private RealmResults<GankMeizi> gankMeizis;

    private GankMeiziAdapter mAdapter;

    private StaggeredGridLayoutManager layoutManager;

    private static final int PRELOAD_SIZE = 6;

    private boolean mIsLoadMore = true;

    private GankMeiZiFragment() {
        super();
    }

    public static GankMeiZiFragment getInstance() {
        return new GankMeiZiFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank_meizi;
    }

    @Override
    protected void initViews() {
        showProgress();
        realm = Realm.getDefaultInstance();
        gankMeizis = realm.where(GankMeizi.class).findAll();
        initRecycleView();
    }

    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManagerWrapper(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(OnLoadMoreListener());
        mAdapter = new GankMeiziAdapter(mRecyclerView,gankMeizis);
        mRecyclerView.setAdapter(mAdapter);
        setRecycleScrollBug();
    }

    RecyclerView.OnScrollListener OnLoadMoreListener(){
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(
                        new int[2])[1] >= mAdapter.getItemCount() - PRELOAD_SIZE;
                if (!mSwipe.isRefreshing() && isBottom){
                    if (!mIsLoadMore){
                        mSwipe.setRefreshing(false);
                        page++;
                        getGankMeizi();
                    } else {
                        mIsLoadMore = false;
                    }
                }
            }
        };
    }

    private void setRecycleScrollBug() {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }

    private void showProgress() {
        mSwipe.setColorSchemeResources(R.color.colorPrimary);
        mSwipe.post(new Runnable() {
            @Override
            public void run() {
                mSwipe.setRefreshing(true);
                clearCache();
                mIsRefreshing = true;
                getGankMeizi();
            }
        });

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                clearCache();
                mIsRefreshing = true;
                getGankMeizi();
            }
        });
    }

    private void clearCache() {
        try {
            realm.beginTransaction();
            realm.where(GankMeizi.class)
                    .findAll().clear();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getGankMeizi() {

        RetrofitHelper.getGankMeiziApi()
                .getGankMeizi(pageNum,page)
                .compose(this.<GankMeiziResult>bindToLifecycle())
                .filter(new Func1<GankMeiziResult, Boolean>() {
                    @Override
                    public Boolean call(GankMeiziResult gankMeiziResult) {
                        return !gankMeiziResult.error;
                    }
                })
                .map(new Func1<GankMeiziResult, Object>() {
                    @Override
                    public Object call(GankMeiziResult gankMeiziResult) {
                        return gankMeiziResult.gankMeizis;
                    }
                })
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        MeiziUtil.getInstance().putGankMeiziCache((List<GankMeiziInfo>) o);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finishTask();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSwipe.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipe.setRefreshing(false);
                            }
                        });
                        SnackbarUtil.showMessage(mRecyclerView,getString(R.string.error_message));
                    }
                });
    }

    private void finishTask() {
        if (page * pageNum - pageNum - 1 > 0){
            mAdapter.notifyItemRangeChanged(page * pageNum - pageNum - 1, pageNum);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
        mIsRefreshing = false;
        mAdapter.setOnItmeClickListener(new AbsRecyclerViewAdapter.OnItmeClickListener() {
            @Override
            public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {

            }
        });
    }

}
