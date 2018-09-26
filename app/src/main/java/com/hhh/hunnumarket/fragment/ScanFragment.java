package com.hhh.hunnumarket.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hhh.hunnumarket.R;

import java.util.ArrayList;
import java.util.List;


public class ScanFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageButton btn_search;
    private ImageButton btn_post;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> indicators;
    private List<Fragment> fragments;

    public ScanFragment() {
    }

    private void initUI(View view) {/*
        toolbar = view.findViewById(R.id.scan_toolbar);
        btn_search = view.findViewById(R.id.scan_btn_search);
        btn_post = view.findViewById(R.id.scan_btn_post);*/
        tabLayout = view.findViewById(R.id.scan_tab_layout);
        viewPager = view.findViewById(R.id.scan_view_pager);
    }

    private void initListener() {
//        btn_search.setOnClickListener(this);
//        btn_post.setOnClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
        initUI(view);
        initListener();
        initData();
        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


    private void initData() {
        indicators = new ArrayList<>();
        fragments = new ArrayList<>();
        indicators.add(getString(R.string.info_offer));
        fragments.add(new OfferFragment());
        indicators.add(getString(R.string.info_demand));
        fragments.add(new DemandFragment());
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return indicators.get(position);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.scan_btn_search:
                Toast.makeText(getActivity(), "搜索", Toast.LENGTH_SHORT).show();
                break;

            case R.id.scan_btn_post:
                PopupMenu pm = new PopupMenu(getActivity(), v);
                pm.getMenuInflater().inflate(R.menu.menu_scan_pop_post, pm.getMenu());
                pm.show();
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.scan_pop_sell:
                                startActivity(new Intent(getContext(), PostItemActivity.class));
                                return true;
                            case R.id.scan_pop_need:
                                startActivity(new Intent(getContext(), PostNeedActivity.class));
                                return true;
                        }
                        return false;
                    }
                });
                break;*/


        }
    }
}
