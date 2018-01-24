package com.fwk.moequestdemo.module.douban;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.base.RxBaseFragment;

/**
 * Created by wenke on 2018/1/18.
 */

public class DouBanMeiZiFragment extends RxBaseFragment {

    private DouBanMeiZiFragment(){
        super();
    }

    public static DouBanMeiZiFragment getInstance(){
        return new DouBanMeiZiFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected void initViews() {

    }
}
