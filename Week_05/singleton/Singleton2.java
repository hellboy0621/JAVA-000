package com.transformers.singleton;

/**
 * 懒汉式
 */
public class Singleton2 {

    private static Singleton2 instance;
    
    private static boolean initFlag = false;

    private Singleton2() {
        /**
         * 防止反射漏洞
         */
        if (!initFlag) {
            initFlag = true;
        } else {
            throw new IllegalArgumentException("Cannot reflectively create singleton objects");
        }
    }

    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

    /**
     * 防止反序列化漏洞
     *
     * @return
     */
    public Object readResolve() {
        return instance;
    }

}
