package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.VersionBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VersionCodeApi {
//    http://gzq.test.szgps.net:88/loadapp.php?action=getVersion
    String HOST = "http://gzq.test.szgps.net:88/";
    @GET("loadapp.php")
    Flowable<VersionBean> getVersionCode(@Query("action") String query);
}
