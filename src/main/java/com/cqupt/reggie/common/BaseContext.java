package com.cqupt.reggie.common;

/**
 * 某一个线程之内
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrendId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrendId(){
        return threadLocal.get();
    }
}
