package cn.magic.rubychina.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by magic on 2014/7/26.
 */
public class Topic extends AbstractObject   {
    private static final DateFormat LAST_REPLY_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final String LAST_REPLY_TEMPLATE = "于%s前回复";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getReplied_at() {
        return getShowTime(replied_at);
//        String reply;
//        if (replied_at == null) {
//            reply = "还没有回复，快进去讨论吧！";
//        } else {
//            reply = String.format(LAST_REPLY_TEMPLATE, getShowTimeString(replied_at, LAST_REPLY_FORMAT));
//        }
//        return reply;
    }

    public void setReplied_at(String replied_at) {
        this.replied_at = replied_at;
    }

    public String getReplies_count() {
        return replies_count;
    }

    public void setReplies_count(String replies_count) {
        this.replies_count = replies_count;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getLast_reply_user_id() {
        return last_reply_user_id;
    }

    public void setLast_reply_user_id(String last_reply_user_id) {
        this.last_reply_user_id = last_reply_user_id;
    }

    public String getLast_reply_user_login() {
        return last_reply_user_login;
    }

    public void setLast_reply_user_login(String last_reply_user_login) {
        this.last_reply_user_login = last_reply_user_login;
    }

    public String getLogin() {
        return user.getLogin();
    }

    public void setLogin(String login) {
        user.setLogin(login);
    }

    public String getAvatar_url() {
        return user.getAvatar_url();
    }

    public void setAvatar_url(String avatar_url) {
        user.setAvatar_url(avatar_url);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getBody_html() {
        return body_html;
    }

    public void setBody_html(String body_html) {
        this.body_html = body_html;
    }


    public static final int DAYINT = 24 * 3600;
    public static final int HOURINT = 3600;
    public static final int MINUTE = 60;


    public static String getShowTimeString(String showTime, DateFormat dateFormat) {
        Date date = new Date();
        Date nowTime = new Date();
        try {
            date = dateFormat.parse(showTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 获得时间差的秒数
        long between = Math.abs((nowTime.getTime() - date.getTime()) / 1000);

        long day = between / DAYINT;

        long hour = between % DAYINT / HOURINT;

        long minute = between % HOURINT / MINUTE;

        if (day > 0) {
            return day + "天前";
        } else if (hour > 0) {
            return hour + "小时前";
        } else if (minute > 0) {
            return minute + "分钟前";
        } else {
            return between + "秒前";
        }
    }

    public static String getShowTime(String date){
        String returnDate;
        if (date == null) {
            returnDate = "还没有回复，快进去讨论吧！";
        } else {
            returnDate = String.format(LAST_REPLY_TEMPLATE, getShowTimeString(date, LAST_REPLY_FORMAT));
        }
        return returnDate;
    }



    public User user;
    public String avatar_url;
    public String login;
    public String id;
    public String title;
    public String created_at;
    public String replied_at;
    public String replies_count;
    public String node_name;
    public String node_id;
    public String last_reply_user_id;
    public String last_reply_user_login;

    public static final String AVATAR_URL = "avatar_url";
    public static final String LOGIN = "login";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CREATED_AT = "created_at";
    public static final String REPLIED_AT = "replied_at";
    public static final String REPLIES_COUNT = "replies_count";
    public static final String NODE_NAME = "node_name";
    public static final String NODE_ID = "node_id";
    public static final String LAST_REPLY_USER_ID = "last_reply_user_id";
    public static final String LAST_REPLY_USER_LOGIN = "last_reply_user_login";
    public String body_html;


    public List<TopicReply> getReplies() {
        return replies;
    }

    public void setReplies(List<TopicReply> replies) {
        this.replies = replies;
    }

    public List<TopicReply> replies;

}
