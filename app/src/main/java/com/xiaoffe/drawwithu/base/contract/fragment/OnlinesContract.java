package com.xiaoffe.drawwithu.base.contract.fragment;

import com.xiaoffe.drawwithu.base.BasePresenter;
import com.xiaoffe.drawwithu.base.BaseView;
import com.xiaoffe.drawwithu.model.bean.OnlinesBean;

import java.util.List;

/**
 * Created by codeest on 16/8/11.
 */

public interface OnlinesContract {

    interface View extends BaseView {

//        void showContent(OnlinesBean info);
//
//        void showMoreContent(OnlinesBean info);

        void showContent(List<String> friendIds, List<String> friendSigns, List<String> friendFaceUrls);

        void showMoreContent(List<String> friendIds, List<String> friendSigns, List<String> friendFaceUrls);
    }

    interface Presenter extends BasePresenter<View> {
        void getOnlineData();
        void getMoreOnlineData();
        //refresh()和getOnlineData（）不同之处：
        // refresh()操作，虽然是和getOnlineData（）一样获取数据。但是有一个不同地方，
        // 就是 一定会在数据获取完后关闭转圈圈的控件。getOnlineData()没有这个任务
        void refresh();
    }
}
