package com.fwk.moequestdemo.module.taogirl;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.base.RxBaseFragment;

/**
 * Created by wenke on 2018/1/18.
 */

public class TaoGirlMeiZiFragment extends RxBaseFragment {

    private TaoGirlMeiZiFragment(){
        super();
    }

    public static TaoGirlMeiZiFragment getInstance(){
        return new TaoGirlMeiZiFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected void initViews() {

    }
}
