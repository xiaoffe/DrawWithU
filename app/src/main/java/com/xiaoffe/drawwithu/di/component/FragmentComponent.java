package com.xiaoffe.drawwithu.di.component;

import android.app.Activity;

import com.xiaoffe.drawwithu.base.contract.fragment.MyProfileContract;
import com.xiaoffe.drawwithu.di.module.FragmentModule;
import com.xiaoffe.drawwithu.di.scope.FragmentScope;
import com.xiaoffe.drawwithu.ui.main.fragment.MyProfileFragment;
import com.xiaoffe.drawwithu.ui.main.fragment.OnlinesFragment;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();
    void inject(OnlinesFragment onlinesFragment);
    void inject(MyProfileFragment myProfileFragment);
}
