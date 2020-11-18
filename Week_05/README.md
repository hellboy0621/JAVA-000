

#### class09-1.使用Java里的动态代理，实现一个简单的AOP

代码详见spring01项目下com.transformers.homework.aop包

#### class09-2.写代码实现Spring Bean的装配，方式越多越好（XML、Annotation都可以）,提交到Github。

代码详见spring01项目下com.transformers.homework.bean包

其中xml配置方式有setter、构造函数、静态工厂方法3种；

注解方式有@Autowired和@Resource2种。

#### class09-3.实现一个Spring XML自定义配置，配置一组Bean，例如Student/Klass/School。

代码详见spring-xml。



------



#### class10-1. （选做）总结一下，单例的各种写法，比较它们的优劣。

代码详见singleton文件夹，共5种实现方式。

#### class10-2.（选做）maven/spring的profile机制，都有什么用法？

对于SpringBoot
多环境配置
**properties配置文件**
使用properties配置文件实现多环境配置，只能通过添加多个application-{profile}.properties来实现。
比如：application-dev.properties,application-test.properties
**YAML配置文件**
使用YAML实现多环境配置要简单的多，只需要一个文件即可，application.yml
在文件中使用---来区分多个环境，每个环境都需要配置spring.profile属性，不配置的属于默认环境
激活profiles
可以在命令行参数、系统参数、application.properties等处进行配置

```bash
命令行
java -jar xxx.jar --spring.profiles.active=dev --debug=true

application.properties
spring.profiles.active=dev
```

对于maven
maven的profile用法有许多种，但基本原理就是根据激活环境的不同，自定义字段被赋予不同的值。

在resources文件夹下，分别创建dev、prod、test3个文件夹，文件夹里分别有一个application.properties文件。

在pom.xml文件中配置如下

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
    <resources>
        <resource>
            <directory>src/main/resources/</directory>
            <!--打包时先排除掉三个文件夹-->
            <excludes>
                <exclude>dev</exclude>
                <exclude>prod</exclude>
                <exclude>test</exclude>
            </excludes>
            <includes>
                <!--如果有其他定义通用文件，需要包含进来-->
                <include>static/*</include>
                <include>templates/*</include>
            </includes>
        </resource>
        <resource>
            <!--这里是关键！ 根据不同的环境，把对应文件夹里的配置文件打包-->
            <directory>src/main/resources/${profiles.active}</directory>
        </resource>
    </resources>
</build>
<profiles>
    <profile>
        <!--不同环境Profile的唯一id-->
        <id>dev</id>
        <properties>
            <!--profiles.active是自定义的字段，自定义字段可以有多个-->
            <profiles.active>dev</profiles.active>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <profiles.active>prod</profiles.active>
        </properties>
        <!--activation用来指定激活方式，可以根据jdk环境，环境变量，文件的存在或缺失-->
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <profiles.active>test</profiles.active>
        </properties>
    </profile>
</profiles>
```

激活方式
1.通过maven命令参数

```bash
mvn clean package -Ptest
```

2.通过pom文件里的activation属性
3.settings.xml中使用activeProfiles指定（了解即可，不推荐）
mave目录下的settings.xml也可以添加下面的代码来指定激活哪个profile。

```xml
<activeProfiles>  
     <activeProfile>profileTest1</activeProfile>  
</activeProfiles>
```



#### class10-3.给前面课程提供的Student/Klass/School实现自动配置和Starter

代码详见assignment-spring-boot-starter文件夹。



#### class10-6.（必做）研究一下JDBC接口和数据库连接池，掌握它们的设计和用法

代码详见jdbc文件夹。