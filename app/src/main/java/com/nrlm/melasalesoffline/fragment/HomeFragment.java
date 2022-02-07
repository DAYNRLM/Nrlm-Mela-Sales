package com.nrlm.melasalesoffline.fragment;

import com.nrlm.melasalesoffline.R;

public class HomeFragment extends BaseFragment {

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }
    @Override
    public int getFragmentLayout() {
        return R.layout.home_fragment_layout;
    }

    @Override
    public void onFragmentReady() {

    }
}
