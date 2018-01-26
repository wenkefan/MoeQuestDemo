package com.fwk.moequestdemo.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.adapter.helper.AbsRecyclerViewAdapter;
import com.fwk.moequestdemo.base.RxBaseFragment;
import com.fwk.moequestdemo.entity.meizitu.MeiziTu;
import com.fwk.moequestdemo.module.meizitu.MeiziTuSimpleFragment;
import com.fwk.moequestdemo.network.RetrofitHelper;
import com.fwk.moequestdemo.utils.LogUtil;
import com.fwk.moequestdemo.utils.MeiziUtil;
import com.fwk.moequestdemo.utils.SnackbarUtil;
import com.fwk.moequestdemo.widget.RatioImageView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 范文轲 on 2018/1/25.
 */

public class MeiziTuAdapter extends AbsRecyclerViewAdapter {

    private RecyclerView mRecyclerView;
    private List<MeiziTu> list;
    private String type;
    private RxBaseFragment fragment;

    public MeiziTuAdapter(RecyclerView recyclerView, List<MeiziTu> list, String type, MeiziTuSimpleFragment meiziTuSimpleFragment) {
        super(recyclerView);
        this.mRecyclerView = recyclerView;
        this.list = list;
        this.type = type;
        this.fragment = meiziTuSimpleFragment;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.card_item_meizi, parent, false));
    }

    boolean isb = false;

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTextView.setText(list.get(position).getTitle());


            RetrofitHelper.getMeiziTuApi()
                    .getMeiziTuMeiziList(list.get(position).getUrl().substring(list.get(position).getUrl().length() - 6), "1")
                    .compose(fragment.<ResponseBody>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ResponseBody>() {
                        @Override
                        public void call(ResponseBody responseBody) {
                            try {
                                String html = responseBody.string();
                                MeiziTu meiziTu = MeiziUtil.getInstance().parserMeiziTuListHtml(html, type);
                                LogUtil.d("ImageUrl_______" + meiziTu.getImageurl());
                                LazyHeaders builder = new LazyHeaders.Builder()
                                        .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Mobile Safari/537.36")
                                        .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                                        .addHeader("Accept-Encoding", "gzip, deflate")
                                        .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                                        .addHeader("Proxy-Connection", "keep-alive")
                                        .addHeader("Referer", "http://m.mzitu.com/hot/").build();

                                GlideUrl glideUrl = new GlideUrl(meiziTu.getImageurl(), builder);
                                Glide.with(getContext())
                                        .load(glideUrl)
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.mipmap.placeholder_image)
                                        .into(itemViewHolder.ratioImageView)
                                        .getSize(new SizeReadyCallback() {
                                            @Override
                                            public void onSizeReady(int width, int height) {
                                                if (!itemViewHolder.item.isShown()) {
                                                    itemViewHolder.item.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        });
                                itemViewHolder.ratioImageView.setTag(R.string.app_name,
                                        meiziTu.getImageurl());
                                ViewCompat.setTransitionName(itemViewHolder.ratioImageView,
                                        meiziTu.getImageurl());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            SnackbarUtil.showMessage(mRecyclerView, getContext().getString(R.string.error_message));
                            LogUtil.d("throwable___" + throwable.toString());
                        }
                    });


//            Glide.with(getContext())
//                    .load(list.get(position).getImageurl())
//                    .centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.mipmap.placeholder_image)
//                    .into(itemViewHolder.ratioImageView)
//                    .getSize(new SizeReadyCallback() {
//                        @Override
//                        public void onSizeReady(int width, int height) {
//                            if (!itemViewHolder.item.isShown()) {
//                            itemViewHolder.item.setVisibility(View.VISIBLE);
//                        }
//                        }
//                    });
//            itemViewHolder.ratioImageView.setTag(R.string.app_name,
//                    list.get(position).getImageurl());
//            ViewCompat.setTransitionName(itemViewHolder.ratioImageView,
//                    list.get(position).getImageurl());
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemViewHolder extends ClickableViewHolder {

        public RatioImageView ratioImageView;

        public TextView mTextView;

        public View item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            ratioImageView = $(R.id.item_img);
            mTextView = $(R.id.item_title);
            ratioImageView.setOriginalSize(50, 50);
        }
    }
}
