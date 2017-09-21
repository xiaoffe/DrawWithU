package com.xiaoffe.drawwithu.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.app.Constants;

import javax.inject.Inject;

/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @description:
 */

public class ImplPreferencesHelper implements PreferencesHelper {

    private static final boolean DEFAULT_NIGHT_MODE = false;
    private static final boolean DEFAULT_NO_IMAGE = false;
    private static final boolean DEFAULT_AUTO_SAVE = true;

    private static final boolean DEFAULT_LIKE_POINT = false;
    private static final boolean DEFAULT_VERSION_POINT = false;
    private static final boolean DEFAULT_MANAGER_POINT = false;

    private static final int DEFAULT_CURRENT_ITEM = Constants.TYPE_ZHIHU;

    private static final String SHAREDPREFERENCES_NAME = "my_sp";

    private final SharedPreferences mSPrefs;

    @Inject
    public ImplPreferencesHelper() {
        mSPrefs = App.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

//    @Override
//    public boolean getVersionPoint() {
//        return mSPrefs.getBoolean(Constants.SP_VERSION_POINT, DEFAULT_VERSION_POINT);
//    }
//
//    @Override
//    public void setVersionPoint(boolean isFirst) {
//        mSPrefs.edit().putBoolean(Constants.SP_VERSION_POINT, isFirst).apply();
//    }

    @Override
    public boolean getNoImageState() {
        return mSPrefs.getBoolean(Constants.SP_NO_IMAGE, DEFAULT_NO_IMAGE);
    }

    @Override
    public void setNoImageState(boolean state) {
        mSPrefs.edit().putBoolean(Constants.SP_NO_IMAGE, state).apply();
    }

    @Override
    public String getLastAccount() {
        return mSPrefs.getString(Constants.LAST_ACCOUNT, "");
    }

    @Override
    public String getLastPassword() {
        return mSPrefs.getString(Constants.LAST_PASSWORD, "");
    }
    //别忘了加.apply()
    @Override
    public void setLastAccount(String account) {
        mSPrefs.edit().putString(Constants.LAST_ACCOUNT, account).apply();
    }

    @Override
    public void setLastPassword(String password) {
        mSPrefs.edit().putString(Constants.LAST_PASSWORD, password).apply();
    }
}
