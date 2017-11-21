package com.xiaoffe.drawwithu.model.http;


import com.xiaoffe.drawwithu.model.bean.LoginBean;
import com.xiaoffe.drawwithu.model.bean.LoginBeanTest;
import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;
import java.util.List;
import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Query;

/**
 */

public interface HttpHelper {

    Flowable<WelcomeBean> fetchWelcomeInfo(String res);

    Flowable<MyHttpResponse<VersionBean>> fetchVersionInfo();

    Flowable<List<ZhuangbiImage>> getZhuangBiInfo(String query);

    Flowable<LoginBean> getLoginInfo(String account, String password);

    Flowable<LoginBean> register(String account, String password);

//    Flowable<ResponseBody> uploadFace(String description, String imgPath);
    Flowable<LoginBeanTest> uploadFace(String action, String username, String imgPath);

    Flowable<ResponseBody> downloadApk();

    Flowable<VersionBean> getVersionCode(String query);
}