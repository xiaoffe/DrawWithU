package com.xiaoffe.drawwithu.base;

import android.view.View;
import android.view.ViewGroup;

import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.widget.ProgressImageView;

/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @desciption:
 */
//在这个类里没有看到持有T的类成员， 这里只是一个过渡。T的类成员在BaseFragment里
//这个类不处理和泛型T有关的事情
//总结一下，这个类的作用就是，在FrameLayout（应该是FrameLayout）中添加了各种状态的View（加载中，错误等。。但好像没看到空白）
public abstract class RootFragment<T extends BasePresenter> extends BaseFragment<T> {

    private static final int STATE_MAIN = 0x00;
    private static final int STATE_LOADING = 0x01;
    private static final int STATE_ERROR = 0x02;

//    private ProgressImageView ivLoading;
    private View viewError;
    private View viewLoading;
    private ViewGroup viewMain;
    private ViewGroup mParent;

    private int mErrorResource = R.layout.view_error;

    private int currentState = STATE_MAIN;
    private boolean isErrorViewAdded = false;

    @Override
    protected void initEventAndData() {
        if (getView() == null)
            return;
        //例如，这个R.id.view_main是zhihu的layout.xml里面的
        viewMain = (ViewGroup) getView().findViewById(R.id.view_main);
        if (viewMain == null) {
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain a View named 'view_main'.");
        }
        if (!(viewMain.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "view_main's ParentView should be a ViewGroup.");
        }
        //下面这种将layout加入到mParent中的写法，不熟悉
        mParent = (ViewGroup) viewMain.getParent();
        View.inflate(mContext, R.layout.view_progress, mParent);
        viewLoading = mParent.findViewById(R.id.view_loading);
//        ivLoading = (ProgressImageView) viewLoading.findViewById(R.id.iv_progress);
        viewLoading.setVisibility(View.GONE);
        viewMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateError() {
        if (currentState == STATE_ERROR)
            return;
        if (!isErrorViewAdded) {
            isErrorViewAdded = true;
            //同上，将layout加入到mParent中的写法，不熟悉
            View.inflate(mContext, mErrorResource, mParent);
            viewError = mParent.findViewById(R.id.view_error);
            if (viewError == null) {
                throw new IllegalStateException(
                        "A View should be named 'view_error' in ErrorLayoutResource.");
            }
        }
        hideCurrentView();
        currentState = STATE_ERROR;
        viewError.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateLoading() {
        if (currentState == STATE_LOADING)
            return;
        hideCurrentView();
        currentState = STATE_LOADING;
        viewLoading.setVisibility(View.VISIBLE);
//        ivLoading.start();
    }

    @Override
    public void stateMain() {
        if (currentState == STATE_MAIN)
            return;
        hideCurrentView();
        currentState = STATE_MAIN;
        viewMain.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
                viewMain.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
//                ivLoading.stop();
                viewLoading.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                if (viewError != null) {
                    viewError.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setErrorResource(int errorLayoutResource) {
        this.mErrorResource = errorLayoutResource;
    }
}
