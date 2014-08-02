package cn.magic.rubychina.vo;

/**
 * Created by magic on 2014/7/26.
 */
public class Topic extends AbstractObject{



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
        return replied_at;
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
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

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

    public static final String AVATAR_URL="avatar_url";
    public static final String LOGIN="login";
    public static final String ID="id";
    public static final String TITLE="title";
    public static final String CREATED_AT="created_at";
    public static final String REPLIED_AT="replied_at";
    public static final String REPLIES_COUNT="replies_count";
    public static final String NODE_NAME="node_name";
    public static final String NODE_ID="node_id";
    public static final String LAST_REPLY_USER_ID="last_reply_user_id";
    public static final String LAST_REPLY_USER_LOGIN="last_reply_user_login";
}
