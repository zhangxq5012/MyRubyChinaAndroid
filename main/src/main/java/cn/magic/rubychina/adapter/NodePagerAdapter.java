package cn.magic.rubychina.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.magic.rubychina.ui.NodeFragment;
import cn.magic.rubychina.vo.Node;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class NodePagerAdapter extends FragmentStatePagerAdapter {
    private String[] sections;

    public NodePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public NodePagerAdapter(FragmentManager fm,String[] sections) {
        super(fm);
        this.sections=sections;
    }



    @Override
    public Fragment getItem(int position) {
        return NodeFragment.newInstance(sections[position]);
    }

    @Override
    public int getCount() {
        return sections.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sections[position];
    }
}