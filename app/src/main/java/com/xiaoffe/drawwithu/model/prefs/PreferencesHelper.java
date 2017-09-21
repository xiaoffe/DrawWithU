package com.xiaoffe.drawwithu.model.prefs;

/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @description:
 */

public interface PreferencesHelper {

//    boolean getVersionPoint();
//
//    void setVersionPoint(boolean isFirst);

    boolean getNoImageState();

    void setNoImageState(boolean state);

    String getLastAccount();

    void setLastAccount(String account);

    String getLastPassword();

    void setLastPassword(String password);
}
