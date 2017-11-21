package com.xiaoffe.drawwithu.service;

import java.util.ArrayList;
import java.util.List;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.TIMUserStatusListener;
import com.tencent.TIMValueCallBack;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.ui.main.activity.SyncPadActivity;
import com.xiaoffe.drawwithu.util.TIMUtil;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import javax.inject.Inject;


public class MsgChatService extends Service{
	private final static String TAG = "MsgChatService";
	@Inject TIMUtil timUtil;
	private AlertDialog mIncomingDlg;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setMessageListener();
		Log.d(TAG, "MsgChatService启动");

	}

	// 消息监听器
	private TIMMessageListener msgListener = new TIMMessageListener() {
		@Override
		public boolean onNewMessages(List<TIMMessage> list) {
			Log.d(TAG, "onNewMessages 收到信息");
			for(final TIMMessage timmsg : list){
				Log.d(TAG, "COUNT............." + timmsg.getElementCount());
				for(int n = 0; n < timmsg.getElementCount(); n++){
					Log.d(TAG, "elem type " + timmsg.getElement(n).getType());
				}
				String senderName = timmsg.getSender();
				TIMElem elem = timmsg.getElement(0);
				Log.d(TAG, "消息类型：" + elem.getType());
				if(elem.getType().equals(TIMElemType.Text)){
					Log.d(TAG, ((TIMTextElem) elem).getText());
					final TIMTextElem textElem = (TIMTextElem) elem;
					String elemText = textElem.getText();
					if(elemText.startsWith("sendPeer")){
						String[] arr = elemText.split("#");
						//当move没有内容，实际上得到的长度会等于2.不存在arr[2]，既越界
						//所以要加一个判断
						Intent intent = new Intent(Constants.SEND_PEER);
						intent.putExtra("down", arr[1]);
						if(arr.length>2){
							intent.putExtra("move", arr[2]);
						}else{
							intent.putExtra("move","");
						}
						sendBroadcast(intent);
					}else if(elemText.equals(Constants.INVITE_DRAW)){
						Log.d(TAG, "OPEN pad");
						Intent intent = new Intent(MsgChatService.this, SyncPadActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
//						if (null != mIncomingDlg){  // 关闭遗留来电对话框
//							mIncomingDlg.dismiss();
//						}
//						mIncomingDlg = new AlertDialog.Builder(MsgChatService.this)
//								.setTitle("有人邀请你打开画板")
//								.setMessage("邀请朋友："+ senderName)
//								.setPositiveButton("Accept", new DialogInterface.OnClickListener(){
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										timUtil.sendCode(Constants.REFUSE_DRAW);
//									}
//								})
//								.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										Intent intent = new Intent(MsgChatService.this, SyncPadActivity.class);
//										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//										startActivity(intent);
//									}
//								})
//								.create();
//						mIncomingDlg.setCanceledOnTouchOutside(false);
//						mIncomingDlg.show();

					}else if(elemText.equals(Constants.CANCEL_DRAW)){
						if (null != mIncomingDlg){  // 关闭遗留来电对话框
							mIncomingDlg.dismiss();
						}
					}else if(elemText.equals(Constants.REFUSE_DRAW )){
						Intent intent = new Intent(Constants.REFUSE_DRAW);
						sendBroadcast(intent);
					}else if(elemText.equals(Constants.CLOSE_DRAW)){
						Intent intent = new Intent(Constants.CLOSE_DRAW);
						sendBroadcast(intent);
					}else if(elemText.equals(Constants.OPEN_MIC_SPEAKER)){
						Intent intent = new Intent(Constants.OPEN_MIC_SPEAKER);
						sendBroadcast(intent);
					}else if(elemText.equals(Constants.CLOSE_MIC_SPEAKER)){
						Intent intent = new Intent(Constants.CLOSE_MIC_SPEAKER);
						sendBroadcast(intent);
					}
				}else if(elem.getType().equals(TIMElemType.Image)){
					TIMImageElem imageElem = (TIMImageElem) elem;
					ArrayList<TIMImage> timImages = imageElem.getImageList();
					for(TIMImage timImage : timImages){
						timImage.getImage(new TIMValueCallBack<byte[]>() {
							@Override
							public void onError(int i, String s) {

							}

							@Override
							public void onSuccess(byte[] bytes) {
								//getElement(1)一定是text类型的，我就不去做判断了
								TIMElem elem = timmsg.getElement(1);
								final TIMTextElem comment = (TIMTextElem) elem;
								String context = comment.getText();
								String[] arr = context.split(" ");
								Log.d(TAG, "CONTEXT" + context);
								for(String s : arr){
									Log.d(TAG, s);
								}
								Intent intent = new Intent(Constants.SEND_PEER_DRAW);
								intent.putExtra("sendPeerDraw", bytes);
								intent.putExtra("widthPercent", arr[0]);
								intent.putExtra("heightPercent", arr[1]);
								sendBroadcast(intent);
							}
						});
					}
				}
			}
			return false;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		removeMessageListener();
	}

	private void setForceLogout() {
		TIMManager.getInstance().setUserStatusListener(
				new TIMUserStatusListener() {
					@Override
					public void onForceOffline() {

					}

					@Override
					public void onUserSigExpired() {

					}
				}
		);
	}

	private void setMessageListener() {
		TIMManager.getInstance().addMessageListener(msgListener);
	}

	private void removeMessageListener(){
		TIMManager.getInstance().removeMessageListener(msgListener);
	}

}
