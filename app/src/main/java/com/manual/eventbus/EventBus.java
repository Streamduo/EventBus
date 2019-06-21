package com.manual.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus {

    private static volatile EventBus instance;
    private Map<Object, List<SubscribileMethod>> cachMap;
    private Handler handler = new Handler();

    private EventBus() {
        cachMap = new HashMap<>();
    }

    public static EventBus getDeafault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object object) {
        List<SubscribileMethod> list = cachMap.get(object);
        if (list == null) {
            list = findSubscribileMethods(object);
            cachMap.put(object, list);
        }

    }

    private List<SubscribileMethod> findSubscribileMethods(Object object) {
        List<SubscribileMethod> list = new ArrayList<>();
        Class<?> aClass = object.getClass();
        //循环找到其父类的带有Subscrbile方法
        while (aClass != null) {
            String name = aClass.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") ||
                    name.startsWith("android.")) {
                break;
            }
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Subscrbile subscrbile = method.getAnnotation(Subscrbile.class);
                if (subscrbile == null) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    //方法参数有且只能是一
                }
                ThreadMode threadMode = subscrbile.threadMode();
                SubscribileMethod subscribileMethod =
                        new SubscribileMethod(method, threadMode, parameterTypes[0]);
                list.add(subscribileMethod);
                aClass = aClass.getSuperclass();
            }
        }
        return list;
    }

    public void post(final Object bean) {
        //循环cachemap然后找到对应的bean对象的方法进行调用
        Set<Object> set = cachMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object obj = iterator.next();
            List<SubscribileMethod> list = cachMap.get(obj);
            for (final SubscribileMethod subscribileMethod : list) {
                //匹配if条件后面的对象 对象所对应的类的信息的父类或者接口
                if (subscribileMethod.getType()
                        .isAssignableFrom(bean.getClass())) {
                    switch (subscribileMethod.getmThreadMode()) {
                        case MAIN:
                            //主 - 主
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(subscribileMethod, obj, bean);
                            } else {
                                //子 - 主
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribileMethod, obj, bean);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:

                            break;
                    }
                }
            }
        }
    }

    private void invoke(SubscribileMethod subscribileMethod, Object obj, Object bean) {
        Method method = subscribileMethod.getMethod();
        try {
            method.invoke(obj, bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
