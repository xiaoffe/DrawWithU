package com.xiaoffe.drawwithu.model.http;

import com.xiaoffe.drawwithu.model.bean.LoginBean;
import com.xiaoffe.drawwithu.model.bean.LoginBeanTest;
import com.xiaoffe.drawwithu.model.bean.VersionBean;
import com.xiaoffe.drawwithu.model.bean.WelcomeBean;
import com.xiaoffe.drawwithu.model.bean.ZhuangbiImage;
import com.xiaoffe.drawwithu.model.http.api.ApkApi;
import com.xiaoffe.drawwithu.model.http.api.LoginApi;
import com.xiaoffe.drawwithu.model.http.api.MyApis;
import com.xiaoffe.drawwithu.model.http.api.RegisterApi;
import com.xiaoffe.drawwithu.model.http.api.UploadFaceApi;
import com.xiaoffe.drawwithu.model.http.api.VersionCodeApi;
import com.xiaoffe.drawwithu.model.http.api.ZhihuApis;
import com.xiaoffe.drawwithu.model.http.api.ZhuangBiApi;
import com.xiaoffe.drawwithu.model.http.response.MyHttpResponse;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by codeest on 2016/8/3.
 */
public class RetrofitHelper implements HttpHelper {

    private ZhihuApis mZhihuApiService;
    private MyApis mMyApiService;
    private ZhuangBiApi mZhuangBiService;

    private LoginApi mLoginApi;
    private RegisterApi mRegisterApi;
    private UploadFaceApi uploadFaceApi;
    private ApkApi apkApi;
    private VersionCodeApi versionCodeApi;
    @Inject
    public RetrofitHelper(ZhihuApis zhihuApiService, MyApis myApiService, ZhuangBiApi zhuangBiApi,
                          LoginApi loginApi, RegisterApi registerApi, UploadFaceApi uploadFaceApi,
                          ApkApi apkApi, VersionCodeApi versionCodeApi) {
        this.mZhihuApiService = zhihuApiService;
        this.mMyApiService = myApiService;
        this.mZhuangBiService = zhuangBiApi;
        this.mLoginApi = loginApi;
        this.mRegisterApi = registerApi;
        this.uploadFaceApi = uploadFaceApi;
        this.apkApi = apkApi;
        this.versionCodeApi = versionCodeApi;
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

    @Override
    public Flowable<LoginBean> getLoginInfo(String account, String password) {
        LoginBeanTest loginBeanTest = new LoginBeanTest();
        loginBeanTest.userName = account;
        loginBeanTest.password = password;
        return mLoginApi.getLoginInfo(loginBeanTest);
//        return mLoginApi.getLoginInfo(account, password);
    }

    @Override
    public Flowable<LoginBean> register(String account, String password) {
        return mRegisterApi.register(account, password);
    }

    @Override
    public Flowable<LoginBeanTest> uploadFace(String action, String username, String imgPath) {
//        RequestBody actionBody = RequestBody.create(MediaType.parse("text/plain"), action);
//        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
        //img file
        File file = new File(imgPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("applicaiton/otcet-stream"), file);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return uploadFaceApi.uploadFace(action,username,requestFile);
//        return uploadFaceApi.uploadFace("http://gzq.test.szgps.net:88/tlsuser.php?action=newSetUserIcon&username=" + username,requestFile);

    }

    @Override
    public Flowable<ResponseBody> downloadApk() {
        return apkApi.downloadApk();
    }

    @Override
    public Flowable<VersionBean> getVersionCode(String query) {
        return versionCodeApi.getVersionCode(query);
    }
}
