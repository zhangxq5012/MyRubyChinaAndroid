package cn.magic.rubychina.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import cn.magic.rubychina.main.MainActivity;
import cn.magic.rubychina.main.R;

/**
 * Created by magic on 2014/8/8.
 */
public class LruBitMapCacheTest extends ActivityInstrumentationTestCase2<MainActivity>{
    public static String URL="cn.magic.rubychina.util.url";

    MainActivity mainActivity;
    public LruBitMapCacheTest(String pkg, Class activityClass) {
        super(pkg, activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity=getActivity();
    }

    public void testMainActivity(){
       Bitmap bitmap= BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.avatar_middle);
        LruBitmapCache lruCache= LruBitmapCache.getInstance(mainActivity);
        lruCache.addBitmapToCache(URL, bitmap);
        Bitmap bitmap1=lruCache.getBitmap(URL);
        assertNotNull(bitmap1);


    }
}
