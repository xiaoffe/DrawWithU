package com.xiaoffe.drawwithu.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.base.BaseActivity;
import com.xiaoffe.drawwithu.base.contract.main.LoginContract;
import com.xiaoffe.drawwithu.presenter.main.LoginPresenter;
import com.xiaoffe.drawwithu.util.ToastUtil;
import com.xiaoffe.drawwithu.util.WaitDialogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/30.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View{
    private static String TAG = "LoginActivity";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void jumpToRegister() {
//                        getWindow().setExitTransition(null);
//                        getWindow().setEnterTransition(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
            startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
        } else {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        //do nothing
    }

    @Override
    public void showWaitDialog() {
        WaitDialogUtil.waitDialog(this, "登录中...");
    }

    @Override
    public void closeWaitDialog() {
        WaitDialogUtil.dismiss();
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                //像这种和数据获取无关的view操作，就不用放到MVP的p里面去了吧？
                jumpToRegister();
                break;
            case R.id.bt_go:
                Log.d(TAG, "click login btn");
                login();
                break;
        }
    }

    private void login(){
        String strAccount = etUsername.getText().toString();
        String strPwd =etPassword.getText().toString();

        if (TextUtils.isEmpty(strAccount) || TextUtils.isEmpty(strPwd)){
            ToastUtil.shortShow("密码或账号不能空");
            return;
        }
        //先忍住不把WaitDialogUtil写成注入依赖吧20170830
        mPresenter.tryLogin(strAccount,strPwd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWaitDialog();
    }
}
