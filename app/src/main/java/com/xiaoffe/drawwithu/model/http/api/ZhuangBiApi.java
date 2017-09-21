package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import java.util.List;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by codeest on 16/10/10.
 * https://github.com/codeestX/my-restful-api
 */

public interface ZhuangBiApi {
	String HOST = "http://www.zhuangbi.info/";
	
    @GET("search")
    Flowable<List<ZhuangbiImage>> getZhuangBiInfo(@Query("q") String query);
}
