package com.xiaoffe.drawwithu.presenter.main;

import android.Manifest;
import android.net.NetworkInfo;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.main.MainContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;
import com.xiaoffe.drawwithu.util.RxUtil;
import com.xiaoffe.drawwithu.widget.CommonSubscriber;
import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by codeest on 16/8/9.
 */
//MVP
    //implements MainContract.Presenter是P
    //<MainContract.View>是V
    //DataManager是M
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter{

    private DataManager mDataManager;
    private App mApp;

    @Inject
    public MainPresenter(DataManager mDataManager, App mApp) {
        this.mDataManager = mDataManager;
        this.mApp = mApp;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
    }

    @Override
    public void checkVersion(final String currentVersion) {
        addSubscribe(mDataManager.fetchVersionInfo()
                .compose(RxUtil.<MyHttpResponse<VersionBean>>rxSchedulerHelper())
                .compose(RxUtil.<VersionBean>handleMyResult())
                .filter(new Predicate<VersionBean>() {
                    @Override
                    public boolean test(@NonNull VersionBean versionBean) throws Exception {
                        return Integer.valueOf(currentVersion.replace(".", "")) < Integer.valueOf(versionBean.getCode().replace(".", ""));
                    }
                })
                .map(new Function<VersionBean, String>() {
                    @Override
                    public String apply(VersionBean bean) {
                        StringBuilder content = new StringBuilder("版本号: v");
                        content.append(bean.getCode());
                        content.append("\r\n");
                        content.append("版本大小: ");
                        content.append(bean.getSize());
                        content.append("\r\n");
                        content.append("更新内容:\r\n");
                        content.append(bean.getDes().replace("\\r\\n","\r\n"));
                        return content.toString();
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(mView) {
                    @Override
                    public void onNext(String s) {
                        mView.showUpdateDialog(s);
                    }
                })
        );
    }

    @Override
    public void checkPermissions(RxPermissions rxPermissions) {
        addSubscribe(rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            mView.startDownloadService();
                        } else {
                            mView.showErrorMsg("下载应用需要文件写入权限哦~");
                        }
                    }
                })
        );
    }

    @Override
    public void checkNetworkState() {
        ReactiveNetwork.observeNetworkConnectivity(mApp.getBaseContext())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Connectivity>() {
                    @Override
                    public void accept(@NonNull Connectivity connectivity) throws Exception {
                        NetworkInfo.State state = connectivity.getState();
                        if(state.equals(NetworkInfo.State.CONNECTED)){
                            mView.showNetworkConnected();
                        }else if(state.equals(NetworkInfo.State.DISCONNECTED)){
                            mView.showNetworkDisconnected();
                        }
                    }
                });

    }

    //    @Override
//    public void setVersionPoint(boolean b) {
//        mDataManager.setVersionPoint(b);
//    }
//
//    @Override
//    public boolean getVersionPoint() {
//        return mDataManager.getVersionPoint();
//    }

}
