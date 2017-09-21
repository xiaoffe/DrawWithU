package com.xiaoffe.drawwithu.base.contract.main;

        import com.xiaoffe.drawwithu.base.BasePresenter;
        import com.xiaoffe.drawwithu.base.BaseView;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface SyncPadContract {
    interface View extends BaseView {
        void dummy();
    }

    interface Presenter extends BasePresenter<View> {
        void sendCode(String text);
        void sendBitmap(float widht, float height);
        void closePad();
        void makeCall();
        void acceptCall(int callId);
        void endCall();
        void switchMicSpeaker(boolean on);
    }
}
