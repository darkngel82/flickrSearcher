package com.dani.nv.flickrsearcher;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

public class AppApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
