package com.transformers.advanced.week04;

/**
 * 使用wait阻塞main线程，当计算完毕后
 * 使用时间：163 ms
 */
public class HomeworkWaitNotify {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        final Object obj = new Object();
        // 在这里创建一个线程或线程池
        final int[] ret = new int[1];
        Thread thread = new Thread(() -> {
            ret[0] = sum();
            synchronized (obj) {
                obj.notifyAll();
            }
        });
        thread.start();

        // 异步执行 下面方法
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //这是得到的返回值
        int result = ret[0];

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
