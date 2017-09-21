package com.xiaoffe.drawwithu.presenter.main;

import android.util.Log;
import com.tencent.callsdk.ILVCallConstants;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVCallOption;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.RxPresenter;
import com.xiaoffe.drawwithu.base.contract.main.SyncPadContract;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.ui.main.activity.SyncPadActivity;
import com.xiaoffe.drawwithu.util.TIMUtil;
import javax.inject.Inject;

/**
 * Created by Administrator on 2017/8/30.
 */

public class SyncPadPresenter extends RxPresenter<SyncPadContract.View> implements SyncPadContract.Presenter{
    private static final String TAG ="syncpadPresenter";
    private DataManager mDataManager;
    private TIMUtil mTIMUtil;

    ILVCallOption option;
    private int mCallId;
    @Inject
    public SyncPadPresenter(TIMUtil util, DataManager dataManager) {
        this.mTIMUtil = util;
        this.mDataManager = dataManager;
         option = new ILVCallOption(mDataManager.getLastAccount())
                .callTips("CallSDK Demo")
                 .setMemberListener((SyncPadActivity)mView)
                .setCallType(ILVCallConstants.CALL_TYPE_AUDIO);
    }

    @Override
    public void sendCode(String text) {
        mTIMUtil.sendCode(text);
    }

    @Override
    public void sendBitmap(float width, float height) {
        mTIMUtil.sendBitmap(width, height);
    }

    @Override
    public void closePad() {
        mTIMUtil.sendCode(Constants.CLOSE_DRAW);
    }

    @Override
    public void makeCall() {
        Log.d(TAG, "makeCall");
        if(mDataManager.getLastAccount().equals("15700071533a")){
            mCallId = ILVCallManager.getInstance().makeCall("15700071533b", option);
        }else if(mDataManager.getLastAccount().equals("15700071533b")){
            mCallId = ILVCallManager.getInstance().makeCall("15700071533a", option);
        }
    }

    @Override
    public void acceptCall(int callId) {
        Log.d(TAG, "acceptCall");
        ILVCallManager.getInstance().acceptCall(callId, option);
        mCallId = callId;
    }

    @Override
    public void endCall() {
        Log.d(TAG, "endCall");
        ILVCallManager.getInstance().endCall(mCallId);
    }

    @Override
    public void switchMicSpeaker(boolean on) {
        if(on){
            mTIMUtil.sendCode(Constants.OPEN_MIC_SPEAKER);
        }else{
            mTIMUtil.sendCode(Constants.CLOSE_MIC_SPEAKER);
        }
    }
}