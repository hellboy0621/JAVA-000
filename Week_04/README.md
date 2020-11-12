## 必做作业题一：思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？

HomeworkCallable

- 使用Callable和FutureTask实现
- 使用时间：162 ms

HomeworkCountLatch

 * 使用CountLatch实现
 * 使用时间：183 ms

HomeworkJoin

 * 使用join方法阻塞main线程，等待thread1执行完成后，从数组中获取结果。
 * 使用时间：170 ms

HomeworkLockSupport

 * 使用LockSupport的park和unpark机制实现
 * 使用时间：104 ms

HomeworkWaitNotify

 * 使用wait阻塞main线程，当计算完毕后
 * 使用时间：163 ms

HomeworkYield

 * 判断当前执行线程数量大于2时，yield让出CPU时间片，直到运算完毕，活动线程数量为2时，继续执行
 * 使用时间：190 ms



## 必做作业题二：把多线程和并发相关知识带你梳理一遍，画一个脑图

详见[Java多线程和并发.xmind]文件