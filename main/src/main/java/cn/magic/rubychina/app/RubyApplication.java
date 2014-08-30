package cn.magic.rubychina.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by magic on 2014/8/6.
 */
public class RubyApplication extends Application{
    public static final int VERSION=1;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext=getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

}
