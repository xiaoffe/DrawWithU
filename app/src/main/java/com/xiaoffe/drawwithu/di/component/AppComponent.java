package com.xiaoffe.drawwithu.di.component;

import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.di.module.AppModule;
import com.xiaoffe.drawwithu.di.module.HttpModule;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.db.RealmHelper;
import com.xiaoffe.drawwithu.model.http.RetrofitHelper;
import com.xiaoffe.drawwithu.model.prefs.ImplPreferencesHelper;
import com.xiaoffe.drawwithu.util.PixUtil;
import com.xiaoffe.drawwithu.util.TIMUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */
//@Component：@Component用于标注接口，是依赖需求方和依赖提供方之间的桥梁。
// 被Component标注的接口在编译时会生成该接口的实现类
    //然而这里并没有inject方法，很有可能AppComponent是用来给别的Component来提供dependency用的
@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    App getContext();  // 提供App的Context

    DataManager getDataManager(); //数据中心

    RetrofitHelper retrofitHelper();  //提供http的帮助类

    RealmHelper realmHelper();    //提供数据库帮助类

    ImplPreferencesHelper preferencesHelper(); //提供sp帮助类

    TIMUtil getTIMUtil();

    PixUtil getPixUtil();
}
