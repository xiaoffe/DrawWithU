package com.xiaoffe.drawwithu.ui.main.activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.karumi.expandableselector.ExpandableItem;
import com.karumi.expandableselector.ExpandableSelector;
import com.karumi.expandableselector.OnExpandableItemClickListener;
import com.tencent.callsdk.ILVBCallMemberListener;
import com.tencent.callsdk.ILVCallConfig;
import com.tencent.callsdk.ILVCallListener;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVIncomingListener;
import com.tencent.callsdk.ILVIncomingNotification;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.BaseActivity;
import com.xiaoffe.drawwithu.base.contract.main.SyncPadContract;
import com.xiaoffe.drawwithu.presenter.main.SyncPadPresenter;
import com.xiaoffe.drawwithu.util.PixUtil;
import com.xiaoffe.drawwithu.widget.doodle.Doodle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/9/4.
 */

public class SyncPadActivity extends BaseActivity<SyncPadPresenter>
        implements SyncPadContract.View,ILVIncomingListener, ILVCallListener, ILVBCallMemberListener {
    private static final String TAG = "syncpad";
    @BindView(R.id.mDoodle)
    Doodle mDoodle;
    @BindView(R.id.es_sizes)
    ExpandableSelector sizesExpandableSelector;
    @BindView(R.id.es_colors)
    ExpandableSelector colorsExpandableSelector;
    @BindView(R.id.es_backward)
    ImageView button;
    @BindView(R.id.iv_test)
    ImageView buttonTest;
    @BindView(R.id.bt_colors)
    Button colorsHeaderButton;
    @Inject
    PixUtil pixUtil;
    private String sendPeer;
    private Doodle.OnDrawListener mDrawListener = new Doodle.OnDrawListener() {
        //传屏幕百分比
        float x;
        float y;
        String type;
        int size;
        int color;

        @Override
        public void onDown(MotionEvent event) {
//            x = pixUtil.px2dip(event.getRawX());
//            y = pixUtil.px2dip(event.getRawY());
            x = pixUtil.getWidthPercent(event.getRawX());
            y = pixUtil.getHeightPercent(event.getRawY());
            type = mDoodle.getMyType().toString();
            size = mDoodle.getMySize();
            color = mDoodle.getMyColor();
            String downContent = x + " " + y + " " + type + " " + size + " " + color;
            sendPeer = "sendPeer";
            sendPeer += "#";
            sendPeer += downContent;
            sendPeer += "#";
        }

        @Override
        public void onMove(MotionEvent event) {
//            x = pixUtil.px2dip(event.getRawX());
//            y = pixUtil.px2dip(event.getRawY());
            x = pixUtil.getWidthPercent(event.getRawX());
            y = pixUtil.getHeightPercent(event.getRawY());
            String moveContent = x + " " + y + "@";
            sendPeer += moveContent;
        }

        @Override
        public void onUp(MotionEvent event) {
            mPresenter.sendCode(sendPeer);
        }
    };
    private boolean callConnected = false;
    private boolean enableMic = false;
    //ILVIncomingListener
    @Override
    public void onCallEstablish(int callId) {
        Log.d(TAG, "onCallEstablish");
        callConnected = true;
        buttonTest.setBackgroundResource(R.mipmap.tv_turn_down);
    }

    @Override
    public void onCallEnd(int callId, int endResult, String endInfo) {
        Log.d(TAG, "onCallEnd" + endResult + " " + endInfo);
        callConnected = false;
        buttonTest.setBackgroundResource(R.mipmap.tv_turn_up);
        mPresenter.closePad();
        finish();
    }

    @Override
    public void onException(int iExceptionId, int errCode, String errMsg) {
        Log.d(TAG, "onException" + errCode + " " + errMsg);
    }
    //ILVBCallMemberListener
    //170912 发现这里的回调不起作用（onMicEvent）
    //所以现在换成IM的方式来进行通知。。。诶
    @Override
    public void onCameraEvent(String id, boolean bEnable) {

    }

    @Override
    public void onMicEvent(String id, boolean bEnable) {

    }

    @Override
    public void onMemberEvent(String id, boolean bEnter) {

    }
    //ILVIncomingListener
    @Override
    public void onNewIncomingCall(int callId, int callType, ILVIncomingNotification notification) {
        mPresenter.acceptCall(callId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        ILVCallManager.getInstance().onResume();
        registerReceiver();
        super.onResume();
    }

    @Override
    protected void onPause() {
        ILVCallManager.getInstance().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ILVCallManager.getInstance().removeIncomingListener(this);
        ILVCallManager.getInstance().removeCallListener(this);
        ILVCallManager.getInstance().onDestory();
        mDoodle.recycleBitmap();
        unRegisterReceiver();
        super.onDestroy();
        //mDoodle.recycleBitmap(); 放这里会crash
    }

    @Override
    public void onBackPressed() {
        if(callConnected){
            mPresenter.endCall();
        }else{
            mPresenter.closePad();
            super.onBackPressed();
        }
    }

    @Override
    public void dummy() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_syncpad;
    }

    @Override
    protected void initEventAndData() {
        initColorExpSelector();
        initSizeExpSelector();
        mDoodle.setMySize(4);
        mDoodle.setOnDrawListener(mDrawListener);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SyncPadActivity.this);
            }
        });
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!callConnected){
                    //todo:对方不在线时，多次点击这里会有bug，小问题先跳过
                    mPresenter.makeCall();
                }else{
                    mPresenter.switchMicSpeaker(enableMic);
                    switchMic(enableMic);//不写在Presenter里了
                }
            }
        });

        ILVCallManager.getInstance().init(new ILVCallConfig()
                .setAutoBusy(true));
        // 设置通话回调
        ILVCallManager.getInstance().addIncomingListener(this);
        ILVCallManager.getInstance().addCallListener(this);
    }

    private void switchMic(boolean b){
        enableMic = b;
        ILVCallManager.getInstance().enableMic(enableMic);
        ILVCallManager.getInstance().enableSpeaker(enableMic);
        if(enableMic){
            buttonTest.setBackgroundResource(R.mipmap.tv_turn_down);
        }else{
            buttonTest.setBackgroundResource(R.mipmap.tv_turn_up);
        }
        enableMic = !enableMic;
    }


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDoodle.onTouchEvent(event);
    }

    //想要做mDoodle.recycleBitmap();操作，必须放到super.onDestroy();后面去
    //因为super.onDestroy（）最终会调用App.getInstance().removeActivity(this);和 mUnBinder.unbind();
    //特别是mUnBinder.unbind();操作，使得@BindView绑定的mDoodle，已经解绑（字面意思）
    //再对mDoodle操作就会crash

    private void initColorExpSelector() {
        List<ExpandableItem> expandableItems = new ArrayList<>();
        expandableItems.add(new ExpandableItem(R.drawable.item_brown));
        expandableItems.add(new ExpandableItem(R.drawable.item_green));
        expandableItems.add(new ExpandableItem(R.drawable.item_orange));
        expandableItems.add(new ExpandableItem(R.drawable.item_pink));
        expandableItems.add(new ExpandableItem(R.drawable.item_light_red));
        expandableItems.add(new ExpandableItem(R.drawable.item_light_green));
        expandableItems.add(new ExpandableItem(R.drawable.item_light_yellow));
        expandableItems.add(new ExpandableItem(R.drawable.item_light_blue));
        colorsExpandableSelector.showExpandableItems(expandableItems);
        colorsHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                colorsHeaderButton.setVisibility(View.INVISIBLE);
                colorsExpandableSelector.expand();
            }
        });
        colorsExpandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
            @Override public void onExpandableItemClickListener(int index, View view) {
                colorsHeaderButton.setVisibility(View.VISIBLE);
                switch (index) {
                    case 0:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_brown);
                        mDoodle.setMyColor("#FF4B2D29");
                        break;
                    case 1:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_green);
                        mDoodle.setMyColor("#FF42543C");
                        break;
                    case 2:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_orange);
                        mDoodle.setMyColor("#FFE29B33");
                        break;
                    case 3:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_pink);
                        mDoodle.setMyColor("#FFE9C7C7");
                        break;
                    case 4:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_light_red);
                        mDoodle.setMyColor("#FFCC0000");
                        break;
                    case 5:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_light_green);
                        mDoodle.setMyColor("#ff00CC00");
                        break;
                    case 6:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_light_yellow);
                        mDoodle.setMyColor("#FFFFCC00");
                        break;
                    default:
                        colorsHeaderButton.setBackgroundResource(R.drawable.item_light_blue);
                        mDoodle.setMyColor("#ff6699ff");
                        break;
                }
                colorsExpandableSelector.collapse();
            }
        });
    }
    private void initSizeExpSelector(){
        List<ExpandableItem> expandableItems = new ArrayList<>();
        expandableItems.add(new ExpandableItem("中"));
        expandableItems.add(new ExpandableItem("细"));
        expandableItems.add(new ExpandableItem("宽"));
        sizesExpandableSelector.showExpandableItems(expandableItems);
        sizesExpandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
            @Override public void onExpandableItemClickListener(int index, View view) {
                String title = sizesExpandableSelector.getExpandableItem(index).getTitle();
                if(title.equals("细")){
                    mDoodle.setMySize(2);
                }else if(title.equals("中")){
                    mDoodle.setMySize(4);
                }else if(title.equals("宽")){
                    mDoodle.setMySize(6);
                }
                switch (index) {
                    case 1:
                        ExpandableItem firstItem = sizesExpandableSelector.getExpandableItem(1);
                        swipeFirstItem(1, firstItem);
                        break;
                    case 2:
                        ExpandableItem secondItem = sizesExpandableSelector.getExpandableItem(2);
                        swipeFirstItem(2, secondItem);
                        break;
                    default:
                }
                sizesExpandableSelector.collapse();
            }

            private void swipeFirstItem(int position, ExpandableItem clickedItem) {
                ExpandableItem firstItem = sizesExpandableSelector.getExpandableItem(0);
                sizesExpandableSelector.updateExpandableItem(0, clickedItem);
                sizesExpandableSelector.updateExpandableItem(position, firstItem);
            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Constants.SEND_PEER.equals(action)){
                String downContent = intent.getStringExtra("down");
                String moveContent = intent.getStringExtra("move");
                Log.d(TAG, downContent + "..." + moveContent);

                String[] downContentArr = downContent.split(" ");
//                float peerX = pixUtil.dip2px(Float.parseFloat(downContentArr[0]));
//                float peerY = pixUtil.dip2px(Float.parseFloat(downContentArr[1]));
                float peerX = pixUtil.getWidthPix(Float.parseFloat(downContentArr[0]));
                float peerY = pixUtil.getHeightPix(Float.parseFloat(downContentArr[1]));
                Doodle.ActionType peerType = Doodle.ActionType.valueOf(downContentArr[2]);
                int peerSize = Integer.parseInt(downContentArr[3]);
                int peerColor = Integer.parseInt(downContentArr[4]);
                mDoodle.downPeer(peerType, peerSize, peerColor, peerX, peerY);
                if(!moveContent.equals("")){
                    String[] moveContentArr = moveContent.split("@");
                    for(String s : moveContentArr){
                        if(s!=null){
                            String[] arr = s.split(" ");
//                            float moveX = pixUtil.dip2px(Float.parseFloat(arr[0]));
//                            float moveY = pixUtil.dip2px(Float.parseFloat(arr[1]));
                            float moveX = pixUtil.getWidthPix(Float.parseFloat(arr[0]));
                            float moveY = pixUtil.getHeightPix(Float.parseFloat(arr[1]));
                            mDoodle.justMovePeer(moveX, moveY);
                        }
                    }
                }
                mDoodle.upPeer();
            }else if(Constants.SEND_PEER_DRAW.equals(action)){
                byte[] bytes = intent.getByteArrayExtra("sendPeerDraw");
                Bitmap peerBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.d(TAG, "(intent.getFloatExtra(\"widthPercent\", 0)" + Float.parseFloat(intent.getStringExtra("widthPercent")));
                Log.d(TAG, "intent.getFloatExtra(\"heightPercent\", 0)" + Float.parseFloat(intent.getStringExtra("heightPercent")));
                float showWidth = pixUtil.getWidthPix(Float.parseFloat(intent.getStringExtra("widthPercent")));
                float showHeight = pixUtil.getHeightPix(Float.parseFloat(intent.getStringExtra("heightPercent")));
                Log.d(TAG, " showWidh " + showWidth + " showHeight " + showHeight);
                mDoodle.drawPic(peerBitmap, showWidth ,showHeight);
            }else if(Constants.CLOSE_DRAW.equals(action) || Constants.REFUSE_DRAW.equals(action)){
                finish();
            }else if(Constants.OPEN_MIC_SPEAKER.equals(action)){
                switchMic(true);
            }else if(Constants.CLOSE_MIC_SPEAKER.equals(action)){
                switchMic(false);
            }
        }
    };

    private void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        //画画相关
        intentFilter.addAction(Constants.SEND_PEER);
        intentFilter.addAction(Constants.SEND_PEER_DRAW);
        //电话相关
        intentFilter.addAction(Constants.REFUSE_DRAW);
        intentFilter.addAction(Constants.CLOSE_DRAW);
        intentFilter.addAction(Constants.OPEN_MIC_SPEAKER);
        intentFilter.addAction(Constants.CLOSE_MIC_SPEAKER);
        //
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unRegisterReceiver(){
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Bitmap bitmap = decodeUri(SyncPadActivity.this, result.getUri(), 500, 500);
                Observable.timer(5*100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                mDoodle.drawPic(bitmap);
                                saveAndSendBitmap(bitmap);
                            }
                        });
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 读取一个缩放后的图片，限定图片大小，避免OOM
     * @param uri       图片uri，支持“file://”、“content://”
     * @param maxWidth  最大允许宽度
     * @param maxHeight 最大允许高度
     * @return  返回一个缩放后的Bitmap，失败则返回null
     */
    public static Bitmap decodeUri(Context context, Uri uri, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只读取图片尺寸
        resolveUri(context, uri, options);

        //计算实际缩放比例
        int scale = 1;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if ((options.outWidth / scale > maxWidth &&
                    options.outWidth / scale > maxWidth * 1.4) ||
                    (options.outHeight / scale > maxHeight &&
                            options.outHeight / scale > maxHeight * 1.4)) {
                scale++;
            } else {
                break;
            }
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;//读取图片内容
        options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改
        Bitmap bitmap = null;
        try {
            bitmap = resolveUriForBitmap(context, uri, options);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static void resolveUri(Context context, Uri uri, BitmapFactory.Options options) {
        if (uri == null) {
            return;
        }

        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
                ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream stream = null;
            try {
                stream = context.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(stream, null, options);
            } catch (Exception e) {
                Log.w("resolveUri", "Unable to open content: " + uri, e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.w("resolveUri", "Unable to close content: " + uri, e);
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            Log.w("resolveUri", "Unable to close content: " + uri);
        } else {
            Log.w("resolveUri", "Unable to close content: " + uri);
        }
    }

    private static Bitmap resolveUriForBitmap(Context context, Uri uri, BitmapFactory.Options options) {
        if (uri == null) {
            return null;
        }

        Bitmap bitmap = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
                ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream stream = null;
            try {
                stream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(stream, null, options);
            } catch (Exception e) {
                Log.w("resolveUriForBitmap", "Unable to open content: " + uri, e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.w("resolveUriForBitmap", "Unable to close content: " + uri, e);
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
        } else {
            Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
        }

        return bitmap;
    }

    public void saveAndSendBitmap(Bitmap mBitmap) {
        File filePic;

        try {
            filePic = new File(Constants.CHOOSE_PIC_PATH + "temp.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            float width = mBitmap.getWidth();
            float height = mBitmap.getHeight();
            mPresenter.sendBitmap(width, height);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
