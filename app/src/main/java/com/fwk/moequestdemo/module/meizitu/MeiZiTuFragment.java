package com.fwk.moequestdemo.module.meizitu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.base.RxBaseFragment;
import com.fwk.moequestdemo.utils.ConstantUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wenke on 2018/1/18.
 */

public class MeiZiTuFragment extends RxBaseFragment {

    @Bind(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private List<String> titles = Arrays.asList("首页", "热门", "推荐", "性感", "日本", "台湾", "清纯");

    private List<String> types = Arrays.asList(
            ConstantUtil.HOME,
            ConstantUtil.HOT_MEIZI,
            ConstantUtil.TUIJIAN_MEIZI,
            ConstantUtil.XINGGAN_MEIZI,
            ConstantUtil.JAPAN_MEIZI,
            ConstantUtil.TAIWAN_MEIZI,
            ConstantUtil.QINGCHUN_MEIZI);

    private MeiZiTuFragment() {
        super();
    }

    public static MeiZiTuFragment getInstance() {
        return new MeiZiTuFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizitu;
    }

    @Override
    protected void initViews() {
        initFragment();
    }

    private void initFragment() {
        mViewPager.setAdapter(new MeiziuPageAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(1);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private class MeiziuPageAdapter extends FragmentStatePagerAdapter {

        public MeiziuPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MeiziTuSimpleFragment.getInstance(types.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }
}
