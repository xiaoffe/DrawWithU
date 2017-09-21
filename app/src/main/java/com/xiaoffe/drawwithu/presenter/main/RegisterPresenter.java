package com.xiaoffe.drawwithu.presenter.main;

import android.text.TextUtils;
import android.util.Log;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.main.RegisterContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.util.RxUtil;
import com.xiaoffe.drawwithu.util.ToastUtil;
import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by codeest on 16/8/15.
 */
public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter{
    private static final String TAG = "RegisterPresenter";
    private DataManager mDataManager;

    @Inject
    public RegisterPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void tryRejester(String account, String password) {
        if (TextUtils.isEmpty(account) || !isValidUserName(password)) {
            ToastUtil.show("账号不规范哟~~~~(>_<)~~~~");
            return;
        } else if (TextUtils.isEmpty(password) || !isValidPassword(password)) {
            ToastUtil.show("密码不规范哟~~~~(>_<)~~~~");
            return;
        }
        mView.showWaitDialog();
        regist(account, password);
    }
    private boolean isValidUserName(String userName) {
        return userName.length() >= 4 &&
                userName.length() <= 24 &&
                userName.matches("^[A-Za-z0-9]*[A-Za-z][A-Za-z0-9]*$");
    }
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.length() <= 16 && password.matches("^[A-Za-z0-9]*$");
    }
    private void regist(String account, String password){
        Log.d(TAG, "here 111");
        ILiveLoginManager.getInstance().tlsRegister(account, password, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.shortShow("regist success!");
                addSubscribe(RxUtil.createData(data)
                        .compose(RxUtil.<Object>rxSchedulerHelper())
                        .subscribe(new Consumer<Object>() {
                                       @Override
                                       public void accept(@NonNull Object o) throws Exception {
                                           Log.d(TAG, "here 222");
                                           mView.closeWaitDialog();
                                           mView.jumpToLogin();
                                           ToastUtil.show("regist success!");
                                       }
                                   }
                        ));
            }

            @Override
            public void onError(String module, final int errCode, String errMsg) {
                ToastUtil.shortShow("regist failed:" + module+"|"+errCode+"|"+errMsg);
                addSubscribe(RxUtil.createData(module)
                        .compose(RxUtil.<String>rxSchedulerHelper())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                Log.d(TAG, "here 333");
                                mView.closeWaitDialog();
                                ToastUtil.show("login onError :" + errCode);
                                Log.d(TAG, "login onError :" + errCode);
                            }
                        }));
            }
        });
    }
}
