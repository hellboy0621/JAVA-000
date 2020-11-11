package com.transformers.advanced.week04;

import java.util.concurrent.locks.LockSupport;

/**
 * 使用LockSupport的park和unpark机制实现
 * 使用时间：104 ms
 */
public class HomeworkLockSupport {

    static class MyThread extends Thread {
        private int[] ret;
        private Thread mainThread;

        public MyThread(int[] ret, Thread mainThread) {
            this.ret = ret;
            this.mainThread = mainThread;
        }

        @Override
        public void run() {
            ret[0] = sum();
            LockSupport.unpark(mainThread);
        }
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池
        final int[] ret = new int[1];
        Thread thread = new Thread(new MyThread(ret, Thread.currentThread()));
        thread.start();

        // 异步执行 下面方法
        LockSupport.park();

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
