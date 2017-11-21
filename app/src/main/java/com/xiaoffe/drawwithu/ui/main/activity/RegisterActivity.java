package com.xiaoffe.drawwithu.ui.main.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.base.BaseActivity;
import com.xiaoffe.drawwithu.base.contract.main.RegisterContract;
import com.xiaoffe.drawwithu.presenter.main.RegisterPresenter;
import com.xiaoffe.drawwithu.util.WaitDialogUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/30.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View{
    private static String TAG = "RegisterActivity";
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cv_add)
    CardView cvAdd;
    @BindView(R.id.bt_go)
    Button btn_go;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

    //仅仅是为了加入这个原来的动画，而写入的onCreate。
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showWaitDialog() {
        WaitDialogUtil.waitDialog(this, "注册中...");
    }

    @Override
    public void closeWaitDialog() {
        WaitDialogUtil.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWaitDialog();
    }

    @Override
    public void jumpToLogin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
            startActivity(new Intent(this, LoginActivity.class), options.toBundle());
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_go:
                Log.d(TAG, "click register btn");
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                mPresenter.tryRejester(userName, password);
                break;
            case R.id.fab:
                animateRevealClose();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.mipmap.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
}
