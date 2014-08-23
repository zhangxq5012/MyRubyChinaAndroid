package cn.magic.rubychina.ui.topicinfo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

import cn.magic.rubychina.adapter.AbstractObjectAdapter;
import cn.magic.rubychina.main.R;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.vo.Topic;
import cn.magic.rubychina.vo.TopicInfo;
import cn.magic.rubychina.vo.TopicReply;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopicInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopicInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "TOPICID";
    private Topic topicInfo;
    // TODO: Rename and change types of parameters
    private String topicID;

    View rootView;
    public static final String[] FROM = new String[]{Topic.AVATAR_URL, Topic.LOGIN, TopicReply.UPDATED_AT, TopicReply.BODY_HTML};
    public static final int[] TO = {R.id.r_reply_user_avatar, R.id.r_login, R.id.r_replied_at,R.id.r_replied_body_html};

    //界面组件
    NetworkImageView imageView;
    TextView loginText;
    TextView repliesCount;
    TextView title;
    TextView nodeName;
    TextView lastReplyUserLogin;
    TextView repliedAt;
    WebView webbody;
    ListView repliesList;
    AbstractObjectAdapter repliesAdapter;
    List<TopicReply> replies;


    private void initViews() {
        imageView = (NetworkImageView) rootView.findViewById(R.id.avatarView);
        loginText = (TextView) rootView.findViewById(R.id.login);
        repliesCount = (TextView) rootView.findViewById(R.id.replies_count);
        title = (TextView) rootView.findViewById(R.id.title);
        nodeName = (TextView) rootView.findViewById(R.id.node_name);
        lastReplyUserLogin = (TextView) rootView.findViewById(R.id.last_reply_user_login);
        repliedAt = (TextView) rootView.findViewById(R.id.replied_at);


        webbody = (WebView) rootView.findViewById(R.id.topic_body);
        webbody.getSettings().setDefaultTextEncodingName(NetWorkUtil.CHARSET);
        webbody.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        repliesList=(ListView) rootView.findViewById(R.id.replies_list);
        repliesAdapter = new AbstractObjectAdapter(getActivity(), R.layout.reply_item, replies, FROM, TO);

        View topic_info=rootView.findViewById(R.id.topic_info);
        repliesList.addHeaderView(topic_info);

        repliesList.setAdapter(repliesAdapter);

    }

    private void initTopicHead() {
        imageView.setImageUrl(topicInfo.getUser().getAvatar_url(), NetWorkUtil.getInstance(getActivity()).getImageLoader());
        loginText.setText(topicInfo.getLogin());
        repliesCount.setText(topicInfo.getReplies_count());
        title.setText(topicInfo.getTitle().toString());
        nodeName.setText(topicInfo.getNode_name().toString());
        lastReplyUserLogin.setText(topicInfo.getLast_reply_user_login());
        repliedAt.setText(topicInfo.getReplied_at());

        String html = topicInfo.getBody_html();

        webbody.loadData(html, "text/html; charset=UTF-8", null);

        replies=topicInfo.getReplies();
        repliesAdapter.setAbstractObjects(replies);


    }


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TopicInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicInfoFragment newInstance(String param1) {
        TopicInfoFragment fragment = new TopicInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public TopicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicID = getArguments().getString(ARG_PARAM1);
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        initTopic();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_topic_info, container, false);
        initViews();
        return rootView;
//        return inflater.inflate(R.layout.fragment_topic_info, container, false);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void initTopic() {
        String url = String.format(NetWorkUtil.TOPIC_INFO, topicID);
        StringRequest request = new StringCharsetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                topicInfo = gson.fromJson(response, Topic.class);
                initTopicHead();
                Log.e("test", "test2");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "获取文章信息失败，请查看网络！", Toast.LENGTH_SHORT).show();
                Log.e("获取文章信息", error.getMessage());

            }
        }, NetWorkUtil.CHARSET
        );
        NetWorkUtil.getInstance(getActivity()).getRequestQueue().add(request);


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
