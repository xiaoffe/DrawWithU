package com.xiaoffe.drawwithu.model.http;

import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.model.http.api.MyApis;
import com.xiaoffe.drawwithu.model.http.api.ZhihuApis;
import com.xiaoffe.drawwithu.model.http.api.ZhuangBiApi;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by codeest on 2016/8/3.
 */
public class RetrofitHelper implements HttpHelper {

    private ZhihuApis mZhihuApiService;
//    private GankApis mGankApiService;
//    private WeChatApis mWechatApiService;
    private MyApis mMyApiService;
//    private GoldApis mGoldApiService;
//    private VtexApis mVtexApiService;
    private ZhuangBiApi mZhuangBiService;

    @Inject
    public RetrofitHelper(ZhihuApis zhihuApiService, MyApis myApiService, ZhuangBiApi zhuangBiApi) {
        this.mZhihuApiService = zhihuApiService;
//        this.mGankApiService = gankApiService;
//        this.mWechatApiService = wechatApiService;
        this.mMyApiService = myApiService;
//        this.mGoldApiService = goldApiService;
//        this.mVtexApiService = vtexApiService;
        this.mZhuangBiService = zhuangBiApi;
    }

    @Override
    public Flowable<WelcomeBean> fetchWelcomeInfo(String res) {
        return mZhihuApiService.getWelcomeInfo(res);
    }

    @Override
    public Flowable<MyHttpResponse<VersionBean>> fetchVersionInfo() {
        return mMyApiService.getVersionInfo();
    }

    @Override
    public Flowable<List<ZhuangbiImage>> getZhuangBiInfo(String query) {
        return mZhuangBiService.getZhuangBiInfo(query);
    }
}
