package com.transformers.advanced.week04;

import java.util.concurrent.CountDownLatch;

/**
 * 使用CountLatch实现
 * 使用时间：183 ms
 */
public class HomeworkCountLatch {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池
        CountDownLatch cdl = new CountDownLatch(1);
        final int[] ret = new int[1];
        new Thread(() -> {
            ret[0] = sum();
            cdl.countDown();
        }).start();
        // 异步执行 下面方法
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int result = ret[0]; //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }
}
