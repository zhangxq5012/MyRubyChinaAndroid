package cn.magic.rubychina.app;

import android.content.Context;

import com.activeandroid.app.Application;

import cn.magic.rubychina.dao.NodeUtil;

/**
 * Created by magic on 2014/8/6.
 */
public class RubyApplication extends Application {
    public static final int VERSION=1;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext=getApplicationContext();
        NodeUtil.saveNodeInfos(false);
    }

    public static Context getContext() {
        return sContext;
    }

}
