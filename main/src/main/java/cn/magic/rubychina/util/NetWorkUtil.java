package cn.magic.rubychina.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.net.URL;
import java.util.Map;

/**
 * Created by magic on 2014/7/26.
 * 访问网络需要用到的类
 */
public class NetWorkUtil {

    public static String CHARSET ="UTF-8";

    public static NetWorkUtil netWorkUtil;
    //RUBY china 的URL
    public static final String RUBYCHINAURL = "https://ruby-china.org";
    //RUBYCHINA 的API的url
    public static final String API = RUBYCHINAURL + "/" + "api";
    //文章列表的URL
    public static final String TOPICS = API + "/" + "topics.json";

    public static final String TOPIC_INFO = API + "/"+"topics/%s.json";

    RequestQueue requestQueue;

    private static Context context;


    private NetWorkUtil(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static NetWorkUtil getInstance(Context context) {
        if (netWorkUtil == null) {
            netWorkUtil = new NetWorkUtil(context);
        }
        return netWorkUtil;
    }

    /**
     * 访问网络,获取json数据
     */


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        ImageLoader loader = new ImageLoader(getRequestQueue(),
                 LruBitmapCache.getInstance(context));
        return loader;
    }


    public String appendParam(String url,Map<String,String> param){
        StringBuffer strb=new StringBuffer();
        if(param.size()>0){
            strb.append("?");
            for(String key:param.keySet()){
                strb.append(key+"="+param.get(key));
                strb.append("&");
            }
            url=url+strb.substring(0,strb.length()-1) ;
        }
        return url;
    }


}
