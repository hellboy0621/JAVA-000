package com.transformers.advanced.week04;

/**
 * 判断当前执行线程数量大于2时，yield让出CPU时间片，直到运算完毕，活动线程数量为2时，继续执行
 * 使用时间：190 ms
 */
public class HomeworkYield {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池
        final int[] ret = new int[1];
        Thread thread1 = new Thread(() -> {
            ret[0] = sum();
        });
        thread1.start();

        // 异步执行 下面方法
        while (Thread.activeCount() > 2) {
            Thread.yield();
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
