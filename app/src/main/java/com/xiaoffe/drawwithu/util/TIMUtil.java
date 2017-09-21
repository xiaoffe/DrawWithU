package com.xiaoffe.drawwithu.util;

import android.util.Log;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.prefs.PreferencesHelper;

import javax.inject.Inject;

/**
 * Created by xiaoluo on 2017/1/19.
 */
public class TIMUtil {
    private static final String TAG = "TIMUtil";
    private TIMConversation conversation;
//    static PreferencesHelper mPreferencesHelper;
//    public TIMUtil(PreferencesHelper preferencesHelper){
//        this.mPreferencesHelper = preferencesHelper;
//    }
    static DataManager dataManager;
    static PixUtil pixUtil;
    public TIMUtil(DataManager dataManager, PixUtil pixUtil){
        this.dataManager = dataManager;
        this.pixUtil = pixUtil;
    }
    public void sendCode(String text){
        Log.d(TAG, ".............." + text);
        String peerAccount="";
        String myAccount = dataManager.getLastAccount();
        Log.d(TAG, "set myAccount " + myAccount);
        if(myAccount.equals("15700071533a")){
            peerAccount = "15700071533b";
            Log.d(TAG, "peerAccount  (\"15700071533b\");");
        }else if(myAccount.equals("15700071533b")){
            peerAccount = "15700071533a";
            Log.d(TAG, "peerAccount  (\"15700071533a\");");
        }
        Log.d(TAG, "here1");
        conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C, peerAccount);
        TIMMessage msg = new TIMMessage();
//        Long timeStamp = System.currentTimeMillis();
//        msg.setTimestamp(timeStamp);
//        System.out.println("xiaoluo TIMUtils.sendCode() timeStamp:"+timeStamp);
        TIMTextElem elem = new TIMTextElem();
        elem.setText(text);
        int iRet = msg.addElement(elem);
        if (iRet != 0) {
            Log.d(TAG, "add element error:" + iRet);
            return;
        }
        Log.d(TAG, "ready send text msg");
        conversation.sendOnlineMessage(msg, new TIMValueCallBack<TIMMessage>() {// 发送消息回调
            public void onError(int code, String desc) {// 发送消息失败
                Log.i(TAG, "====发消息失败");
            }

            public void onSuccess(TIMMessage arg0) {
                Log.i(TAG, "====发消息成功");
            }
        });
//        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {// 发送消息回调
//            public void onError(int code, String desc) {// 发送消息失败
//                Log.i(TAG, "====发消息失败");
//            }
//
//            public void onSuccess(TIMMessage arg0) {
//                Log.i(TAG, "====发消息成功");
//            }
//        });
    }

    public void sendBitmap(float width, float height){
        String peerAccount="";
        String myAccount = dataManager.getLastAccount();
        Log.d(TAG, "set myAccount " + myAccount);
        if(myAccount.equals("15700071533a")){
            peerAccount = "15700071533b";
            Log.d(TAG, "MainApplication.getInstance().setPeerName(\"15700071533b\");");
        }else if(myAccount.equals("15700071533b")){
            peerAccount = "15700071533a";
            Log.d(TAG, "MainApplication.getInstance().setPeerName(\"15700071533a\");");
        }
        conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C, peerAccount);
        TIMMessage msg = new TIMMessage();
//        Long timeStamp = System.currentTimeMillis();
//        msg.setTimestamp(timeStamp);
        TIMImageElem imageElem = new TIMImageElem();
        imageElem.setPath(Constants.CHOOSE_PIC_PATH + "temp.jpg");
        int iRet = msg.addElement(imageElem);
        if (iRet != 0) {
            Log.d(TAG, "add element error:" + iRet);
            return;
        }
        //再附加一个图片占屏幕大小比
        TIMTextElem timTextElem = new TIMTextElem();
        String comment = pixUtil.getWidthPercent(width) + " " + pixUtil.getHeightPercent(height);
        timTextElem.setText(comment);
        Log.d(TAG, comment);
        int iRetComment = msg.addElement(timTextElem);
        if (iRetComment != 0) {
            Log.d(TAG, "add comment error:" + iRetComment);
            return;
        }
        Log.d(TAG, "ready send image msg");
        conversation.sendOnlineMessage(msg, new TIMValueCallBack<TIMMessage>() {// 发送消息回调
            public void onError(int code, String desc) {// 发送消息失败
                Log.i(TAG, "====发图片消息失败");
            }

            public void onSuccess(TIMMessage arg0) {
                Log.i(TAG, "====发图片消息成功");
            }
        });
//        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {// 发送消息回调
//            public void onError(int code, String desc) {// 发送消息失败
//                Log.i(TAG, "====发图片消息失败");
//            }
//
//            public void onSuccess(TIMMessage arg0) {
//                Log.i(TAG, "====发图片消息成功");
//            }
//        });
    }

}
