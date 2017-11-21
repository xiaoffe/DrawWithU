package com.xiaoffe.drawwithu.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.base.BaseActivity;
import com.xiaoffe.drawwithu.base.SimpleFragment;
import com.xiaoffe.drawwithu.base.contract.main.MainContract;
import com.xiaoffe.drawwithu.presenter.main.MainPresenter;
import com.xiaoffe.drawwithu.service.MsgChatService;
import com.xiaoffe.drawwithu.ui.main.fragment.FragmentFactory;
import com.xiaoffe.drawwithu.util.SnackbarUtil;
import com.xiaoffe.drawwithu.widget.ChangeColorIconWithText;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by codeest on 16/8/9.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{
    private static final String  TAG = "main";
    @BindView(R.id.content)
    RelativeLayout layout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab_online_people)
    ChangeColorIconWithText onlinePeople;
    @BindView(R.id.tab_my_package)
    ChangeColorIconWithText myPackage;
    //左边的抽屉
    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * 由于recreate 需要特殊处理夜间模式
     * @param savedInstanceState
     */
    //onCreate会调用super.onCreate, 然后调用initEventAndData()
    //所以这里先看initEventAndData()，再看onCreate（）
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();//可能放在onCreate里更好
    }

    @Override
    protected void initEventAndData() {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SimpleFragment baseFragment = FragmentFactory.getFragment(position);
                resetOthresTab();
                mTabIndicators.get(position).setIconAlpha(1.0f);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabIndicators.add(onlinePeople);
        mTabIndicators.add(myPackage);
        mTabIndicators.get(0).setIconAlpha(1.0f);

        mNavigationView.getMenu().findItem(R.id.drawer_zhihu).setChecked(false);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_zhihu:
                        //for test add friend
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_gank:
                        break;
                    case R.id.drawer_wechat:
                        break;
                    case R.id.drawer_gold:

                        break;
                }
                mNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                return false;
            }
        });
        mPresenter.checkNetworkState();
    }

    private void showExitDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出GeekNews吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                App.getInstance().exitApp();
            }
        });
        builder.show();
    }
    // 像Dilaog这种，与数据获得无关的东西。不放到MVP的P里面去。
    // 也不需要用@Inject去放到MainActivity里依赖吗？那是否使用@Inject依赖的判断标准是什么？
    // 然而在别的项目栗子里面看到过，将Toast写成Util，在Activity里直接依赖的。。。（当然跟MVP无关）
    @Override
    public void showUpdateDialog(String versionContent) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("检测到新版本!");
        builder.setMessage(versionContent);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkPermissions();
            }
        });
        builder.show();
    }

    @Override
    public void startDownloadService() {

    }

    public void checkPermissions() {
        mPresenter.checkPermissions(new RxPermissions(this));
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.getInstance().exitApp();
    }

    @OnClick({ R.id.tab_online_people, R.id.tab_my_package })
    public void clickTab(ChangeColorIconWithText tab) {
        resetOthresTab();

        switch(tab.getId()){
            case R.id.tab_online_people:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.tab_my_package:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                viewPager.setCurrentItem(1, false);
                break;
            default:
                break;
        }
    }

    private void resetOthresTab() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    private void startService(){
        Intent intent = new Intent(MainActivity.this, MsgChatService.class);
        startService(intent);
    }

    @Override
    public void showNetworkConnected() {
        SnackbarUtil.dismiss();
    }

    @Override
    public void showNetworkDisconnected() {
        SnackbarUtil.show(layout, "网络连接不上/(ㄒoㄒ)/~~222");
    }
}
//  君さえいれば
//　君さえいれば、どんな勝負も勝ち続ける、暗闇を切り裂くようにi　need　your　love
//  数センチのズレを重ねて、偶然は運命になる
//  屈託なく笑う声に、免じて四次元の会話も慣れて、つい引き込まれてゆく
//　君さえいれば、遥かな道も越えて行ける
//  どんな時も守り抜く
//　大地の水は、透き通る花を咲かせる糧
//　暗闇を、切り裂くようにI　need　your　love
//  生まれ変わっても、そんなセリフ
//  こだわりは僕の弱さ
//  ちょっと逃げ腰だとしても、変わらぬ、愛は誓える、簡単じゃない二人だからいい
//  美しいのは壊れかけたと知ってるから、キスで互いを隠して
//  水平線の先に明日があるとしたら、波風も悪くはないにiｍ　no　match　for　you
//  君さえいれば、どんな勝負も勝ち続ける、どんな時も守り抜く
//  大地の水は透き通る花を、咲かせる糧、暗闇を切り裂くようにi　need　you　love
//　ありふれた言葉を並べて　lalala