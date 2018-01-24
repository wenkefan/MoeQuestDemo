package com.fwk.moequestdemo.module.jiandan;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.base.RxBaseFragment;

/**
 * Created by wenke on 2018/1/18.
 */

public class JianDanMeiZiFragment extends RxBaseFragment {

    private JianDanMeiZiFragment(){
        super();
    }

    public static JianDanMeiZiFragment getInstance(){
        return new JianDanMeiZiFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected void initViews() {

    }
}
