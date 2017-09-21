package com.xiaoffe.drawwithu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.di.component.DaggerFragmentComponent;
import com.xiaoffe.drawwithu.di.component.FragmentComponent;
import com.xiaoffe.drawwithu.di.module.FragmentModule;

import javax.inject.Inject;

/**
 * Created by codeest on 2016/8/2.
 * MVP Fragment基类
 */
//这个和BaseActivity差不多意思
public abstract class BaseFragment<T extends BasePresenter> extends SimpleFragment implements BaseView {

    @Inject
    protected T mPresenter;

    protected FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule(){
        return new FragmentModule(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showErrorMsg(String msg) {
//        SnackbarUtil.show(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0), msg);
    }

    @Override
    public void stateError() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateMain() {

    }

    protected abstract void initInject();
}