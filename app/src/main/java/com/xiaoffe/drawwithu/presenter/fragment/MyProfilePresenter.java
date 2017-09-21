package com.xiaoffe.drawwithu.presenter.fragment;

import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.fragment.MyProfileContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.MyProfileBean;
import com.xiaoffe.drawwithu.util.RxUtil;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MyProfilePresenter extends RxPresenter<MyProfileContract.View> implements MyProfileContract.Presenter{

    private DataManager mDataManager;

    @Inject
    public MyProfilePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getMyProfileData() {
        MyProfileBean dummy = new MyProfileBean();
        addSubscribe(RxUtil.createData(dummy).delay(1,TimeUnit.SECONDS)
                .compose(RxUtil.<MyProfileBean>rxSchedulerHelper())
                .subscribe(new Consumer<MyProfileBean>() {
                               @Override
                               public void accept(@NonNull MyProfileBean b) throws Exception {
                                   mView.showContent(b);
                               }
                           }
                ));
    }

}
