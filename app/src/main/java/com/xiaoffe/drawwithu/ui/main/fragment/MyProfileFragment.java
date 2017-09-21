package com.xiaoffe.drawwithu.ui.main.fragment;

import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.base.RootFragment;
import com.xiaoffe.drawwithu.base.contract.fragment.MyProfileContract;
import com.xiaoffe.drawwithu.model.bean.MyProfileBean;
import com.xiaoffe.drawwithu.presenter.fragment.MyProfilePresenter;


/**
 * Created by Administrator on 2017/9/1.
 */

public class MyProfileFragment extends RootFragment<MyProfilePresenter> implements MyProfileContract.View {
    private final String TAG = "profile";

    @Override
    public void showContent(MyProfileBean info) {
        //do nothing
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myprofile;
        }
        }