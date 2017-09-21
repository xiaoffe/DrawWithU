package com.xiaoffe.drawwithu.base.contract.fragment;

import com.xiaoffe.drawwithu.base.BasePresenter;
import com.xiaoffe.drawwithu.base.BaseView;
import com.xiaoffe.drawwithu.model.bean.MyProfileBean;

/**
 * Created by codeest on 16/8/11.
 */

public interface MyProfileContract {

    interface View extends BaseView {

        void showContent(MyProfileBean info);

    }

    interface Presenter extends BasePresenter<View> {
        void getMyProfileData();

    }
}
