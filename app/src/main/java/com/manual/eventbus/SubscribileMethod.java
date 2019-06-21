package com.manual.eventbus;

import java.lang.reflect.Method;

public class SubscribileMethod {

    private Method method;

    private ThreadMode mThreadMode;

    private Class<?> type;

    public SubscribileMethod(Method method,ThreadMode mThreadMode,Class<?> type) {
        this.method = method;
        this.mThreadMode = mThreadMode;
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ThreadMode getmThreadMode() {
        return mThreadMode;
    }

    public void setmThreadMode(ThreadMode mThreadMode) {
        this.mThreadMode = mThreadMode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
