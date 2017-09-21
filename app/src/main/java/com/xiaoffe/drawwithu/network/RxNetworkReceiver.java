package com.xiaoffe.drawwithu.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.xiaoffe.drawwithu.util.RxUtil;
import org.reactivestreams.Publisher;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;

/**
 * Created by prashant on 8/9/16.
 */
public class RxNetworkReceiver {
    public static Flowable<Boolean> stream(final Context context){
        final IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        final Flowable<Boolean> stateStream =
                Flowable.create(new OnSubscribeBroadcastRegister(context, intentFilter,null,null), BackpressureStrategy.BUFFER)
                .compose(new FlowableTransformer<Intent, Boolean>() {
                    @Override
                    public Publisher<Boolean> apply(Flowable<Intent> upstream) {
                        return RxUtil.createData(checkConnectivityStatus(context.getApplicationContext()));
                    }
                });
        return stateStream.startWith(checkConnectivityStatus(context)).distinctUntilChanged();
    }

    public static boolean checkConnectivityStatus(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return null!= networkInfo && networkInfo.isConnected();
    }

    private static class OnSubscribeBroadcastRegister implements FlowableOnSubscribe<Intent> {
        @Override
        public void subscribe(final FlowableEmitter<Intent> emitter) throws Exception {
            final BroadcastReceiver broadCastReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    emitter.onNext(intent);
                }
            };
            context.registerReceiver(broadCastReceiver, intentFilter, permission, schedulerHandler);
        }
        private final Context context;
        private final IntentFilter intentFilter;
        private final String permission;
        private final Handler schedulerHandler;

        public OnSubscribeBroadcastRegister(Context context, IntentFilter intentFilter, String permission, Handler schedulerHandler) {
            this.context = context;
            this.intentFilter = intentFilter;
            this.permission = permission;
            this.schedulerHandler = schedulerHandler;
        }
    }
}
