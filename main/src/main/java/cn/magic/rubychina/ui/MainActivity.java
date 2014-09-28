package cn.magic.rubychina.ui;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import java.util.Stack;

import cn.magic.rubychina.main.R;
import cn.magic.rubychina.ui.itf.IBackPressed;
import cn.magic.rubychina.ui.itf.OnFragmentInteractionListener;
import cn.magic.rubychina.ui.topicinfo.TopicInfoFragment;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.util.UserUtils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TopicsFragment.OnTopicSelectedListener
        , TopicInfoFragment.OnFragmentInteractionListener, OnFragmentInteractionListener {

    public static final int LOGINREQUEST = 256;
    public static final int SENDTOPICREQUEST = 256;


    String TAG = "MainActivity";

    Fragment[] fragments;

    IBackPressed currentFrag;

    Stack<Fragment> stack=new Stack<Fragment>();


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Debug.startMethodTracing();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //打开文章列表界面
        onNavigationDrawerItemSelected(0);

    }

    @Override
    protected void onStop() {
        Debug.stopMethodTracing();
        super.onStop();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0) {
            fragment = TopicsFragment.newInstance(NetWorkUtil.TOPICS);
        } else if (position == 1) {
            fragment = NodePagerFragment.newInstance();
        }
        replaceFragment(fragment, TOPIC_TAG);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_hotdiscuss);
                break;
            case 2:
                mTitle = getString(R.string.title_nodeclassify);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            if (UserUtils.isLogined()) {
                menu.findItem(R.id.m_login).setTitle("退出");
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m_login) {
            if (UserUtils.isLogined()) {
                UserUtils.clearUser();
                invalidateOptionsMenu();
                Toast.makeText(this, getString(R.string.exitSuccess), Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGINREQUEST);
                return true;
            }
        }
        if (id == R.id.post_topic) {
            Intent intent = new Intent(this, NewTopicActivity.class);
            startActivityForResult(intent, SENDTOPICREQUEST);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTopicReplies() {

    }

    private static final String TOPIC_TAG = "topicTag";
    private static final String INFO_TAG = "topicInfo";

    public void onTopicSelect(String topicID) {
        TopicInfoFragment topicInfoFragment = TopicInfoFragment.newInstance(topicID);
        replaceFragment(topicInfoFragment, TOPIC_TAG);
        Log.e(TAG, "testTopicSelected" + topicID);
        currentFrag = topicInfoFragment;
    }

    private void replaceFragment(final Fragment fragment, String tag) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(stack.size()>0){
            transaction.hide(stack.peek());
        }

        if(!stack.contains(fragment)){
            transaction.add(R.id.container,fragment);
            stack.add(fragment);
        }
        transaction.addToBackStack(null).commit();

    }


    @Override
    public void onBackPressed() {
        if (currentFrag != null) {
            if (currentFrag.onBackPressed()) {
                return;
            }
        }
        if (stack.size()==1){
            onStop();
        }
        stack.pop();
        getSupportFragmentManager().beginTransaction().show(stack.peek()).commit();
        super.onBackPressed();
    }

    @Override
    public void callReplyInfos(Bundle bundle) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGINREQUEST) {
            if (resultCode == LoginActivity.LOGINSUCCESS) {
                invalidateOptionsMenu();//更新菜单项
            }
        }
        if (requestCode == SENDTOPICREQUEST) {
            if (resultCode == NewTopicActivity.SENDSUCCESS) {
                invalidateOptionsMenu();//更新菜单项
                String topicID = data.getStringExtra(NewTopicActivity.TOPICID);
                onTopicSelect(topicID);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        String source = bundle.getString(OnFragmentInteractionListener.SOURCE);
        if (source == NodeFragment.class.getName()) {//节点分类调用
            String nodeID = bundle.getString(OnFragmentInteractionListener.ARGS);
            String nodeUrl = String.format(NetWorkUtil.NODE_URL, nodeID);
            replaceFragment(TopicsFragment.newInstance(nodeUrl), INFO_TAG);

        }

    }
}
