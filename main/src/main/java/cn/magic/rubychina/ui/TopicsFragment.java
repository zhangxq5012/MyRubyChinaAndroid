package cn.magic.rubychina.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.magic.rubychina.adapter.ExpandCursorAdapter;
import cn.magic.rubychina.main.R;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.vo.Topic;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopicsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public static int TOPIC_LOADER = 1;

    OnTopicSelectedListener mCallback;
    private static TopicsFragment fragment;

    public static final String PER_PAGE = "15";

    public static final String[] from = new String[]{Topic.AVATAR_URL, Topic.LOGIN, Topic.REPLIES_COUNT,
            Topic.TITLE, Topic.NODE_NAME, Topic.LAST_REPLY_USER_LOGIN, Topic.REPLIED_AT};
    public static final int[] to = {R.id.avatarView, R.id.m_login, R.id.replies_count, R.id.title
            , R.id.node_name, R.id.last_reply_user_login, R.id.replied_at};
//    AbstractObjectAdapter adapter;

    @InjectView(R.id.topic_listview)
    ListView listView;
    @InjectView(R.id.swipRefreshContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    ExpandCursorAdapter mAdapter;
    View rootView;

    ArrayList topicList;
    int page = 1;


    private Type listType = new TypeToken<List<Topic>>() {
    }.getType();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TopicsFragment newInstance() {
        if (fragment == null) {
            fragment = new TopicsFragment();
        }
        return fragment;
    }

    public TopicsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引

            //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItemIndex == mAdapter.getCount() - 1) {
                    loadNextPage();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });
//        adapter = new AbstractObjectAdapter(getActivity(), R.layout.topic_header_item, topicList, from, to);
        mAdapter = new ExpandCursorAdapter(getActivity(), R.layout.topic_header_item, null, from, to);
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new TopicItemClickListener());

        initAnimAdapter();

        getLoaderManager().initLoader(TOPIC_LOADER, null, this);

        loadFirstPage();


        return rootView;
    }


    /**
     * 给listview添加动画效果
     */
    private void initAnimAdapter() {
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContentProvider.createUri(Topic.class, null), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);

    }


    private class TopicItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            String topicID = ((Topic) mAdapter.getItem(position - 1)).id;
            String topicID = mAdapter.getID(position);
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
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();
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

                topicList = new ArrayList();
                if (page == 1) {
                    new Delete().from(Topic.class).execute();//清空topic表
                }
                insertList(topics);
                Log.i("topicInsert", new Select().from(Topic.class).execute().size() + "");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "获取新文章失败，请查看网络！", Toast.LENGTH_SHORT).show();
                    }
                }
                , NetWorkUtil.CHARSET
        );

        NetWorkUtil.getInstance(getActivity()).getRequestQueue().add(request);
    }

    public void insertList(List list) {
        ActiveAndroid.beginTransaction();
        try {
            if (list == null || list.size() == 0) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                Topic model = (Topic) list.get(i);

                addTopicInfo(model);

                if (topicList.contains(model)) {
                    continue;
                }
                model.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    private void addTopicInfo(Topic topic) {
        topic.avatar_url = topic.getUser().getAvatar_url();
        topic.login = topic.getUser().getLogin();
        topic.replied_at = topic.getReplied_at();

    }


}