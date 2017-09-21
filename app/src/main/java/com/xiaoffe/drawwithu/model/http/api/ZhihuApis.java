package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.WelcomeBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by codeest on 2016/8/2.
 * 知乎APIs
 */
public interface ZhihuApis {

    String HOST = "http://news-at.zhihu.com/api/4/";

    /**
     * 启动界面图片
     */
    @GET("start-image/{res}")
    Flowable<WelcomeBean> getWelcomeInfo(@Path("res") String res);

//
}
