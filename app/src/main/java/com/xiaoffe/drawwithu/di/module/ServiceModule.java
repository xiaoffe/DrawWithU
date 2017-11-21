package com.xiaoffe.drawwithu.di.module;

import android.app.IntentService;
import com.xiaoffe.drawwithu.di.scope.ServiceScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by codeest on 16/8/7.
 */

@Module
public class ServiceModule {
    private IntentService service;

    public ServiceModule(IntentService service) {
        this.service = service;
    }

    @Provides
    @ServiceScope
    public IntentService provideService() {
        return service;
    }
}
