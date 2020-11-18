
启动school-sample-app项目，访问如下接口，控制台打印日志。
http://localhost:8080/school/hello

自定义starter步骤：
1.引入依赖
2.编写配置类SpringBootConfiguration，如果需要传值，创建SpringBootPropertiesConfiguration从配置文件中读取配置
3.在META-INF/spring.factories中，开启自动配置，指向编写配置类SpringBootConfiguration类
4.【可选】在META-INF/spring.provides写上自己starter名字
5.新建测试项目school-sample-app，开发一个接口用于测试
