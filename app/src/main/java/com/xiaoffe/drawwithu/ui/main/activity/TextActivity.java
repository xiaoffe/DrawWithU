package com.xiaoffe.drawwithu.ui.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;

import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.widget.CoordinatorTabLayout;

/**
 * Created by Administrator on 2017/9/1.
 */

public class TextActivity extends AppCompatActivity {
    CoordinatorTabLayout mCoordinatorTabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_online);
        mCoordinatorTabLayout = (CoordinatorTabLayout)findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTitle("teachMe")
                .setBackEnable(true)
                .setContentScrimColorArray(new int[]{android.R.color.holo_blue_light})
                .setLoadHeaderImagesListener(null);
    }
}