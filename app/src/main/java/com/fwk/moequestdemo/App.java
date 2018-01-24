package com.fwk.moequestdemo;

import android.app.Application;
import android.content.Context;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by wenke on 2018/1/18.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        RealmConfiguration configuration = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(6)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm dynamicRealm, long l, long l1) {

                    }
                })
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    public static Context getInstance(){
        return mContext;
    }
}
