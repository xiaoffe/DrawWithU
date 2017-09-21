package com.xiaoffe.drawwithu.di.component;

import android.app.Activity;

import com.xiaoffe.drawwithu.di.module.ActivityModule;
import com.xiaoffe.drawwithu.di.scope.ActivityScope;
import com.xiaoffe.drawwithu.ui.main.activity.LoginActivity;
import com.xiaoffe.drawwithu.ui.main.activity.MainActivity;
import com.xiaoffe.drawwithu.ui.main.activity.RegisterActivity;
import com.xiaoffe.drawwithu.ui.main.activity.SyncPadActivity;
import com.xiaoffe.drawwithu.ui.main.activity.WelcomeActivity;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(WelcomeActivity welcomeActivity);

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity rejesterActivity);

    void inject(SyncPadActivity syncPadActivity);
}
