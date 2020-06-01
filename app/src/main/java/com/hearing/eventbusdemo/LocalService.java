package com.hearing.eventbusdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hearing.eventbusdemo.eventbus.EventWrapper;
import com.hearing.eventbusdemo.eventbus.IEventCallback;
import com.hearing.eventbusdemo.eventbus.IEventInterface;
import com.hearing.eventbusdemo.eventbus.MyEventBus;
import com.hearing.eventbusdemo.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import static com.hearing.eventbusdemo.util.Debug.TAG;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
public class LocalService extends Service {

    private final RemoteCallbackList<IEventCallback> mRemoteCallbackList = new RemoteCallbackList<>();
    private Binder mBinder = new IEventInterface.Stub() {
        @Override
        public void register(IEventCallback callback) throws RemoteException {
            Log.d(TAG, "LocalService register: " + callback);
            mRemoteCallbackList.register(callback);
        }

        @Override
        public void unregister(IEventCallback callback) throws RemoteException {
            Log.d(TAG, "LocalService unregister: " + callback);
            mRemoteCallbackList.unregister(callback);
        }

        @Override
        public void notify(Bundle event) throws RemoteException {
            Log.d(TAG, "LocalService notify: " + event);
            // 主进程收到子进程的事件后，通过EventBus转发给主进程的订阅者
            MyEventBus.getInstance().postSingle(MyEventBus.getInstance().unPack(event));
        }
    };

    // 桥梁：监听主进程发送的事件，并转发给子进程
    @Subscribe
    public void handle(EventWrapper wrapper) {
        Log.v(TAG, "LocalService handle: " + wrapper);
        synchronized (mRemoteCallbackList) {
            int n = mRemoteCallbackList.beginBroadcast();
            try {
                for (int i = 0; i < n; i++) {
                    // 转发给子进程
                    mRemoteCallbackList.getBroadcastItem(i).notifyEvent(wrapper.mBundle);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mRemoteCallbackList.finishBroadcast();
        }
    }

    // 主进程的事件订阅，在接收到主进程的事件后，发给主进程的消息在这里处理，发给子进程的消息在handle方法中转发
    @Subscribe
    public void onEvent(String event) {
        Log.d(TAG, "LocalService onEvent: " + event);
    }

    // 主进程的事件订阅，在接收到主进程的事件后，发给主进程的消息在这里处理，发给子进程的消息在handle方法中转发
    @Subscribe
    public void onEvent1(Integer event) {
        Log.d(TAG, "LocalService onEvent: " + event);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyEventBus.getInstance().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.sleep(1000);
                // 主进程发送事件
                MyEventBus.getInstance().post("A message from main process at " + System.currentTimeMillis());
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        MyEventBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
