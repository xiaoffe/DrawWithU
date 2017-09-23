package com.xiaoffe.drawwithu.ui.main.activity;

import android.content.Intent;
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
//        mPresenter.getWelcomeData();
        //把tryLogin（）放到获取装逼图片后面玩一下
    mPresenter.tryLogin();
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
