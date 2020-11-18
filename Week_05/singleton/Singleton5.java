package com.transformers.singleton;

/**
 * 枚举实现
 * 枚举本身是单例，由JVM根本上提供保障，避免通过反射和反序列化漏洞，无延迟加载。
 */
public enum Singleton5 {
    INSTANCE;
}
