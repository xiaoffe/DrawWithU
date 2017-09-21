package com.xiaoffe.drawwithu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by codeest on 16/8/11.
 * 无MVP的Fragment基类
 */
//放弃了使用原先的SupportActivity和SupportFragment。不然，使用CoordinatorTabLayout会有问--20170901
public abstract class SimpleFragment extends Fragment {

    protected View mView;
//    protected Activity mActivity;
    protected AppCompatActivity mActivity;
    protected Context mContext;
    //Unbinder mUnBinder = ButterKnife.bind(this, view); 什么作用自己猜
    private Unbinder mUnBinder;
//    protected boolean isInited = false;

    //当ViewPager用setAdapter（extends FragmentPagerAdapter）时，Fragment的声明周期会完成此操作。持有Activity
    @Override
    public void onAttach(Context context) {
        //这样持有Activity不会出问题？？好像不是好的写法。。170821 am0:59
        mActivity = (AppCompatActivity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        initEventAndData();
    }

//    @Override
//    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
//        super.onLazyInitView(savedInstanceState);
//        isInited = true;
//        initEventAndData();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    protected abstract int getLayoutId();
    protected abstract void initEventAndData();
}
