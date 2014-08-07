package cn.magic.rubychina.vo;

/**
 * Created by magic on 2014/8/3.
 */
public class User extends AbstractObject {
    public String login;
    public String id;
    public String avatar_url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
