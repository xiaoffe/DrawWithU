package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.LoginBean;
import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterApi {
	String HOST = "http://106.14.173.93/";
    //注意POST的时候不要用Field
    @FormUrlEncoded
    @POST("api/register/")
    Flowable<LoginBean> register(@Field("userName") String account, @Field("password") String password);
}
