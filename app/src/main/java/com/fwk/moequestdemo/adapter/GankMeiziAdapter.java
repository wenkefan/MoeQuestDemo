package com.fwk.moequestdemo.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.adapter.helper.AbsRecyclerViewAdapter;
import com.fwk.moequestdemo.entity.gank.GankMeizi;
import com.fwk.moequestdemo.widget.RatioImageView;

import java.util.List;

/**
 * Created by wenke on 2018/1/23.
 */

public class GankMeiziAdapter extends AbsRecyclerViewAdapter {

    private List<GankMeizi> meizis;

    public GankMeiziAdapter(RecyclerView recyclerView, List<GankMeizi> meizis) {
        super(recyclerView);
        this.meizis = meizis;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        ClickableViewHolder holder = new ItemViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.card_item_meizi, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder holde = (ItemViewHolder) holder;
            holde.mTextView.setText(meizis.get(position).getDesc());
            Glide.with(getContext())
                    .load(meizis.get(position).getUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.placeholder_image)
                    .into(holde.ratioImageView)
                    .getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                            if (!holde.item.isShown()) {
                                holde.item.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            holde.ratioImageView.setTag(R.string.app_name, meizis.get(position).getUrl());
            ViewCompat.setTransitionName(holde.ratioImageView, meizis.get(position).getUrl());
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return meizis == null ? 0 : meizis.size();
    }

    private class ItemViewHolder extends ClickableViewHolder {

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
