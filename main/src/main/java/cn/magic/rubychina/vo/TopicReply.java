package cn.magic.rubychina.vo;

import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by magic on 2014/8/12.
 * Topic回复详细信息
 */
@Table(name = "topic_reply",id = BaseColumns._ID)
public class TopicReply extends AbstractObject{
    public static final String BODY_HTML = "body_html";
    public static final String UPDATED_AT = "updated_at";

    @Column(unique = true,name = "topic_reply_id")
    public String id;
    @Column
    public String body;
    @Column
    public String created_at;
    @Column
    public String updated_at;
    @Column
    public String deleted_at;

    @Column
    public Topic topic;
    @Column
    public String topic_id;


    @Column
    public String body_html;
    @Column
    User user;

    @Column
    public String avatar_url;
    @Column
    public String login;


    public String getLogin() {
        return user.getLogin();
    }
    public void setLogin() {
        login= getLogin();
    }

    public String getAvatar_url() {
        return user.getAvatar_url();
    }
    public void setAvatar_url() {
        avatar_url= user.getAvatar_url();
    }

//    public String getId() {
//        return id;
//    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return Topic.getShowTime(updated_at);
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getBody_html() {
        return body_html;
    }

    public void setBody_html(String body_html) {
        this.body_html = body_html;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void addUserInfo() {
        setAvatar_url();
        setLogin();
    }
}
