package cn.magic.rubychina.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.magic.rubychina.dao.NodeUtil;
import cn.magic.rubychina.main.R;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.util.UserUtils;
import cn.magic.rubychina.vo.Node;
import cn.magic.rubychina.vo.Topic;

public class NewTopicActivity extends Activity {
    public static final int SENDSUCCESS=2;
    public static final String TOPICID="topic_id";
    private String nodeid;


    String[] from={Node.SECTION_NAME};
    int[] to ={android.R.id.text1};

    LinkedHashMap<String,LinkedHashMap<String,String>> nodeMap=new LinkedHashMap<String,LinkedHashMap<String,String>>();


    @InjectView(R.id.node_group)
    Spinner nodeGroupSpinner;
    @InjectView(R.id.node_name)
    Spinner nodeNameSpinner;
    @InjectView(R.id.new_topic_title)
    EditText  newTopicTitle;
    @InjectView(R.id.new_topic_content)
    EditText  newTopicContent;

    private ArrayAdapter nodeGroupAdapter;
    private ArrayAdapter nodeNameAdapter;
    private String[] nodeNames={};
    Cursor nameCursor;


    private HashMap<String,String> param=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        ButterKnife.inject(this);
        new NodeAsyncTask().execute();

    }

    private class NodeAsyncTask extends AsyncTask<Void,Void,String[]>{


        @Override
        protected String[] doInBackground(Void... params) {
            String[] sections= NodeUtil.getAllSectionName();
            List<Node> nodes=NodeUtil.getAllNodes();
            if(nodes!=null&&nodes.size()>0){
                for(Node node:nodes){
                    if(nodeMap.get(node.section_name)!=null){
                        nodeMap.get(node.section_name).put(node.name,node.id);
                    }else{
                        LinkedHashMap<String,String> map=new LinkedHashMap<String, String>();
                        map.put(node.name,node.id);
                        nodeMap.put(node.section_name,map);
                    }

                }
            }
            return nodeMap.keySet().toArray(new String[0]);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            initSpinner(strings);
        }
    }

    private void initSpinner(String[] sections) {
        nodeGroupAdapter= new ArrayAdapter(NewTopicActivity.this,android.R.layout.simple_spinner_item, sections);
        nodeGroupSpinner.setAdapter(nodeGroupAdapter);



//        nodeGroupSpinner.setSelection(0);
//        nodeNameAdapter =new ArrayAdapter(this,android.R.layout.simple_spinner_item, Node.nodes[0]);
//        nodeNameSpinner.setAdapter(nodeNameAdapter);
//        nodeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nodeGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sectionName=parent.getSelectedItem().toString();
                nodeNameAdapter =new ArrayAdapter(NewTopicActivity.this,android.R.layout.simple_spinner_item,nodeMap.get(sectionName).keySet().toArray());
                nodeNameSpinner.setAdapter(nodeNameAdapter);
                nodeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        nodeNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nodeid=nodeMap.get(nodeGroupSpinner.getSelectedItem().toString()).get(parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nodeGroupSpinner.setSelection(0);
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
            onSend();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onSend() {
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
