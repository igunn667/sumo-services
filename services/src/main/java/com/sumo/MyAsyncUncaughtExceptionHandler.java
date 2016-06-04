package com.sumo;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        System.out.println("Method Name::"+method.getName());
        System.out.println("Exception occurred::"+ ex);
    }
}
