#### 1.使用Netty客户端

netty-assignment文件夹，使用Netty客户端代替老师例子中的HttpClient，之前没接触过Netty，不知道哪里的问题，压测时差距很大。

后端服务为http://localhost:8803，使用BIO多线程版本（HttpServer03）启动提供，压测数据如下：

```bash
$ sb -u http://localhost:8803 -c 40 -N 60
Starting at 2020/11/4 16:22:55
[Press C to stop the test]
104373  (RPS: 1641.9)
---------------Finished!----------------
Finished at 2020/11/4 16:23:59 (took 00:01:03.6548129)
Status 200:    103400
Status 303:    973

RPS: 1708.6 (requests/second)
Max: 497ms
Min: 0ms
Avg: 22.1ms

  50%   below 20ms
  60%   below 20ms
  70%   below 21ms
  80%   below 21ms
  90%   below 24ms
  95%   below 41ms
  98%   below 42ms
  99%   below 43ms
99.9%   below 54ms
```

通过实现的网关，压测数据惨不忍睹

```bash
$ sb -u http://localhost:8888 -c 40 -N 60
Starting at 2020/11/4 16:25:18
[Press C to stop the test]
968     (RPS: 9.4))
---------------Finished!----------------
Finished at 2020/11/4 16:27:01 (took 00:01:43.6583197)
970     (RPS: 9.4)                      Status 200:    964
Status 303:    6

RPS: 9.6 (requests/second)
Max: 100038ms
Min: 26ms
Avg: 847.4ms

  50%   below 181ms
  60%   below 215ms
  70%   below 267ms
  80%   below 339ms
  90%   below 454ms
  95%   below 610ms
  98%   below 880ms
  99%   below 1097ms
99.9%   below 100038ms
1004    (RPS: 9.7)
```

控制台报错如下：

```bash
十一月 04, 2020 4:25:33 下午 io.netty.handler.logging.LoggingHandler exceptionCaught
信息: [id: 0x360e7cfc, L:/127.0.0.1:55723 - R:localhost/127.0.0.1:8803] EXCEPTION: java.io.IOException: 远程主机强迫关闭了一个现有的连接。
java.io.IOException: 远程主机强迫关闭了一个现有的连接。
	at sun.nio.ch.SocketDispatcher.read0(Native Method)
	at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:43)
	at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
	at sun.nio.ch.IOUtil.read(IOUtil.java:192)
	at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:378)
	at io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:253)
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1133)
	at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:350)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:148)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)

十一月 04, 2020 4:25:33 下午 io.netty.channel.DefaultChannelPipeline onUnhandledInboundException
警告: An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.
java.io.IOException: 远程主机强迫关闭了一个现有的连接。
	at sun.nio.ch.SocketDispatcher.read0(Native Method)
	at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:43)
	at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
	at sun.nio.ch.IOUtil.read(IOUtil.java:192)
	at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:378)
	at io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:253)
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1133)
	at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:350)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:148)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)
```



#### 2.在请求头中增加自定义请求头信息

nio02文件夹，在老师例子中增加自定义请求头信息。





