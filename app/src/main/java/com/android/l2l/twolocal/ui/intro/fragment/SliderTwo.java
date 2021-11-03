package com.android.l2l.twolocal.ui.intro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.ui.base.BaseFragment;

public class SliderTwo extends Fragment {

    public static SliderTwo newInstance() {
        return new SliderTwo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_two, container, false);
        return view;
    }

}
