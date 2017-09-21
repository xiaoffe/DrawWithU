package com.xiaoffe.drawwithu.base.contract.main;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaoffe.drawwithu.base.BasePresenter;
import com.xiaoffe.drawwithu.base.BaseView;

/**
 * Created by codeest on 16/8/9.
 */

public interface MainContract {

    interface View extends BaseView {

        void showUpdateDialog(String versionContent);

        void startDownloadService();

        void showNetworkConnected();

        void showNetworkDisconnected();
    }

    interface  Presenter extends BasePresenter<View> {

        void checkVersion(String currentVersion);

        void checkPermissions(RxPermissions rxPermissions);

        void checkNetworkState();
//        void setVersionPoint(boolean b);
//
//        boolean getVersionPoint();
    }
}
