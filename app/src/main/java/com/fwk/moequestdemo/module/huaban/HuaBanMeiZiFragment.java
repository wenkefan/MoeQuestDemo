package com.fwk.moequestdemo.module.huaban;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.base.RxBaseFragment;

/**
 * Created by wenke on 2018/1/18.
 */

public class HuaBanMeiZiFragment extends RxBaseFragment {

    private HuaBanMeiZiFragment(){
        super();
    }

    public static HuaBanMeiZiFragment getInstance(){
        return new HuaBanMeiZiFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected void initViews() {

    }
}
