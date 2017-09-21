package com.xiaoffe.drawwithu.presenter.main;

import android.util.Log;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.main.LoginContract;
import com.xiaoffe.drawwithu.base.contract.main.WelcomeContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.util.RxUtil;
import com.xiaoffe.drawwithu.util.WaitDialogUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/8/30.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter{
    private static final String TAG = "LoginPresenter";
    private static final int COUNT_DOWN_TIME = 2200;

    private DataManager mDataManager;
    //LoginPresenter的注入，没有任何Module直接提供。而是通过自己的构造函数提供。参数DataManager有Module提供
    @Inject
    public LoginPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void tryLogin(final String account, final String password) {
        mView.showWaitDialog();
        //如果使用tlsLogin（）方法登录，就不能成功使用IM发送信息。用tlsLoginAll（）可以。。20170905
        ILiveLoginManager.getInstance().tlsLoginAll(account, password, new ILiveCallBack() {
            @Override
            public void onSuccess(final Object data) {
                addSubscribe(RxUtil.createData(data)
                        .compose(RxUtil.<Object>rxSchedulerHelper())
                        .subscribe(new Consumer<Object>() {
                                       @Override
                                       public void accept(@NonNull Object s) throws Exception {
                                           mDataManager.setLastAccount(account);
                                           mDataManager.setLastPassword(password);
                                           Log.d(TAG, "login onSuccess :" + data);
                                           Log.d(TAG, "..." + mDataManager.getLastAccount() + mDataManager.getLastPassword());
                                           mView.closeWaitDialog();
                                           mView.jumpToMain();
                                       }
                                   }
                        ));
            }

            @Override
            public void onError(String module, final int errCode, String errMsg) {
                addSubscribe(RxUtil.createData(module)
                        .compose(RxUtil.<String>rxSchedulerHelper())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                Log.d(TAG, "login onError :" + errCode);
                                mView.closeWaitDialog();
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
}