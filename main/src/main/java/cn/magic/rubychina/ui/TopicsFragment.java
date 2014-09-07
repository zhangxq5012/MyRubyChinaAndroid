package cn.magic.rubychina.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.magic.rubychina.adapter.AbstractObjectAdapter;
import cn.magic.rubychina.main.R;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.vo.Topic;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopicsFragment extends Fragment {
    OnTopicSelectedListener mCallback;
    private static TopicsFragment fragment;

    public static final String PER_PAGE = "15";

    public static final String[] FROM = new String[]{Topic.AVATAR_URL, Topic.LOGIN, Topic.REPLIES_COUNT,
            Topic.TITLE, Topic.NODE_NAME, Topic.LAST_REPLY_USER_LOGIN, Topic.REPLIED_AT};
    public static final int[] TO = {R.id.avatarView, R.id.m_login, R.id.replies_count, R.id.title
            , R.id.node_name, R.id.last_reply_user_login, R.id.replied_at};
    AbstractObjectAdapter adapter;
    PullToRefreshListView listView;
    ArrayList topicList;
    int page = 1;


    private Type listType = new TypeToken<List<Topic>>() {
    }.getType();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TopicsFragment newInstance() {
        if(fragment==null){
            fragment=new TopicsFragment();
        }
        return fragment;
    }

    public TopicsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        listView = (PullToRefreshListView) rootView.findViewById(R.id.pull_to_refresh_listview);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                new GetDataTask().execute();
            }
        });
        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadNextPage();

            }
        });
        adapter = new AbstractObjectAdapter(getActivity(), R.layout.topic_header_item, topicList, FROM, TO);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new TopicItemClickListener());
        loadFirstPage();
        return rootView;
    }

    private class TopicItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String topicID=((Topic) adapter.getItem(position-1)).getId();
            mCallback.onTopicSelect(topicID);

        }
    }

    public interface OnTopicSelectedListener {
        public void onTopicSelect(String topicID);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            loadFirstPage();
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            listView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnTopicSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void loadFirstPage() {
        loadTopics(1);
    }

    public void loadNextPage() {
        page = page + 1;
        loadTopics(page);
    }

    public void loadTopics(final int page) {
        ArrayList<Topic> topics;
        Map<String, String> param = new HashMap<String, String>();
        param.put("page", page + "");
        param.put("per_page", PER_PAGE);

        String url = NetWorkUtil.getInstance(getActivity()).appendParam(NetWorkUtil.TOPICS, param);

        StringRequest request = new StringCharsetRequest(url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ArrayList<Topic> topics = gson.fromJson(response, listType);
                String title = (String) topics.get(0).getAttribute("title");

                if (page == 1 || topicList == null) {
                    topicList = topics;
                } else {
                    topicList.addAll(topics);
                }

                adapter.setAbstractObjects(topicList);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "获取新文章失败，请查看网络！", Toast.LENGTH_SHORT).show();
                    }
                }
        ,NetWorkUtil.CHARSET
        ) ;

        NetWorkUtil.getInstance(getActivity()).getRequestQueue().add(request);
    }


}