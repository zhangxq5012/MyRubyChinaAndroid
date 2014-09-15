package cn.magic.rubychina.vo;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by magic on 2014/9/3.
 */
@Table(name = "node",id = BaseColumns._ID)
public class Node extends Model{



    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String topics_count;
    @Column
    public String summary;
    @Column
    public String section_id;
    @Column
    public String sort;
    @Column
    public String section_name;

    public static String SECTION_NAME="section_name";
    public static String SECTION_ID="section_id";
    public static String SORT="sort";
    public static String SUMMARY="summary";
    public static final String NAME ="name" ;


    public static final String nodes_id[][] = {
            {"52","2","3","44","1","29","37","43","45","47","54",},
            {"26","27","24","63","64","30","38","42","50","56","62","25","28",},
            {"10","11","12","17","18","61","39","40","41","46","53","55","60","20","9",},
            {"32","33",},
            {"4","5","34","35","48","58","59",},
            {"21","22","23",},
            {"31","51","57",}
    };

    public static final String nodes[][] = {
            {"新手问题","Rails","Gem","部署","Ruby","重构","Testing","Sinatra","RVM","开源项目","JRuby"},
            {"分享","瞎扯淡","工具控","健康","求职","产品控","书籍","Mac","插画","创业","移民","招聘","其他"},
            {"Redis","Git","Database","Linux","Nginx","NoPoint","搜索分词","算法","CSS","Mailer","数学","运维","Web 安全","云服务","MongoDB"},
            {"iPhone","Android"},
            {"Python","JavaScript","Go","Erlang","Cocoa","Clojure","Haskell"},
            {"公告","反馈","社区开发"},
            {"RubyTuesday","RubyConf","线下活动"},
            {"未选择"}
    };
}
