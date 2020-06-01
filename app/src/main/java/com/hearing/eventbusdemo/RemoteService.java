package com.hearing.eventbusdemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
public class RemoteService extends Service {

    private IEventInterface mEventInterface;

    private IEventCallback mEventCallback = new IEventCallback.Stub() {
        @Override
        public void notifyEvent(Bundle event) throws RemoteException {
            Log.d(TAG, "RemoteService notifyEvent: " + event);
            // 收到主进程的转发后，将事件转发到本进程
            MyEventBus.getInstance().postSingle(MyEventBus.getInstance().unPack(event));
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        MyEventBus.getInstance().register(this);

        bindService(new Intent(this, LocalService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "RemoteService onServiceConnected");
                try {
                    mEventInterface = IEventInterface.Stub.asInterface(service);
                    mEventInterface.register(mEventCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "RemoteService onServiceConnected");
                try {
                    mEventInterface.unregister(mEventCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, BIND_AUTO_CREATE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.sleep(2000);
                MyEventBus.getInstance().post(100);
            }
        }).start();
    }

    // 桥梁：监听本进程发送的事件，并转发给主进程
    @Subscribe
    public void handle(EventWrapper wrapper) {
        Log.v(TAG, "RemoteService handle: " + wrapper);
        try {
            mEventInterface.notify(wrapper.mBundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 主进程的事件订阅者
    @Subscribe
    public void onEvent(String event) {
        Log.d(TAG, "RemoteService onEvent: " + event);
    }

    // 主进程的事件订阅者
    @Subscribe
    public void onEvent1(Integer event) {
        Log.d(TAG, "RemoteService onEvent: " + event);
    }

    @Override
    public void onDestroy() {
        MyEventBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
