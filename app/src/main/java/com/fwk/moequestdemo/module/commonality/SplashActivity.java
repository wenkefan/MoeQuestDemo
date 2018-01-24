package com.fwk.moequestdemo.module.commonality;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.fwk.moequestdemo.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wenke on 2018/1/18.
 */

public class SplashActivity extends Activity {
    @Bind(R.id.iv_splash)
    ImageView mSpalsh;

    private static final int ANIMATION_TIME = 2000;
    private static final float SCALE_END = 1.13f;

    private Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        subscription = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startAnim();
                    }
                });
    }

    private void startAnim() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mSpalsh, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mSpalsh, "scaleY", 1f, SCALE_END);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_TIME);
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                SplashActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        ButterKnife.unbind(this);
    }
}
