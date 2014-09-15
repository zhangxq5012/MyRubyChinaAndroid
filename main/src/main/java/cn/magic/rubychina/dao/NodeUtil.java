package cn.magic.rubychina.dao;

import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.vo.Node;
import cn.magic.rubychina.vo.Topic;

/**
 * Created by magic on 2014/9/14.
 */
public class NodeUtil {


    /**
     * 从网络获取节点信息存储到数据库
     * @param  isFroce 是否强制执行,如果原本已经获取过信息则不需要再次获取,如果这个参数是ture,则
     *                 任何情况下都会重新获取
     * */
    public static void saveNodeInfos(boolean isFroce){
        if(isFroce){
            new Delete().from(Node.class).execute();
        }
        if(!isLoaded()){
            StringCharsetRequest  request=new StringCharsetRequest(NetWorkUtil.NODE,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Node>>() {}.getType();
                    ArrayList nodes=gson.fromJson(response, listType);
                    BaseDao.insertList(nodes);
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("NodeUtil","获取node信息失败,请检查网络连接");
                }
            });
            NetWorkUtil.getInstance().getRequestQueue().add(request);
        }
    }

    public static boolean isLoaded(){
        return new Select().from(Node.class).count()>0;
    }

    public static List<Node> getAllNodes(){
        return new Select().from(Node.class).execute();
    }

    public static String[] getAllSectionName(){
        List<String> sectionNames=new ArrayList<String>();
        List<Node> nodes=new Select().from(Node.class).execute();
        if(nodes!=null&&nodes.size()>0){
            for(Node node:nodes){
                if(!sectionNames.contains(node.section_name)){
                    sectionNames.add(node.section_name);
                }
            }
        }
        return sectionNames.toArray(new String[0]);


//        return new Select(Node.SECTION_NAME).distinct().from(Node.class).orderBy(Node.SECTION_ID).execute().toArray(new String[0]);
    }

    public static List<Node> get(String sectionName){
        return new Select().from(Node.class).where(Node.SECTION_NAME+"=?",sectionName).orderBy(Node.SORT).execute();
    }

}
