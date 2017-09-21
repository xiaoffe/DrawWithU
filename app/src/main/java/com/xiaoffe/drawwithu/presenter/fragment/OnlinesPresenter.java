package com.xiaoffe.drawwithu.presenter.fragment;

import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.fragment.OnlinesContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.OnlinesBean;
import com.xiaoffe.drawwithu.util.RxUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class OnlinesPresenter extends RxPresenter<OnlinesContract.View> implements OnlinesContract.Presenter{

    private DataManager mDataManager;

    @Inject
    public OnlinesPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getOnlineData() {
        //获取假数据
        OnlinesBean dummy = new OnlinesBean();
        addSubscribe(RxUtil.createData(dummy).delay(1,TimeUnit.SECONDS)
                .compose(RxUtil.<OnlinesBean>rxSchedulerHelper())
                .subscribe(new Consumer<OnlinesBean>() {
                               @Override
                               public void accept(@NonNull OnlinesBean b) throws Exception {
                                   mView.showContent(b);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(@NonNull Throwable throwable) throws Exception {
                               }
                           }
                ));
    }

    @Override
    public void getMoreOnlineData() {
//        mView.stateLoading();
        //获取假数据
        OnlinesBean dummy = new OnlinesBean();
        addSubscribe(RxUtil.createData(dummy).delay(1,TimeUnit.SECONDS)
                .compose(RxUtil.<OnlinesBean>rxSchedulerHelper())
                .subscribe(new Consumer<OnlinesBean>() {
                               @Override
                               public void accept(@NonNull OnlinesBean b) throws Exception {
                                   mView.showMoreContent(b);
//                                   mView.stateMain();
                               }
                           }
                ));
    }
    //因为refresh()是关于圈圈操作的。所以模拟一个延迟3秒动作
    @Override
    public void refresh() {
        OnlinesBean dummy = new OnlinesBean();
        addSubscribe(RxUtil.createData(dummy).delay(2,TimeUnit.SECONDS)
                .compose(RxUtil.<OnlinesBean>rxSchedulerHelper())
                .subscribe(new Consumer<OnlinesBean>() {
                               @Override
                               public void accept(@NonNull OnlinesBean b) throws Exception {
                                   mView.showContent(b);
                               }
                           }
                ));
    }

}
