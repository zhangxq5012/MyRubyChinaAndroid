package cn.magic.rubychina.vo;

import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by magic on 2014/8/3.
 */
@Table(name = "user",id = BaseColumns._ID)
public class User extends AbstractObject {
    @Column
    public String login;
    @Column(unique = true,name = "user_id")
    public String id;
    @Column
    public String avatar_url;
    @Column
    public String email;
    @Column
    public String private_token;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

//    public String getId() {
//        return id;
//    }

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
