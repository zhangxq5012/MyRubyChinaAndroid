package cn.magic.rubychina.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.magic.rubychina.main.R;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.util.UserUtils;
import cn.magic.rubychina.vo.Topic;
import cn.magic.rubychina.vo.User;

public class LoginActivity extends Activity {
    public static final int LOGINSUCCESS=1;

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.passwd);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLogin(View view) {
        Map<String, String> param = new HashMap<String, String>();
        if (TextUtils.isEmpty(userName.getText())) {
            Toast.makeText(this, R.string.userNameNotNull, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password.getText())) {
            Toast.makeText(this, R.string.passwdNotNull, Toast.LENGTH_LONG).show();
            return;
        }

        String login = userName.getText().toString();
        String passwd = password.getText().toString();
        param.put("user[login]", login + "");
        param.put("user[password]", passwd);
        String url = NetWorkUtil.appendParam(NetWorkUtil.SIGN_IN, param);
        StringRequest request = new StringCharsetRequest(Request.Method.POST,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User u = gson.fromJson(response, User.class);
                UserUtils.saveUserLogin(u.login);
                UserUtils.saveUserToken(u.private_token);
                UserUtils.saveUserEmail(u.email);
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                setResult(LOGINSUCCESS);
                onBackPressed();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String parsed = new String(error.networkResponse.data,"utf-8");
                            JSONObject json=new JSONObject(parsed);
                            Toast.makeText(LoginActivity.this, json.getString("error"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , NetWorkUtil.CHARSET
        );
        NetWorkUtil.getInstance(this).getRequestQueue().add(request);

    }

}
