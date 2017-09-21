package com.xiaoffe.drawwithu.util;

import android.util.Log;

import com.xiaoffe.drawwithu.app.App;
/**
 * Created by xiaoluo on 2017/1/19.
 */
public class PixUtil {
    private static final String TAG = "pix";
    private App app;
    public PixUtil(App app){
        this.app = app;
    }

    public float dip2px(float dpValue) {
        final float scale = app.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
    public int px2dip(float pxValue){
        final float scale = app.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);//+0.5是为了向上取整
    }
    //用上面两个方法没有达到屏幕适配的效果。所以改为传百分比
    public float getWidthPercent(float value){
        final float width = app.getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, " " +  value/width);
        return value/width;
    }

    public float getHeightPercent(float value){
        final float height = app.getResources().getDisplayMetrics().heightPixels;
        Log.d(TAG, " " +  value/height);
        return value/height;
    }

    public float getWidthPix(float percent){
        return app.getResources().getDisplayMetrics().widthPixels*percent;
    }

    public float getHeightPix(float percent){
        return app.getResources().getDisplayMetrics().heightPixels*percent;
    }
}
