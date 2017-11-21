package com.xiaoffe.drawwithu.ui.main.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.RootFragment;
import com.xiaoffe.drawwithu.base.contract.fragment.MyProfileContract;
import com.xiaoffe.drawwithu.model.bean.MyProfileBean;
import com.xiaoffe.drawwithu.presenter.fragment.MyProfilePresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2017/9/1.
 */

public class MyProfileFragment extends RootFragment<MyProfilePresenter> implements MyProfileContract.View {
    private final String TAG = "profile";

    @BindView(R.id.my_avatar)
    CircleImageView mAvatar;
    @Override
    public void showContent(MyProfileBean info) {
        //do nothing

    }
    @Override
    protected void initEventAndData() {
        mPresenter.loadAvatar("http://gzq.test.szgps.net:88/upload/usericon/15700071533.jpg");
        mPresenter.checkPermissions(new RxPermissions(MyProfileFragment.this.getActivity()));
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myprofile;
        }

    @OnClick({R.id.my_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_avatar:
                showChoosePicDialog();
                break;
        }
    }

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private Bitmap mBitmap;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileFragment.this.getContext());
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        //将后面的参数从1改为0.1之后呢，就成为任意比例了20170724
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            mAvatar.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码...
            saveBitmap(mBitmap);
            uploadAvatar();//for test

        }
    }
    public void saveBitmap(Bitmap mBitmap) {
        File filePic;

        try {
            Log.d(TAG, "here1111");
            filePic = new File(Constants.CUT_PIC_PATH + "temp.jpg");
            if (!filePic.exists()) {
                Log.d(TAG, "here22222");
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Log.d(TAG, "here3333333");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "here4444444");
            e.printStackTrace();
        }finally {
            mBitmap.recycle();
        }
    }
    private void uploadAvatar(){
        Log.d(TAG, "here555555555");
        mPresenter.uploadAvatar("newSetUserIcon", "15700071533", Constants.CUT_PIC_PATH + "temp.jpg");
        TIMFriendshipManager.getInstance().setFaceUrl(Constants.CUT_PIC_PATH + "temp.jpg", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "set TIM face onError" + s);
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "set TIM face onSuccess");
            }
        });
    }

    @Override
    public void loadAvatar(String url) {
        Glide.with(getActivity())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mAvatar);
    }

}
