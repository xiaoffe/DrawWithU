package com.xiaoffe.drawwithu.ui.main.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.base.BaseActivity;
import com.xiaoffe.drawwithu.base.contract.main.WelcomeContract;
import com.xiaoffe.drawwithu.component.ImageLoader;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.presenter.main.WelcomePresenter;
import com.xiaoffe.drawwithu.service.DownFileService;

import java.util.List;

import butterknife.BindView;

/**
 * Created by codeest on 16/8/15.
 */
//在这个项目里，没有看到，在Activity里面，直接以field的方式来@Inject注入依赖的。
public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements WelcomeContract.View {
    //hehe
    private final String TAG = "welcome";
    @BindView(R.id.iv_welcome_bg)
    ImageView ivWelcomeBg;
    @BindView(R.id.tv_welcome_author)
    TextView tvWelcomeAuthor;

    int REQUEST_EXTERNAL_STORAGE = 1;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initEventAndData() {
        int permission = ActivityCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    WelcomeActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        //登录
        mPresenter.tryLogin();
        //获取最新apk的版本号
        mPresenter.getVersionCode();
        //下载apk
        Intent intent = new Intent(WelcomeActivity.this, DownFileService.class);
//        startService(intent);
//    mPresenter.downloadApk();
    }

    @Override
    public void showContent(List<ZhuangbiImage> welcomeBean) {
//        ImageLoader.load(this, welcomeBean.getImg(), ivWelcomeBg);
//        ivWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
//        tvWelcomeAuthor.setText(welcomeBean.getText());
        Log.d(TAG, "showContent");
        ImageLoader.load(this, welcomeBean.get(0).image_url, ivWelcomeBg);
        ivWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
        tvWelcomeAuthor.setText(welcomeBean.get(0).description);
    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        Glide.clear(ivWelcomeBg);
        super.onDestroy();
    }

    @Override
    public void jumpToLogin() {
        Intent intent = new Intent();
        intent.setClass(this,LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
