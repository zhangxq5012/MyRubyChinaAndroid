package cn.magic.rubychina.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.magic.rubychina.main.R;
import cn.magic.rubychina.ui.topicinfo.TopicInfoFragment;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.util.UserUtils;
import cn.magic.rubychina.vo.Node;
import cn.magic.rubychina.vo.Topic;

public class NewTopicActivity extends Activity {
    public static final int SENDSUCCESS=2;
    public static final String TOPICID="topic_id";



    @InjectView(R.id.node_group)
    Spinner nodeGroupSpinner;
    @InjectView(R.id.node_name)
    Spinner nodeNameSpinner;
    @InjectView(R.id.new_topic_title)
    EditText  newTopicTitle;
    @InjectView(R.id.new_topic_content)
    EditText  newTopicContent;

    private ArrayAdapter nodeNameAdapter;
    private String[] nodeNames={};


    private HashMap<String,String> param=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        ButterKnife.inject(this);
        initSpinner();

    }

    private void initSpinner() {
        nodeGroupSpinner.setSelection(0);
        nodeNameAdapter =new ArrayAdapter(this,android.R.layout.simple_spinner_item, Node.nodes[0]);
        nodeNameSpinner.setAdapter(nodeNameAdapter);
        nodeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nodeGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nodeNameAdapter =new ArrayAdapter(NewTopicActivity.this,android.R.layout.simple_spinner_item, Node.nodes[position]);
                nodeNameSpinner.setAdapter(nodeNameAdapter);
                nodeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.send_newtopic) {
            send();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void send() {
        if(!UserUtils.isLogined()){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            return;
        }

        if(TextUtils.isEmpty(newTopicTitle.getText())||TextUtils.isEmpty(newTopicContent.getText())){
            Toast.makeText(NewTopicActivity.this,R.string.not_allow_null,Toast.LENGTH_LONG).show();
            return;
        }

        StringCharsetRequest request=new StringCharsetRequest(Request.Method.POST, NetWorkUtil.TOPIC_NEW,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NewTopicActivity.this, R.string.send_success, Toast.LENGTH_SHORT).show();
                Gson gson=new Gson();
                Topic topic = gson.fromJson(response, Topic.class);
                Intent intent = new Intent(NewTopicActivity.this, MainActivity.class);
                intent.putExtra("topic_id", topic.id);
                setResult(SENDSUCCESS,intent);
                finish();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String parsed = null;
                try {
                    parsed = new String(error.networkResponse.data, "utf-8");
                    Toast.makeText(NewTopicActivity.this, parsed, Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        nodeGroupSpinner.getSelectedItemId();


        String nodeid= Node.nodes_id[nodeGroupSpinner.getSelectedItemPosition()][nodeNameSpinner.getSelectedItemPosition()];
//        String nodeInfo=nodeGroupSpinner.getSelectedItem().toString()+nodeNameSpinner.getSelectedItem().toString();
        String title=newTopicTitle.getText().toString();
        String content=newTopicContent.getText().toString();

        Map<String, String> param=new HashMap<String, String>();
        param.put("node_id",nodeid);
        param.put("title",title);
        param.put("body",content);
        UserUtils.putToken(param);
        request.setParam(param);
        NetWorkUtil.getInstance().getRequestQueue().add(request);
    }


}
