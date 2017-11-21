package com.xiaoffe.drawwithu.model;

import com.xiaoffe.drawwithu.model.bean.LoginBean;
import com.xiaoffe.drawwithu.model.bean.LoginBeanTest;
import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.model.db.DBHelper;
import com.xiaoffe.drawwithu.model.http.HttpHelper;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;
import com.xiaoffe.drawwithu.model.prefs.PreferencesHelper;
import java.util.List;
import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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
    public void insertToken(String s) {
        mDbHelper.insertToken(s);
    }

    @Override
    public Flowable<WelcomeBean> fetchWelcomeInfo(String res) {
        //将 token 放进去
        String account = mPreferencesHelper.getLastAccount();
        return mHttpHelper.fetchWelcomeInfo(res);
    }

    @Override
    public String getToken() {
        return mDbHelper.getToken();
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
    public Flowable<LoginBean> getLoginInfo(String account, String password) {
        return mHttpHelper.getLoginInfo(account, password);
    }

    @Override
    public Flowable<LoginBean> register(String account, String password) {
        return mHttpHelper.register(account, password);
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

    @Override
    public Flowable<LoginBeanTest> uploadFace(String action, String username, String imgPath) {
        return mHttpHelper.uploadFace(action, username, imgPath);
    }

    //    @Override
//    public Flowable<ResponseBody> uploadFace(String description, String imgPath) {
//        return mHttpHelper.uploadFace(description, imgPath);
//    }

    @Override
    public Flowable<ResponseBody> downloadApk() {
        return mHttpHelper.downloadApk();
    }

    @Override
    public Flowable<VersionBean> getVersionCode(String query) {
        return mHttpHelper.getVersionCode(query);
    }
}
