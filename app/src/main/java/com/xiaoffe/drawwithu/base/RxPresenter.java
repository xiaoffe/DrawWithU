package com.xiaoffe.drawwithu.base;

//import com.xiaoffe.drawwithu.component.RxBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by codeest on 2016/8/2.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
//看不懂PxPresenter  170814
// RxPresenter<T extends BaseView>表示RxPresenter里应该有一个实现BaseView的成员。（这个成员是用来绑定引用MVP的V用的）
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;
    protected CompositeDisposable mCompositeDisposable;

//  RxJava中已经内置了一个容器CompositeDisposable,
// 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中,
// 在退出的时候, 调用CompositeDisposable.clear() 即可切断所有的水管.
// （既Rx后面收不到回调，就不会有更新UI等后续会崩溃的操作，因为Activity已经销毁）
    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

//    protected <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act) {
//        if (mCompositeDisposable == null) {
//            mCompositeDisposable = new CompositeDisposable();
//        }
//        mCompositeDisposable.add(RxBus.getDefault().toDefaultFlowable(eventType, act));
//    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
