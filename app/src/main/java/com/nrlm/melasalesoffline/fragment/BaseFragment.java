package com.nrlm.melasalesoffline.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(this.getFragmentLayout(), container, false);
        ButterKnife.bind(this, v);
        this.onFragmentReady();
        return v;
    }

    abstract public int getFragmentLayout();

    abstract public void onFragmentReady();

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
