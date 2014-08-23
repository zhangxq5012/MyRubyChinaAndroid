package cn.magic.rubychina.vo;

/**
 * Created by magic on 2014/8/12.
 * Topic回复详细信息
 */
public class TopicReply extends AbstractObject{
    public static final String BODY_HTML = "body_html";
    public static final String UPDATED_AT = "updated_at";

    public String id;
    public String body;
    public String created_at;
    public String updated_at;
    public String deleted_at;
    public String topic_id;
    public String body_html;
    User user;

    public String getLogin() {
        return user.getLogin();
    }
    public String getAvatar_url() {
        return user.getAvatar_url();
    }

    public String getId() {
        return id;
    }

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
        return updated_at;
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




}
