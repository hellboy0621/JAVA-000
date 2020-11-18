package com.transformers.singleton;

/**
 * 双重检测锁
 * DCL(Double Check Lock)
 */
public class Singleton3 {

    private static volatile Singleton3 instance;

    private static boolean initFlag = false;

    private Singleton3() {
        /**
         * 防止反射漏洞
         */
        if (!initFlag) {
            initFlag = true;
        } else {
            throw new IllegalArgumentException("Cannot reflectively create singleton objects");
        }
    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
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
