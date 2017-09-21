package com.xiaoffe.drawwithu.base.contract.main;

import com.xiaoffe.drawwithu.base.BasePresenter;
import com.xiaoffe.drawwithu.base.BaseView;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface LoginContract {
    interface View extends BaseView {

        void jumpToMain();//loginSucced
        void jumpToRegister();
        void showWaitDialog();
        void closeWaitDialog();
    }

    interface Presenter extends BasePresenter<View> {

        void tryLogin(String account, String password);
    }
}
