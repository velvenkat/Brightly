package com.digital_easy.info_share.Application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Brightly extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // The default Realm file is "default.realm" in Context.getFilesDir();
        // we'll change it to "myrealm.realm"
        Fresco.initialize(this);
        Fabric.with(this, new Crashlytics());
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

    }
}
