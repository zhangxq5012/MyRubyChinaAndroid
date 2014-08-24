package cn.magic.rubychina.ui;

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

import cn.magic.rubychina.main.R;
import cn.magic.rubychina.ui.itf.IBackPressed;
import cn.magic.rubychina.ui.topicinfo.TopicInfoFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,TopicsFragment.OnTopicSelectedListener
,TopicInfoFragment.OnFragmentInteractionListener{

    String TAG="MainActivity";

    Fragment[] fragments;

    IBackPressed currentFrag;


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
        initFragments();
        onNavigationDrawerItemSelected(0);

    }

    private void initFragments() {
        fragments=new Fragment[2];
        fragments[0]= TopicsFragment.newInstance();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragments!=null){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragments[position]).addToBackStack(null)
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
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void showTopicReplies(){

    }
    public void onTopicSelect(String topicID){
        replaceFragment( TopicInfoFragment.newInstance(topicID));
        Log.e(TAG,"testTopicSelected"+topicID);
    }

    private void replaceFragment(TopicInfoFragment topicInfoFragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.container,
                topicInfoFragment).addToBackStack(null).commit();
        currentFrag=topicInfoFragment;
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
}