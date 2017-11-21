package com.xiaoffe.drawwithu.presenter.fragment;

import android.Manifest;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.fragment.MyProfileContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.LoginBeanTest;
import com.xiaoffe.drawwithu.model.bean.MyProfileBean;
import com.xiaoffe.drawwithu.util.RxUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class MyProfilePresenter extends RxPresenter<MyProfileContract.View> implements MyProfileContract.Presenter{
    private final String TAG = "profile";
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

    @Override
    public void uploadAvatar(String action, String username, final String imgPath) {
        addSubscribe(mDataManager.uploadFace(action, username, imgPath)
                    .compose(RxUtil.<LoginBeanTest>rxSchedulerHelper())
                    .subscribe(new Consumer<LoginBeanTest>() {
                        @Override
                        public void accept(@NonNull LoginBeanTest responseBody) throws Exception {
                            Log.d("XX", responseBody.toString());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {

                        }
                    }));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpPost httppost = new HttpPost("http://gzq.test.szgps.net:88/tlsuser.php?action=newSetUserIcon&username=15700071533");
//                    FileEntity fileEntity = new FileEntity(new File(imgPath), "binary/octet-stream");
//                    httppost.setEntity(fileEntity);
//                    HttpResponse response = httpclient.execute(httppost);
//                    HttpEntity resEntity = response.getEntity();
//                    System.out.println(response.getStatusLine());
//                    if (resEntity != null && response.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = response.getEntity();
//                        System.out.println("in is===== " + entity);
//                        loadAvatar("http://gzq.test.szgps.net:88/upload/usericon/15700071533.jpg");
//                    }
//                }catch (Exception e){
//                    System.out.println("in is===== " + e);
//                }
//            }
//        }).start();

    }

    @Override
    public void loadAvatar(String url) {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "getSelf error" + s);
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                Log.d(TAG, "getSelf success" + timUserProfile.getFaceUrl());
                mView.loadAvatar(timUserProfile.getFaceUrl());
            }
        });
//        mView.loadAvatar(url);
    }

    //for test
    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        addSubscribe(rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                        } else {
                        }
                        Log.d("XX", "Manifest.permission.READ_EXTERNAL_STORAGE" + granted);
                    }
                })
        );
    }
}
