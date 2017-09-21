package com.xiaoffe.drawwithu.ui.main.fragment;

import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.SimpleFragment;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/17.
 */

public class FragmentFactory {
    private static final String TAG = "FragmentFactory";
    public static HashMap<Integer, SimpleFragment> tempFragment = new HashMap<>();

    public static SimpleFragment getFragment(int position){
        SimpleFragment mFragment = tempFragment.get(position);
        if (mFragment != null) {
            return mFragment;
        }
        switch(position){
            case Constants.FragmentFactorConstant.FRAGMENT_ONLINES:
                mFragment = new OnlinesFragment();
                break;
            case Constants.FragmentFactorConstant.FRAGMENT_MYPROFILE:
                mFragment = new MyProfileFragment();
                break;
            default:
                mFragment = new OnlinesFragment();
                break;
        }
        tempFragment.put(position, mFragment);
        //返回需要的界面
        return mFragment;
    }
}
