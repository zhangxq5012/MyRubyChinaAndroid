package cn.magic.rubychina.dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;

import java.util.List;

import cn.magic.rubychina.vo.Topic;

/**
 * Created by magic on 2014/9/14.
 */
public class BaseDao {

    public static void insertList(List list) {
        ActiveAndroid.beginTransaction();
        try {
            if (list == null || list.size() == 0) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                Model model = (Model) list.get(i);
                model.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
