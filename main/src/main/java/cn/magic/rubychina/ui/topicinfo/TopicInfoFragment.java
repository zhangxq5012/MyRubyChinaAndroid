package cn.magic.rubychina.ui.topicinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.magic.rubychina.adapter.AbstractObjectAdapter;
import cn.magic.rubychina.main.R;
import cn.magic.rubychina.ui.LoginActivity;
import cn.magic.rubychina.ui.itf.IBackPressed;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.StringCharsetRequest;
import cn.magic.rubychina.util.UserUtils;
import cn.magic.rubychina.vo.Topic;
import cn.magic.rubychina.vo.TopicReply;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopicInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopicInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicInfoFragment extends Fragment implements IBackPressed {
    public static final String MENUREPLYLIST = "回复列表";

    private static final String TOPICID = "TOPICID";
    private Topic topicInfo;
    private String topicID;
    boolean repliesState = false;

    //界面组件
    @InjectView(R.id.avatarView) NetworkImageView imageView;
    @InjectView(R.id.m_login)TextView loginText;
    @InjectView(R.id.replies_count) TextView repliesCount;
    @InjectView(R.id.title)TextView title;
    @InjectView(R.id.node_name)TextView nodeName;
    @InjectView(R.id.last_reply_user_login)TextView lastReplyUserLogin;
    @InjectView(R.id.replied_at)TextView repliedAt;
    @InjectView(R.id.topic_body)WebView webbody;
    @InjectView(R.id.topic_header)View magictet;
    @InjectView(R.id.replies_list) ListView repliesList;
    @InjectView(R.id.reply_infos)RelativeLayout relativeLayout;
    @InjectView(R.id.my_reply)EditText replyEdit;
    @InjectView(R.id.send_reply) Button boSendReply;


    private AbstractObjectAdapter repliesAdapter;
    private List<TopicReply> replies;
    public static final String[] FROM = new String[]{Topic.AVATAR_URL, Topic.LOGIN, TopicReply.UPDATED_AT, TopicReply.BODY_HTML};
    public static final int[] TO = {R.id.r_reply_user_avatar, R.id.r_login, R.id.r_replied_at, R.id.r_replied_body_html};

    private OnFragmentInteractionListener mListener;




    private void updateVies() {
        imageView.setImageUrl(topicInfo.getUser().getAvatar_url(), NetWorkUtil.getInstance(getActivity()).getImageLoader());
        loginText.setText(topicInfo.getLogin());
        repliesCount.setText(topicInfo.getReplies_count());
        title.setText(topicInfo.getTitle().toString());
        nodeName.setText(topicInfo.getNode_name().toString());
        lastReplyUserLogin.setText(topicInfo.getLast_reply_user_login());
        repliedAt.setText(topicInfo.getReplied_at());
        String html = topicInfo.getBody_html();
        webbody.loadData(html, "text/html; charset=UTF-8", null);
        repliesAdapter.setAbstractObjects(replies);
    }


    public TopicInfoFragment() {
        // Required empty public constructor
    }

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
        args.putString(TOPICID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicID = getArguments().getString(TOPICID);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        getTopicInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.topic_info, container, false);
        ButterKnife.inject(this, rootView);
        initViewConfig();
        return rootView;
//        return inflater.inflate(R.layout.fragment_topic_info, container, false);
    }

    private void initViewConfig() {
        webbody.setHorizontalScrollbarOverlay(false);
        webbody.getSettings().setDefaultTextEncodingName(NetWorkUtil.CHARSET);
        webbody.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        boSendReply.setOnClickListener(new SendReplyListener());

        repliesAdapter = new AbstractObjectAdapter(getActivity(), R.layout.reply_info_item, replies, FROM, TO);
        repliesList.setAdapter(repliesAdapter);
        repliesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (position + 1) + "楼 @" + replies.get(position).getUser().getLogin();
                replyEdit.setText(text);
                replyEdit.requestFocus();
                replyEdit.setSelection(replyEdit.getText().length());
                InputMethodManager inputManager =
                        (InputMethodManager) replyEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(replyEdit, 0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void getTopicInfo() {
        String url = String.format(NetWorkUtil.TOPIC_INFO, topicID);
        StringRequest request = new StringCharsetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                topicInfo = gson.fromJson(response, Topic.class);
                updateVies();
                Log.e("test", "test2");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "获取文章信息失败，请查看网络！", Toast.LENGTH_SHORT).show();

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
        public void callReplyInfos(Bundle bundle);

        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(MENUREPLYLIST);
        item.setIcon(R.drawable.ic_action_chat);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        Log.i("ACTIONBAR", "TEST");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(MENUREPLYLIST)) {
            if (!repliesState) {
                changeToReplyState(true);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (repliesState) {
            changeToReplyState(false);
            return true;
        }
        return false;
    }

    private void changeToReplyState(boolean flag) {
        repliesState = flag;
        if (flag) {
            webbody.setVisibility(View.GONE);
            magictet.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            webbody.setVisibility(View.VISIBLE);
            magictet.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
    }

    private class SendReplyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (!UserUtils.isLogined()) {
                Intent intent = new Intent(TopicInfoFragment.this.getActivity(), LoginActivity.class);
                startActivity(intent);
                return;
            }

            if (TextUtils.isEmpty(replyEdit.getText())) {
                Toast.makeText(getActivity(), R.string.reply_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            String body = replyEdit.getText().toString();
            String url = String.format(NetWorkUtil.TOPIC_REPLY, topicID);

//            String postUrl=NetWorkUtil.appendParam(url,param);
            StringCharsetRequest request = new StringCharsetRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Toast.makeText(TopicInfoFragment.this.getActivity(), R.string.reply_success, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    TopicReply rt = gson.fromJson(response, TopicReply.class);
                    replies.add(rt);
                    replyEdit.setText("");
                    replyEdit.setSelected(false);
                    replyEdit.clearFocus();
                    // 隐藏输入法
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    // 显示或者隐藏输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String parsed = new String(error.networkResponse.data, "utf-8");
                        Toast.makeText(TopicInfoFragment.this.getActivity(), parsed, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
            });

            Map<String, String> param = new HashMap<String, String>();
            param.put("body", body);
            UserUtils.putToken(param);
            request.setParam(param);
            NetWorkUtil.getInstance().getRequestQueue().add(request);

        }
    }

}
