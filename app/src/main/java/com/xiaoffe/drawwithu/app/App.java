package com.xiaoffe.drawwithu.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.xiaoffe.drawwithu.di.component.AppComponent;
import com.xiaoffe.drawwithu.di.component.DaggerAppComponent;
import com.xiaoffe.drawwithu.di.module.AppModule;
import com.xiaoffe.drawwithu.di.module.HttpModule;
import java.util.HashSet;
import java.util.Set;

//import io.realm.Realm;

/**
 * Created by codeest on 2016/8/2.
 */
public class App extends Application{

    private static App instance;
    public static AppComponent appComponent;
    private Set<Activity> allActivities;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    public static synchronized App getInstance() {
        return instance;
    }

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化屏幕宽高
        getScreenSize();
// 数据库Realm，是用来替代sqlite的一种解决方案，它有一套自己的数据库存储引擎，比sqlite更轻量级，
// 拥有更快的速度，并且具有很多现代数据库的特性，
// 比如支持JSON，流式api，数据变更通知，自动数据同步,简单身份验证，访问控制，事件处理，
// 最重要的是跨平台，目前已有Java，Objective C，Swift，React-Native，Xamarin这五种实现。
        //初始化数据库
//        Realm.init(getApplicationContext());

        //在子线程中完成其他初始化
//        InitializeService.start(this);

        if(MsfSdkUtils.isMainProcess(this)){    // 仅在主线程初始化(call sdk相关)
            // 初始化LiveSDK
            ILiveSDK.getInstance().initSdk(this, 1400028096, 11851);

        }
    }
//使用android-support-multidex解决Dex超出方法数的限制问题,让你的应用不再爆棚
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }
//.build();就只到建立了DaggerAppComponent实例
    public static AppComponent getAppComponent(){
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }
}
