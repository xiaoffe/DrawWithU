package com.xiaoffe.drawwithu.presenter.main;

import android.util.Log;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.main.WelcomeContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.LoginBean;
import com.xiaoffe.drawwithu.model.bean.User;
import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.util.RxUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by codeest on 16/8/15.
 */
//从extends RxPresenter<WelcomeContract.View>可以看出WelcomePresenter绑定/解绑的view是实现了WelcomeContract.View的view
    //仅此而已，别混乱xiaoffe21070820 am 2:29
public class WelcomePresenter extends RxPresenter<WelcomeContract.View> implements WelcomeContract.Presenter{
    private static final String TAG = "WelcomePresenter";

    private static final String RES = "1080*1776";

    private static final int COUNT_DOWN_TIME = 2200;
    @Inject
    protected DataManager mDataManager;
    //WelcomePresenter的注入，没有任何Module直接提供。而是通过自己的构造函数提供。参数DataManager有Module提供
    @Inject
    public WelcomePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getWelcomeData() {
//        addSubscribe(mDataManager.fetchWelcomeInfo(RES)
//                .compose(RxUtil.<WelcomeBean>rxSchedulerHelper())
//                .subscribe(new Consumer<WelcomeBean>() {
//                    @Override
//                    public void accept(WelcomeBean welcomeBean) {
//                        mView.showContent(welcomeBean);
//                        Log.d(TAG, "get Data " + welcomeBean + " and begin COUNT DOWN...");
//                        startCountDown();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        Log.d(TAG, "onError and jump to main");
//                        mView.jumpToMain();
//                    }
//                })
//        );
        addSubscribe(mDataManager.getZhuangBiInfo("装逼")
                .compose(RxUtil.<List<ZhuangbiImage>>rxSchedulerHelper())
                .subscribe(new Consumer<List<ZhuangbiImage>>() {
                    @Override
                    public void accept(@NonNull List<ZhuangbiImage> zhuangbiImages){
                        mView.showContent(zhuangbiImages);
                        Log.d(TAG, "get Data, data size : " + zhuangbiImages.size() + " and begin COUNT DOWN...");
                        startCountDown();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.d(TAG, "onError and jump to main");
                        mView.jumpToMain();
                    }
                })
        );
    }

    @Override
    public void tryLogin() {
        final String lastAccount = mDataManager.getLastAccount();
        final String lastPassword = mDataManager.getLastPassword();
        Log.d(TAG, "look preference : " + lastAccount + lastPassword);
        //如果使用tlsLogin（）方法登录，就不能成功使用IM发送信息。用tlsLoginAll（）可以。。20170905
        addSubscribe(mDataManager.getLoginInfo(lastAccount, lastPassword)
                        .compose(RxUtil.<LoginBean>rxSchedulerHelper())
                        .subscribe(new Consumer<LoginBean>() {
                            @Override
                            public void accept(@NonNull LoginBean loginBean) throws Exception {
                                User user = new User();
                                user.username = lastAccount;
                                user.token = loginBean.getToken();
                                user.save();
                                loginSdk(lastAccount, lastPassword);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d(TAG, "login my server failed ::" + throwable);
                                mView.jumpToLogin();
                            }
                        })
        );
    }

    private void loginSdk(final String lastAccount, final String lastPassword){

        ILiveLoginManager.getInstance().tlsLoginAll(lastAccount, lastPassword, new ILiveCallBack() {
            @Override
            public void onSuccess(final Object data) {
                addSubscribe(RxUtil.createData(data)
                        .compose(RxUtil.<Object>rxSchedulerHelper())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object s) throws Exception {
                                mView.jumpToMain();
                                Log.d(TAG, "login onSuccess :" + data);
                            }
                        }));
            }

            @Override
            public void onError(String module, final int errCode, String errMsg) {
                addSubscribe(RxUtil.createData(module)
                        .compose(RxUtil.<String>rxSchedulerHelper())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                mView.jumpToLogin();
                                Log.d(TAG, "login onError :" + errCode);
                            }
                        }));
            }
        });
    }

    private void startCountDown() {
        addSubscribe(Flowable.timer(COUNT_DOWN_TIME, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        Log.d(TAG, "COUNT DOWN OVER");
                        mView.jumpToMain();
                    }
                })
        );
    }
    //重大发现：发现下面不能使用compose(RxUtil.<ResponseBody>rxSchedulerHelper())里面
    // 的.observeOn(AndroidSchedulers.mainThread());。。要用.observeOn(Schedulers.computation())这是为什么呢？20171010
    @Override
    public void downloadApk() {
        addSubscribe(mDataManager.downloadApk()
//                .compose(RxUtil.<ResponseBody>rxSchedulerHelper())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(@NonNull ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .subscribe(new Consumer<InputStream>() {
                    @Override
                    public void accept(@NonNull InputStream in) throws Exception {
                        String path = Constants.APK_PATH+"test.apk";
                        try {
                            File apkFile = new File(path);
                            if (!apkFile.getParentFile().exists()) {
                                apkFile.getParentFile().mkdirs();
                            }
                            if(apkFile != null && apkFile.exists()){
                                apkFile.delete();
                            }
                            FileOutputStream out = null;
                            byte[] buffer = new byte[4096];
                            long readSize = 0;
                            out = new FileOutputStream(apkFile);
                            int len = -1;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                                readSize += len;
                                Log.d(TAG, "progress-----------"+ readSize);
                            }
                            out.flush();
                            out.close();
                            in.close();
                        } catch (IOException e) {
                            Log.d(TAG, "exception: " + e);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    public void getVersionCode() {
        addSubscribe(mDataManager.getVersionCode("getVersion")
                .compose(RxUtil.<VersionBean>rxSchedulerHelper())
                .subscribe(new Consumer<VersionBean>() {
                    @Override
                    public void accept(@NonNull VersionBean versionBean){
                        Log.d(TAG, "get responseBody : " + versionBean.getVersion());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                })
        );
    }
}
