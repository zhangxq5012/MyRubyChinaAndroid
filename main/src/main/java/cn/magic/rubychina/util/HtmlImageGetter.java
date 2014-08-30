package cn.magic.rubychina.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import cn.magic.rubychina.main.R;

/**
 * Created by magic on 2014/8/26.
 */
public class HtmlImageGetter implements Html.ImageGetter {
    Context context;
    TextView textView;
    String text;

    public HtmlImageGetter(Context context, TextView view, String text) {
        this.context = context;
        this.textView = view;
        this.text = text;
    }



    @Override
    public Drawable getDrawable(String source) {
//        Drawable drawable=context.getResources().getDrawable(R.drawable.avatar_middle);
//        drawable.setBounds(new Rect(0,0,400,200));
//        return drawable;
        Bitmap bitmap=LruBitmapCache.getInstance().getBitmap(source);
        if(bitmap==null){
            ImageGetterTast task=new ImageGetterTast();
            task.execute(source);
        }else{
            Drawable drawable=new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth() * 2, 0
                    + drawable.getIntrinsicHeight() * 2);
//            drawable.setBounds(new Rect(0,0,400,200));
            return drawable;
        }
        return null;

    }



    public class ImageGetterTast extends AsyncTask<String,Void,Drawable>{

        @Override
        protected Drawable doInBackground(String... params) {
            String url=params[0];
            Drawable drawable= fetchDrawable(url);
            drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth() * 2, 0
                    + drawable.getIntrinsicHeight() * 2);
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bitmap= bd.getBitmap();
            LruBitmapCache.getInstance().addBitmapToCache(url,bitmap);
            return  drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            textView.setText(Html.fromHtml(text,HtmlImageGetter.this,null));
        }
    }

    /***
     * Get the Drawable from URL
     * @param urlString
     * @return
     */
    public Drawable fetchDrawable(String urlString) {
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = Drawable.createFromStream(is, "src");
            drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth() * 2, 0
                    + drawable.getIntrinsicHeight() * 2);
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }


}
