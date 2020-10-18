作业说明：
第2题，执行如下命令
javac HelloXlassClassLoader.java
java HelloXlassClassLoader
或
javac HelloXlassClassLoader2.java
java HelloXlassClassLoader2

学习笔记
1.Java是二进制跨平台语言，基于javac生成的字节码及不同系统的JVM共同实现。
2.字节码分四类：栈操作指令、程序流程控制指令、对象操作指令、算术运算及类型转换指令。
3.类加载器
3.1类生命周期
	加载、链接（验证、准备、解析）、初始化、使用、卸载
	访问静态字段时，不会触发该类加载。
3.2启动、扩展、应用类加载器，双亲委派、负责依赖、缓存加载
4.JMM
	线程独有的：虚拟机栈（线程栈，包含栈帧）、本地方法栈、程序计数器
	线程共享的：堆、方法区
	堆：年轻代（新生代、存活区S0/S1）、老年代
	非堆：Metaspace（jdk8之前的持久代）、CCS（压缩类空间，保存class信息）、CodeCache（存放JIT编译器编译后本地机器代码）。
5.JVM启动参数
以-开头的标准参数
-D设置系统属性
-X非标准参数
-XX非稳定参数，+-Flags/key=value
