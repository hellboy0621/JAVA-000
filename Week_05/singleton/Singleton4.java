package com.transformers.singleton;

/**
 * 静态内部类
 */
public class Singleton4 {

    private Singleton4() {
    }

    public static Singleton4 getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final Singleton4 INSTANCE = new Singleton4();
    }
}
