package cn.magic.rubychina.util;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * 根据指定字符编码解析的StringRequest
 * Created by magic on 2014/8/13.
 */
public class StringCharsetRequest extends StringRequest {
    String charSet;
    public StringCharsetRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener,String charSet) {
        super(method, url, listener, errorListener);
        this.charSet=charSet;
    }
    public StringCharsetRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener,String charSet) {
        this(Method.GET, url, listener, errorListener, charSet);
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
}
