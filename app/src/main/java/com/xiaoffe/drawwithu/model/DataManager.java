package com.xiaoffe.drawwithu.model;


import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.model.db.DBHelper;
import com.xiaoffe.drawwithu.model.http.HttpHelper;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;
import com.xiaoffe.drawwithu.model.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Flowable;


/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @desciption:
 */

public class DataManager implements HttpHelper, DBHelper, PreferencesHelper {

    HttpHelper mHttpHelper;
    DBHelper mDbHelper;
    PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper, DBHelper dbHelper, PreferencesHelper preferencesHelper) {
        mHttpHelper = httpHelper;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void insertNewsId(int id) {
        mDbHelper.insertNewsId(id);
    }

//    @Override
//    public boolean getVersionPoint() {
//        return mPreferencesHelper.getVersionPoint();
//    }
//
//    @Override
//    public void setVersionPoint(boolean isFirst) {
//        mPreferencesHelper.setVersionPoint(isFirst);
//    }

    @Override
    public Flowable<WelcomeBean> fetchWelcomeInfo(String res) {
        return mHttpHelper.fetchWelcomeInfo(res);
    }

    @Override
    public boolean queryNewsId(int id) {
        return mDbHelper.queryNewsId(id);
    }

    @Override
    public boolean getNoImageState() {
        return mPreferencesHelper.getNoImageState();
    }

    @Override
    public void setNoImageState(boolean state) {
        mPreferencesHelper.setNoImageState(state);
    }

    @Override
    public Flowable<MyHttpResponse<VersionBean>> fetchVersionInfo() {
        return mHttpHelper.fetchVersionInfo();
    }

    @Override
    public Flowable<List<ZhuangbiImage>> getZhuangBiInfo(String query) {
        return mHttpHelper.getZhuangBiInfo(query);
    }

    @Override
    public String getLastAccount() {
        return mPreferencesHelper.getLastAccount();
    }

    @Override
    public void setLastAccount(String account) {
        mPreferencesHelper.setLastAccount(account);
    }

    @Override
    public String getLastPassword() {
        return mPreferencesHelper.getLastPassword();
    }

    @Override
    public void setLastPassword(String password) {
        mPreferencesHelper.setLastPassword(password);
    }
}
