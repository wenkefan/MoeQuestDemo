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
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.adapter.helper.AbsRecyclerViewAdapter;
import com.fwk.moequestdemo.entity.meizitu.MeiziTu;
import com.fwk.moequestdemo.widget.RatioImageView;

import java.util.List;

/**
 * Created by 范文轲 on 2018/1/25.
 */

public class MeiziTuAdapter extends AbsRecyclerViewAdapter {

    private RecyclerView mRecyclerView;
    private List<MeiziTu> list;

    public MeiziTuAdapter(RecyclerView recyclerView, List<MeiziTu> list) {
        super(recyclerView);
        this.mRecyclerView = recyclerView;
        this.list = list;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.card_item_meizi,parent,false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTextView.setText(list.get(position).getTitle());
            Glide.with(getContext())
                    .load(list.get(position).getImageurl())
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
                    list.get(position).getImageurl());
            ViewCompat.setTransitionName(itemViewHolder.ratioImageView,
                    list.get(position).getImageurl());
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemViewHolder extends ClickableViewHolder{

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
