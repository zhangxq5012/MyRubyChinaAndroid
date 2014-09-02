package cn.magic.rubychina.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 根据指定字符编码解析的StringRequest
 * Created by magic on 2014/8/13.
 */
public class StringCharsetRequest extends StringRequest {


    String charSet;
    Map<String, String> param=null;
    public StringCharsetRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener,String charSet) {
        super(method, url, listener, errorListener);
        this.charSet=charSet;
    }
    public StringCharsetRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(method, url, listener, errorListener, NetWorkUtil.CHARSET);
    }
    public StringCharsetRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener,String charSet) {
        this(Method.GET, url, listener, errorListener, charSet);
    }

    public StringCharsetRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener, NetWorkUtil.CHARSET);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            //返回的是UTF-8编码的，但是返回的http头没有这些信息
            parsed = new String(response.data,charSet);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return getParam();
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }
}
