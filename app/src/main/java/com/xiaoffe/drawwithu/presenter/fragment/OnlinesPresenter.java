package com.xiaoffe.drawwithu.presenter.fragment;

import android.util.Log;

import com.tencent.TIMConversation;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.fragment.OnlinesContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.OnlinesBean;
import com.xiaoffe.drawwithu.util.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class OnlinesPresenter extends RxPresenter<OnlinesContract.View> implements OnlinesContract.Presenter{
    private final String TAG = "online";
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
//                                   mView.showContent(b);
                                   TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMUserProfile>>() {
                                       @Override
                                       public void onError(int i, String s) {
                                           Log.d(TAG, "getFriendList error " + s);
                                       }

                                       @Override
                                       public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                                           List<String> friendIds = new ArrayList<String>();
                                           List<String> friendSigns = new ArrayList<String>();
                                           List<String> friendFaceUrls = new ArrayList<String>();
                                           for(TIMUserProfile timUserProfile : timUserProfiles){
                                               Log.d(TAG, timUserProfile.getIdentifier() + timUserProfile.getFaceUrl());
                                               String friendId = timUserProfile.getIdentifier();
                                               if(friendId.equals(mDataManager.getLastAccount())){
                                                   continue;
                                               }
                                               friendIds.add(friendId);
                                               friendSigns.add(timUserProfile.getSelfSignature());
                                               friendFaceUrls.add(timUserProfile.getFaceUrl());
                                           }
                                           mView.showContent(friendIds,friendSigns,friendFaceUrls);
                                       }
                                   });
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
        //获取假数据(!!!!如果这里不用延时1s，当时发现crash。原因先不管20171024)
        OnlinesBean dummy = new OnlinesBean();
        addSubscribe(RxUtil.createData(dummy).delay(1,TimeUnit.SECONDS)
                .compose(RxUtil.<OnlinesBean>rxSchedulerHelper())
                .subscribe(new Consumer<OnlinesBean>() {
                               @Override
                               public void accept(@NonNull OnlinesBean b) throws Exception {
//                                   mView.showMoreContent(b);
                                   // test
                                   List<String> friendIds = new ArrayList<String>();
                                   List<String> friendSigns = new ArrayList<String>();
                                   List<String> friendFaceUrls = new ArrayList<String>();
                                   for(int n = 0; n <=10; n++){
                                       friendIds.add("fake id " + new Random().nextInt(1000));
                                       friendSigns.add("dummy sign " + new Random().nextInt(1000));
                                       friendFaceUrls.add(null);
                                   }
                                   mView.showMoreContent(friendIds,friendSigns,friendFaceUrls);
                               }
                           }
                ));

    }
    //因为refresh()是关于圈圈操作的。所以模拟一个延迟3秒动作
    @Override
    public void refresh() {
//        OnlinesBean dummy = new OnlinesBean();
//        addSubscribe(RxUtil.createData(dummy).delay(2,TimeUnit.SECONDS)
//                .compose(RxUtil.<OnlinesBean>rxSchedulerHelper())
//                .subscribe(new Consumer<OnlinesBean>() {
//                               @Override
//                               public void accept(@NonNull OnlinesBean b) throws Exception {
//                                   mView.showContent(b);
//                               }
//                           }
//                ));
        getOnlineData();
    }

}
