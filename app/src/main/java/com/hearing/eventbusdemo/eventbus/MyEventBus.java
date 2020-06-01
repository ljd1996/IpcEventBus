package com.hearing.eventbusdemo.eventbus;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * @author liujiadong
 * @since 2020/5/29
 */
public class MyEventBus extends EventBus {
    private static final String KEY = "key";

    private MyEventBus() {
    }

    private static class SingleTon {
        private static MyEventBus sInstance = new MyEventBus();
    }

    public static MyEventBus getInstance() {
        return SingleTon.sInstance;
    }

    // 转发到主进程和子进程
    @Override
    public void post(Object event) {
        super.post(event);
        Bundle bundle = new Bundle();
        if (event instanceof Parcelable) {
            bundle.putParcelable(KEY, (Parcelable) event);
            super.post(new EventWrapper(bundle));
        } else if (event instanceof Serializable) {
            bundle.putSerializable(KEY, (Serializable) event);
            super.post(new EventWrapper(bundle));
        }
    }

    // 只发送到本进程
    public void postSingle(Object event) {
        super.post(event);
    }

    public Object unPack(@NonNull Bundle event) {
        return event.get(KEY);
    }
}
