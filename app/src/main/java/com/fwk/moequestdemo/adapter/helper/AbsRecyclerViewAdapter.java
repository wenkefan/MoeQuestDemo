package com.fwk.moequestdemo.adapter.helper;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wenke on 2018/1/23.
 */

public abstract class AbsRecyclerViewAdapter extends RecyclerView.Adapter<AbsRecyclerViewAdapter.ClickableViewHolder> {

    private Context context;
    protected RecyclerView mRecyclerView;
    private OnItmeClickListener listener;

    public AbsRecyclerViewAdapter(RecyclerView recyclerView){
        this.mRecyclerView = recyclerView;
    }

    public void bindContext(Context context){
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    @Override
    public void onBindViewHolder(final ClickableViewHolder holder, final int position) {
        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onItemClick(position,holder);
                }
            }
        });
    }

    public static class ClickableViewHolder extends RecyclerView.ViewHolder {

        private View parentView;

        public ClickableViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
        }

        public View getParentView() {
            return parentView;
        }

        public <T extends View> T $(@IdRes int id) {
            return parentView.findViewById(id);
        }
    }

    /**
     * 自定义RecyclerView点击监听
     */
    public interface OnItmeClickListener{
       void onItemClick(int position, ClickableViewHolder holder);
    }

    public final void setOnItmeClickListener(OnItmeClickListener listener){
        this.listener = listener;
    }

}
