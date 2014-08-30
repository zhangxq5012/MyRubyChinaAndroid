package cn.magic.rubychina.util;

import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by magic on 2014/8/26.
 */
public class URLImageLoader {
    private static URLImageLoader urlImageLoader;
    public  Bitmap bitmap1;

    private URLImageLoader(){
    }

    public static URLImageLoader getInstance(){
        if(urlImageLoader==null){
            urlImageLoader=new URLImageLoader();
        }
        return urlImageLoader;
    }



    public  void setBitmap(Bitmap bitmap) {
        Bitmap bitmap1 = bitmap;
    }

    public Bitmap getBitmap1( ) {
        return bitmap1;
    }
}
