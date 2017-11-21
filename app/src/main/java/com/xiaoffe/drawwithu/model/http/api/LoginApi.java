package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.LoginBean;
import com.xiaoffe.drawwithu.model.bean.LoginBeanTest;
import com.xiaoffe.drawwithu.model.bean.User;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApi {
	String HOST = "http://106.14.173.93/";

//    @FormUrlEncoded
//    @POST("api/login/")
//    Flowable<LoginBean> getLoginInfo(@Field("userName") String account, @Field("password") String password);

    @POST("api/login/")
    Flowable<LoginBean> getLoginInfo(@Body LoginBeanTest body);

    //上面这两种写法都正确。。171001
}
