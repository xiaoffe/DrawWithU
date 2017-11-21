package com.xiaoffe.drawwithu.model.http.api;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApkApi {
    String HOST = "http://gzq.test.szgps.net:88/";
    @Streaming
    @GET("update/CRP-mobile-android.apk")
    Flowable<ResponseBody> downloadApk();
}
