package com.xiaoffe.drawwithu.base.contract.main;


import com.xiaoffe.drawwithu.base.BasePresenter;
import com.xiaoffe.drawwithu.base.BaseView;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;

import java.util.List;

/**
 * Created by codeest on 16/8/15.
 */

public interface WelcomeContract {

    interface View extends BaseView {

        void showContent(List<ZhuangbiImage> welcomeBean);

//        void showContent(WelcomeBean welcomeBean);

        void jumpToMain();

        void jumpToLogin();
    }

    interface Presenter extends BasePresenter<View> {

        void getWelcomeData();

        void tryLogin();
    }
}
