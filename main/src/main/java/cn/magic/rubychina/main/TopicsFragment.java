package cn.magic.rubychina.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.magic.rubychina.adapter.AbstractObjectAdapter;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.vo.Topic;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopicsFragment extends Fragment {

    public static final String[] FROM=new String[]{Topic.AVATAR_URL,Topic.LOGIN,Topic.REPLIES_COUNT,
            Topic.TITLE,Topic.NODE_NAME,Topic.LAST_REPLY_USER_LOGIN,Topic.REPLIED_AT};
    public static final int[] TO={R.id.avatarView,R.id.login,R.id.replies_count,R.id.title
    ,R.id.node_name,R.id.last_reply_user_login,R.id.replied_at};
    AbstractObjectAdapter adapter;
    ListView listView;
    private Type listType = new TypeToken<List<Topic>>() {
    }.getType();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TopicsFragment newInstance(int sectionNumber) {
        TopicsFragment fragment = new TopicsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TopicsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.topicList);
        adapter=new AbstractObjectAdapter(getActivity(),R.layout.topic_item,null,FROM,TO);
        listView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList list = loadTopics(1);
    }

    public ArrayList loadTopics(int page) {
        StringRequest request = new StringRequest(NetWorkUtil.TOPICS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ArrayList<Topic> topics = gson.fromJson(response, listType);
                String title = (String) topics.get(0).getAttribute("title");

                Log.e("topic", title);
                System.out.println(title);
                System.out.println("test123");
                adapter.setAbstractObjects(topics);



            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "获取新文章失败！", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        NetWorkUtil.getInstance(getActivity()).getRequestQueue().add(request);
        return null;
    }


}