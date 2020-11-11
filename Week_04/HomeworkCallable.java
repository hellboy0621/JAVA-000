package com.transformers.advanced.week04;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用Callable和FutureTask实现
 * 使用时间：162 ms
 */
public class HomeworkCallable {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            return sum();
        });
        // 异步执行 下面方法
        new Thread(futureTask).start();


        int result = 0;
        try {
            //这是得到的返回值
            result = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

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
