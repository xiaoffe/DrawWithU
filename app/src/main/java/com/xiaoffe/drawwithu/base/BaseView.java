package com.xiaoffe.drawwithu.base;

/**
 * View基类
 */
public interface BaseView {

    void showErrorMsg(String msg);

    //=======  State  =======
    void stateError();

    void stateEmpty();

    void stateLoading();

    void stateMain();

}
