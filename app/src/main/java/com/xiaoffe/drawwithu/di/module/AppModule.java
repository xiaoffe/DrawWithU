package com.xiaoffe.drawwithu.di.module;

import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.db.DBHelper;
import com.xiaoffe.drawwithu.model.db.RealmHelper;
import com.xiaoffe.drawwithu.model.http.HttpHelper;
import com.xiaoffe.drawwithu.model.http.RetrofitHelper;
import com.xiaoffe.drawwithu.model.prefs.ImplPreferencesHelper;
import com.xiaoffe.drawwithu.model.prefs.PreferencesHelper;
import com.xiaoffe.drawwithu.util.PixUtil;
import com.xiaoffe.drawwithu.util.TIMUtil;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by codeest on 16/8/7.
 */
@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper) {
        return retrofitHelper;
    }

    // 我觉得，provideDBHelper需要RealmHelper依赖，并不是provideDataManager驱动的。
    // 应该是AppComponent的RealmHelper realmHelper();驱动的
    @Provides
    @Singleton
    DBHelper provideDBHelper(RealmHelper realmHelper) {
        return realmHelper;
    }


    //像其他的一样，ImplPreferencesHelper被需求（被依赖，被DataManager）,但是ImplPreferencesHelper本身
    //不需要依赖别的类。ImplPreferencesHelper的构造方法被@Inject注释，并且不需要参数（等于说不需要依赖别的类）
    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(ImplPreferencesHelper implPreferencesHelper) {
        return implPreferencesHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper, DBHelper DBHelper, PreferencesHelper preferencesHelper) {
        return new DataManager(httpHelper, DBHelper, preferencesHelper);
    }
    //我觉得TIMUtil写不写在AppModule都行170905
//    @Provides
//    @Singleton
//    TIMUtil provideTIMUtil(PreferencesHelper preferencesHelper){
//        return new TIMUtil(preferencesHelper);
//    }
    @Provides
    @Singleton
    TIMUtil provideTIMUtil(DataManager manager, PixUtil pixUtil){
        return new TIMUtil(manager, pixUtil);
    }

    @Provides
    @Singleton
    PixUtil providePixUtil(App app){
        return new PixUtil(app);
    }
}
