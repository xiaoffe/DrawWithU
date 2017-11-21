package com.xiaoffe.drawwithu.di.module;

import android.util.Log;

import com.activeandroid.query.Select;
import com.xiaoffe.drawwithu.BuildConfig;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.di.qualifier.ApkUrl;
import com.xiaoffe.drawwithu.di.qualifier.LoginUrl;
import com.xiaoffe.drawwithu.di.qualifier.MyUrl;
import com.xiaoffe.drawwithu.di.qualifier.RegisterUrl;
import com.xiaoffe.drawwithu.di.qualifier.UploadFaceUrl;
import com.xiaoffe.drawwithu.di.qualifier.VersionCodeUrl;
import com.xiaoffe.drawwithu.di.qualifier.ZhihuUrl;
import com.xiaoffe.drawwithu.di.qualifier.ZhuangBiUrl;
import com.xiaoffe.drawwithu.model.bean.User;
import com.xiaoffe.drawwithu.model.http.api.ApkApi;
import com.xiaoffe.drawwithu.model.http.api.LoginApi;
import com.xiaoffe.drawwithu.model.http.api.MyApis;
import com.xiaoffe.drawwithu.model.http.api.RegisterApi;
import com.xiaoffe.drawwithu.model.http.api.UploadFaceApi;
import com.xiaoffe.drawwithu.model.http.api.VersionCodeApi;
import com.xiaoffe.drawwithu.model.http.api.ZhihuApis;
import com.xiaoffe.drawwithu.model.http.api.ZhuangBiApi;
import com.xiaoffe.drawwithu.model.prefs.ImplPreferencesHelper;
import com.xiaoffe.drawwithu.util.SystemUtil;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Version;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by codeest on 2017/2/26.
 */
//@Module用于标注提供依赖的类。
@Module
public class HttpModule {

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    @ZhihuUrl
    Retrofit provideZhihuRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, ZhihuApis.HOST);
    }

    @Singleton
    @Provides
    @MyUrl
    Retrofit provideMyRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, MyApis.HOST);
    }

    ////
    @Singleton
    @Provides
    @ZhuangBiUrl
    Retrofit provideZhuangBiRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, ZhuangBiApi.HOST);
    }

    @Singleton
    @Provides
    @LoginUrl
    Retrofit provideLoignRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, LoginApi.HOST);
    }

    @Singleton
    @Provides
    @RegisterUrl
    Retrofit provideRegisterRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, RegisterApi.HOST);
    }

    @Singleton
    @Provides
    @UploadFaceUrl
    Retrofit provideUploadFaceRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, UploadFaceApi.HOST);
    }

    @Singleton
    @Provides
    @ApkUrl
    Retrofit provideApkRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, ApkApi.HOST);
    }

    @Singleton
    @Provides
    @VersionCodeUrl
    Retrofit provideVersionCodeApiRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, VersionCodeApi.HOST);
    }
    // 这里有一个坑要注意。。。我花了半天的时间爬坑！！！170927
    // 为了在header里添加token，我首先要获取保存在SharedPreference里面的lastAccount
    // 于是尝试了多次， 直接用@Inject的方式，将DataManager/ImplPreferencesHelper,注入成为成员Field的形式
    // 但是，都不成功。这个成员使用时候发现，是NULL。。
    // 后来发现在这个OkHttpClient provideClient（）里将ImplPreferencesHelper做为参数加入，就能达到目的（如同注入成功）
    // ps：但是加入参数是DataManager的话，连rebuild project都不能成功。原因在深层次。先不深究。。
    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder, ImplPreferencesHelper implPreferencesHelper) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!SystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };

        /**
        *  看能否给请求头加token170923
        * */

        String account = implPreferencesHelper.getLastAccount();
        if(account == null){
            Log.d("header", "impl, null");
        }else{
            Log.d("header", " xxxxxxxxxxxxxxxx" + account);
        }
//        User user = new Select().from(User.class).where("username="+account).executeSingle();//这个写法错误的
        User user = new Select().from(User.class).where("username=?",account).executeSingle();
        final String tokenString;
        if(user!=null){
            tokenString = user.token;
            Log.d("header", "user:"+user.username+" token:"+tokenString);
        }else{
            tokenString="dummy_token";
        }
        Interceptor token = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("Authorization","Bearer " + tokenString) // header("Authorization", "Bearer " + user.authToken)
                        .build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(token);
        //...................
//        Interceptor tokenInerceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Charset UTF8 = Charset.forName("UTF-8");
//
//                Request request = chain.request();
//                // try the request
//                Response originalResponse = chain.proceed(request);
//                /**通过如下的办法曲线取到请求完成的数据
//                 *
//                 * 原本想通过  originalResponse.body().string()
//                 * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
//                 *
//                 * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
//                 */
//                ResponseBody responseBody = originalResponse.body();
//                BufferedSource source = responseBody.source();
//                source.request(Long.MAX_VALUE); // Buffer the entire body.
//                Buffer buffer = source.buffer();
//                Charset charset = UTF8;
//                MediaType contentType = responseBody.contentType();
//                if (contentType != null) {
//                    charset = contentType.charset(UTF8);
//                }
//                String bodyString = buffer.clone().readString(charset);
//                Log.d("httpHeader", bodyString);
//                /***************************************/
////                if (response shows expired token){//根据和服务端的约定判断token过期
////                    //取出本地的refreshToken
////                    String refreshToken = "sssgr122222222";
////                    // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
////                    ApiService service = ServiceManager.getService(ApiService.class);
////                    Call<String> call = service.refreshToken(refreshToken);
////                    //要用retrofit的同步方式
////                    String newToken = call.execute().body();
////                    // create a new request and modify it accordingly using the new token
////                    Request newRequest = request.newBuilder().header("token", newToken)
////                            .build();
////                    // retry the request
////                    originalResponse.body().close();
////                    return chain.proceed(newRequest);
////                }
//                // otherwise just pass the original response on
//                return originalResponse;
//            }
//        };
//        builder.addInterceptor(tokenInerceptor);
        /**
         *  看能否给请求头加token170923
         * */
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        return builder.build();
    }

    @Singleton
    @Provides
    ZhihuApis provideZhihuService(@ZhihuUrl Retrofit retrofit) {
        return retrofit.create(ZhihuApis.class);
    }

    @Singleton
    @Provides
    MyApis provideMyService(@MyUrl Retrofit retrofit) {
        return retrofit.create(MyApis.class);
    }

    @Singleton
    @Provides
    ZhuangBiApi provideZhuangBiService(@ZhuangBiUrl Retrofit retrofit) {
        return retrofit.create(ZhuangBiApi.class);
    }

    @Singleton
    @Provides
    LoginApi provideLoginService(@LoginUrl Retrofit retrofit) {
        return retrofit.create(LoginApi.class);
    }

    @Singleton
    @Provides
    RegisterApi provideRegisterService(@RegisterUrl Retrofit retrofit) {
        return retrofit.create(RegisterApi.class);
    }

    @Singleton
    @Provides
    UploadFaceApi provideUploadFaceService(@UploadFaceUrl Retrofit retrofit) {
        return retrofit.create(UploadFaceApi.class);
    }

    @Singleton
    @Provides
    ApkApi provideApkService(@ApkUrl Retrofit retrofit) {
        return retrofit.create(ApkApi.class);
    }

    @Singleton
    @Provides
    VersionCodeApi provideVersionCodeService(@VersionCodeUrl Retrofit retrofit) {
        return retrofit.create(VersionCodeApi.class);
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
