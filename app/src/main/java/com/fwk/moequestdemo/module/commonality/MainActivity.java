package com.fwk.moequestdemo.module.commonality;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.fwk.moequestdemo.R;
import com.fwk.moequestdemo.base.RxBaseActivity;
import com.fwk.moequestdemo.module.douban.DouBanMeiZiFragment;
import com.fwk.moequestdemo.module.gank.GankMeiZiFragment;
import com.fwk.moequestdemo.module.huaban.HuaBanMeiZiFragment;
import com.fwk.moequestdemo.module.jiandan.JianDanMeiZiFragment;
import com.fwk.moequestdemo.module.meizitu.MeiZiTuFragment;
import com.fwk.moequestdemo.module.taogirl.TaoGirlMeiZiFragment;
import com.fwk.moequestdemo.utils.SnackbarUtil;
import com.fwk.moequestdemo.widget.CircleImageView;

import butterknife.Bind;

public class MainActivity extends RxBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    private int avatars = R.mipmap.ic_avatar1;

    private Fragment[] fragments;

    private int index;//当前fragment的位置
    private int currentTabIndex;//上一个fragment的位置

    private long exitTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setNavigationView();
        initFragment();
    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitle("萌妹子");
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initFragment() {

        GankMeiZiFragment gankMeiZiFragment = GankMeiZiFragment.getInstance();
        MeiZiTuFragment meiZiTuFragment = MeiZiTuFragment.getInstance();
        DouBanMeiZiFragment douBanMeiZiFragment = DouBanMeiZiFragment.getInstance();
        HuaBanMeiZiFragment huaBanMeiZiFragment = HuaBanMeiZiFragment.getInstance();
        TaoGirlMeiZiFragment taoGirlMeiZiFragment = TaoGirlMeiZiFragment.getInstance();
        JianDanMeiZiFragment jianDanMeiZiFragment = JianDanMeiZiFragment.getInstance();

        fragments = new Fragment[]{
                gankMeiZiFragment,
                meiZiTuFragment,
                douBanMeiZiFragment,
                huaBanMeiZiFragment,
                taoGirlMeiZiFragment,
                jianDanMeiZiFragment
        };

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, gankMeiZiFragment)
                .commit();
    }

    private void setNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.inflateHeaderView(R.layout.nav_header_main);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                changIndex(0, getResources().getString(R.string.gank_meizi), item);
                return true;

            case R.id.nav_meizitu:
                changIndex(1, getResources().getString(R.string.meizitu), item);
                return true;

            case R.id.nav_tao:
                changIndex(2, getResources().getString(R.string.tao_female), item);
                return true;

            case R.id.nav_douban:
                changIndex(3, getResources().getString(R.string.douban_meizi), item);
                return true;

            case R.id.nav_huaban:
                changIndex(4, getResources().getString(R.string.huaban_meizi), item);
                return true;

            case R.id.nav_jiandan:
                changIndex(5, getResources().getString(R.string.jiandan_meizi), item);
            default:
                break;
        }
        return true;
    }

    private void changIndex(int changNum, String title, MenuItem item){
        index = changNum;
        switchFragment();
        item.setChecked(true);
        mToolbar.setTitle(title);
        mDrawerLayout.closeDrawers();
    }

    private void switchFragment(){
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        if (!fragments[index].isAdded()){
            trx.add(R.id.content,fragments[index]);
        }
        trx.hide(fragments[currentTabIndex]);
        trx.show(fragments[index]);
        trx.commit();
        currentTabIndex = index;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            logoutApp();
        }
        return true;
    }

    private void logoutApp(){
        if (System.currentTimeMillis() - exitTime > 2000){
            SnackbarUtil.showMessage(mDrawerLayout,getString(R.string.back_message));
            exitTime = System.currentTimeMillis();
        } else {
            MainActivity.this.finish();
        }
    }
}
