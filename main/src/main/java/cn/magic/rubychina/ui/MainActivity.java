package cn.magic.rubychina.ui;

import android.content.Intent;
import android.net.Uri;
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

import cn.magic.rubychina.main.R;
import cn.magic.rubychina.ui.itf.IBackPressed;
import cn.magic.rubychina.ui.topicinfo.TopicInfoFragment;
import cn.magic.rubychina.util.UserUtils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,TopicsFragment.OnTopicSelectedListener
,TopicInfoFragment.OnFragmentInteractionListener{

    public static final  int LOGINREQUEST=256;
    public static final  int SENDTOPICREQUEST=256;


    String TAG="MainActivity";

    Fragment[] fragments;

    IBackPressed currentFrag;

    Fragment currentFragment;


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
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment=null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(position==0){
            fragment=TopicsFragment.newInstance();
        }
        if(fragment!=null){
            fragmentManager.beginTransaction()
                    .replace(R.id.container,fragment).addToBackStack(null)
                    .commit();
        }
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
            if (UserUtils.isLogined()){
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
            if(UserUtils.isLogined()){
                UserUtils.clearUser();
                invalidateOptionsMenu();
                Toast.makeText(this,getString(R.string.exitSuccess),Toast.LENGTH_LONG).show();
            }else{
                Intent intent=new Intent(this,LoginActivity.class);
                startActivityForResult(intent, LOGINREQUEST);
                return true;
            }
        }
        if(id==R.id.post_topic){
            Intent intent=new Intent(this,NewTopicActivity.class);
            startActivityForResult(intent,SENDTOPICREQUEST);
            overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTopicReplies(){

    }
    public void onTopicSelect(String topicID){
        TopicInfoFragment topicInfoFragment=TopicInfoFragment.newInstance(topicID);
        replaceFragment(topicInfoFragment );
        Log.e(TAG,"testTopicSelected"+topicID);
        currentFrag=topicInfoFragment;
    }

    private void replaceFragment(Fragment topicInfoFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.container,
                topicInfoFragment).addToBackStack(null).commit();

    }


    @Override
    public void onBackPressed() {
        if(currentFrag!=null){
            if(currentFrag.onBackPressed()){
                return;
            };
        }
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
        if(requestCode==LOGINREQUEST){
            if(resultCode==LoginActivity.LOGINSUCCESS){
                invalidateOptionsMenu();//更新菜单项
            }
        }
        if(requestCode==SENDTOPICREQUEST){
            if(resultCode==NewTopicActivity.SENDSUCCESS){
                invalidateOptionsMenu();//更新菜单项
                String topicID=data.getStringExtra(NewTopicActivity.TOPICID);
                onTopicSelect(topicID);
            }
        }
    }
}
