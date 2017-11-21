package com.xiaoffe.drawwithu.model.http.api;

import com.xiaoffe.drawwithu.model.bean.LoginBeanTest;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

//public interface UploadFaceApi {
////    String HOST = "http://gzq.test.szgps.net:88/tlsuser.php?action=newSetUserIcon&username=" + username;
//    String HOST = "http://gzq.test.szgps.net:88/";
//    @Multipart
//    @POST("tlsuser.php")
////    Flowable<ResponseBody> uploadFace(@Part("description") RequestBody description, @Part MultipartBody.Part img);
//    //下面这个@Field不行，还得用@Part
////    Flowable<ResponseBody> uploadFace(@Field("action") String newSetUserIcon, @Field("username") String username, @Part MultipartBody.Part img);
////    Flowable<ResponseBody> uploadFace(@Part("action") RequestBody action, @Part("username") RequestBody username, @Part MultipartBody.Part img);
//    Flowable<LoginBeanTest> uploadFace(@Query("action") String action, @Query("username") String username, @Part MultipartBody.Part img);
//}

//最后再试试这个(成功♪(^∇^*)！！ 20171021)
public interface UploadFaceApi {
    //    String HOST = "http://gzq.test.szgps.net:88/tlsuser.php?action=newSetUserIcon&username=" + username;
    String HOST = "http://gzq.test.szgps.net:88/";
//    @Multipart
    @POST("tlsuser.php")
    Flowable<LoginBeanTest> uploadFace(@Query("action") String action, @Query("username") String username, @Body RequestBody img);
}
//下面的注释是笔记。。。。。 上面的注释是痕迹。。。
//网上给的很多栗子都是：
//public interface UploadFaceApi {
//    String HOST = "http://gzq.test.szgps.net:88/";
//    @Multipart
//    @POST("tlsuser.php")
//    Flowable<ResponseBody> uploadFace(@Part("action") RequestBody action, @Part("username") RequestBody username, @Part MultipartBody.Part img);
//}
//或者是
//public interface UploadFaceApi {
//    String HOST = "http://gzq.test.szgps.net:88/";
//    @Multipart
//    @POST
//    Flowable<ResponseBody> uploadFace(@Url String url, @Part MultipartBody.Part img);
//}
// 既都是用@Multipart和@Part来表示支持文件上传的表单栗子，这种可以图文一起传递（传递方式multipart/form-data），而且可以实现多文件上传。

// 但是我用的reeman的接口，只适合用以"applicaiton/otcet-stream"的方式上传单独一个文件。所以锐曼的接口太不灵活。
// 试了很多次，最后上面的写法才能成功。（真心麻蛋）---20171021