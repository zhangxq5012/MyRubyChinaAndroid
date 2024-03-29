package cn.magic.rubychina.vo;

import android.util.Log;

import com.activeandroid.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by magic on 2014/7/28.
 */
public abstract class AbstractObject extends Model {
    public Object getAttribute(String colName) {
        String methodName="get"+colName.substring(0,1).toUpperCase()+colName.substring(1,colName.length());
        try{
            Method method=getClass().getMethod(methodName,new Class[0]);
            return  method.invoke(this,new Object[] {});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
