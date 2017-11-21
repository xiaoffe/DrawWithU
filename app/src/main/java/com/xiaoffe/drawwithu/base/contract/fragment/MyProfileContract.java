package com.xiaoffe.drawwithu.base.contract.fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaoffe.drawwithu.base.BasePresenter;
import com.xiaoffe.drawwithu.base.BaseView;
import com.xiaoffe.drawwithu.model.bean.MyProfileBean;

/**
 * Created by codeest on 16/8/11.
 */

public interface MyProfileContract {

    interface View extends BaseView {

        void showContent(MyProfileBean info);
        void loadAvatar(String url);
    }

    interface Presenter extends BasePresenter<View> {
        void getMyProfileData();
        void uploadAvatar(String action, String username, String imgPath);
        void loadAvatar(String url);
        //for test
        void checkPermissions(RxPermissions rxPermissions);
    }
}
