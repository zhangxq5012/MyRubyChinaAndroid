package cn.magic.rubychina.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.magic.rubychina.adapter.NodePagerAdapter;
import cn.magic.rubychina.dao.NodeUtil;
import cn.magic.rubychina.main.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class NodePagerFragment extends Fragment {

    @InjectView(R.id.node_viewpager)
    ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public static NodePagerFragment newInstance() {
        NodePagerFragment fragment = new NodePagerFragment();
        return fragment;
    }

    public NodePagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_node_pager, container, false);
        ButterKnife.inject(this, rootView);
        String[] sections = NodeUtil.getAllSectionName();
        mPager.setAdapter(new NodePagerAdapter(getFragmentManager(), sections));
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
