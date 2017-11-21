package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.FriendBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FriendListApi {
    String HOST = "http://106.14.173.93/";
	//梁说不用传username来识别返回谁，header里有token就ok
    @GET("friends")
    Flowable<List<FriendBean>> getFriendList(@Query("page") String page);
}
