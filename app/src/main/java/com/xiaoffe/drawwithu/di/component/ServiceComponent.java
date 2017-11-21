package com.xiaoffe.drawwithu.di.component;

import android.app.IntentService;
import com.xiaoffe.drawwithu.di.module.ServiceModule;
import com.xiaoffe.drawwithu.di.scope.ServiceScope;
import com.xiaoffe.drawwithu.service.DownFileService;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */
@ServiceScope
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    IntentService getService();

    void inject(DownFileService downFileService);

}
