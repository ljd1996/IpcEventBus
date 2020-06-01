// IEventInterface.aidl
package com.hearing.eventbusdemo.eventbus;

import com.hearing.eventbusdemo.eventbus.IEventCallback;
import android.os.Bundle;

interface IEventInterface {
    // 子进程向主进程注册事件回调
    void register(IEventCallback callback);
    // 子进程解注册
    void unregister(IEventCallback callback);
    // 子进程向主进程通知事件
    void notify(in Bundle event);
}
