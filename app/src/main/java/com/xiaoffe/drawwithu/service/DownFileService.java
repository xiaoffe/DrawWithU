package com.xiaoffe.drawwithu.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.app.App;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.di.component.DaggerServiceComponent;
import com.xiaoffe.drawwithu.di.module.ServiceModule;
import com.xiaoffe.drawwithu.model.DataManager;
import com.xiaoffe.drawwithu.model.bean.DownLoadBean;
import com.xiaoffe.drawwithu.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DownFileService extends IntentService {
    //回头可再看看我是如何让Servcie也可以注入DataManager的
    @Inject
    protected DataManager mDataManager;
    private static final String TAG = "DownloadApi";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    int downloadCount = 0;
    private DownloadCompleteReceiver receiver;
    public static final String MESSAGE_PROGRESS = "message_progress";
    public DownFileService() {
        super("DownloadApi");
        DaggerServiceComponent.builder()
                .appComponent(App.getAppComponent())
                .serviceModule(new ServiceModule(this))
                .build()
                .inject(this);
    }

    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MESSAGE_PROGRESS)) {
                DownLoadBean download = intent.getParcelableExtra("download");
                if (download.getCurrentFileSize() == download.getTotalFileSize()) {
                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                }
                DownFileService.this.stopSelf();
            }
        }
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//     通知栏样式设定
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//     通知初始化
        receiver = new DownloadCompleteReceiver();
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("下载")
                .setContentText("正在下载应用")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());
        download();
    }

    private void download() {
        DownProgressListener listener = new DownProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //不频繁发送通知，防止通知栏下拉卡顿
                int progress = (int) ((bytesRead * 100) / contentLength);
                if ((downloadCount == 0) || progress > downloadCount) {
                    DownLoadBean download = new DownLoadBean();
                    download.setTotalFileSize(contentLength);
                    download.setCurrentFileSize(bytesRead);
                    download.setProgress(progress);
                    //此处注释掉后只执行一次，但是进度不再显示了
                    sendNotification(download);
                }
            }
        };
        mDataManager.downloadApk()
//                .compose(RxUtil.<ResponseBody>rxSchedulerHelper())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(@NonNull ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .subscribe(new Consumer<InputStream>() {
                    @Override
                    public void accept(@NonNull InputStream in) throws Exception {
                        String path = Constants.APK_PATH+"test.apk";
                        try {
                            File apkFile = new File(path);
                            if (!apkFile.getParentFile().exists()) {
                                apkFile.getParentFile().mkdirs();
                            }
                            if(apkFile != null && apkFile.exists()){
                                apkFile.delete();
                            }
                            FileOutputStream out = null;
                            byte[] buffer = new byte[4096];
                            long readSize = 0;
                            out = new FileOutputStream(apkFile);
                            int len = -1;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                                readSize += len;
                                Log.d(TAG, "progress-----------"+ readSize);
                            }
                            out.flush();
                            out.close();
                            in.close();
                        } catch (IOException e) {
                            Log.d(TAG, "exception: " + e);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    private void downloadCompleted(String msg) {
        DownLoadBean download = new DownLoadBean();
        download.setProgress(100);
        sendIntent(download);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText(msg);
        notificationManager.notify(0, notificationBuilder.build());
        notificationManager.cancel(0);
    }

    private void sendNotification(DownLoadBean download) {
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
                StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendIntent(DownLoadBean download) {
        Intent intent = new Intent(MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownFileService.this).sendBroadcast(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
