package com.xiaoffe.drawwithu.base;

import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.di.component.ActivityComponent;
import com.xiaoffe.drawwithu.di.component.DaggerActivityComponent;
import com.xiaoffe.drawwithu.di.module.ActivityModule;
import javax.inject.Inject;

/**
 * Created by codeest on 2016/8/2.
 * MVP activity基类
 */
//BaseActivity<T extends BasePresenter> 表示BaseActivity里应该有一个实现BasePresenter的成员
public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView {

    @Inject
    protected T mPresenter;

    protected ActivityComponent getActivityComponent(){
        return  DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    //改成private岂不更好
    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    //mPresenter attachView 是绑定的BaseView，而不是绑定mPresenter的类型T
    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
//        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
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