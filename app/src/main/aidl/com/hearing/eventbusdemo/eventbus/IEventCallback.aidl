// IEventCallback.aidl
package com.hearing.eventbusdemo.eventbus;

import android.os.Bundle;

interface IEventCallback {
    // 主进程向子进程发送消息
    void notifyEvent(in Bundle event);
}
