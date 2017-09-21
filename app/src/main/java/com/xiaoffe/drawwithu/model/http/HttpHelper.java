package com.xiaoffe.drawwithu.model.http;


import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;

import java.util.List;

import io.reactivex.Flowable;

/**
 */

public interface HttpHelper {

    Flowable<WelcomeBean> fetchWelcomeInfo(String res);

    Flowable<MyHttpResponse<VersionBean>> fetchVersionInfo();

    Flowable<List<ZhuangbiImage>> getZhuangBiInfo(String query);
}
