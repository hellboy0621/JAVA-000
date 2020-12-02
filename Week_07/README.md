

class13

  2、（必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。   

表结构：

```mysql
CREATE TABLE `t_order`
(
    `order_id` INT,
    `user_id`  INT,
    `status`   VARCHAR(10),
    `column1`  VARCHAR(10),
    `column2`  VARCHAR(10),
    `column3`  VARCHAR(10)
);
```

使用原生JDBC插入数据，代码如下：

```java
// Constant.java
public interface Constant {
    String DRIVER = "com.mysql.cj.jdbc.Driver";
    String URL = "jdbc:mysql://127.0.0.1:3305/test?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";
    String USER_NAME = "root";
    String PASSWORD = "";
}
```



```java
// JdbcDemo.java
public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName(Constant.DRIVER);
    Connection conn = DriverManager.getConnection(Constant.URL, Constant.USER_NAME, Constant.PASSWORD);
    insert(conn);
    conn.close();
}

public static void insert(Connection conn) {
    String sql = "insert into t_order(order_id, user_id, status, column1, column2, column3) values(?,?,?,?,?,?) ";
    try {
        conn.setAutoCommit(false);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        long start = System.currentTimeMillis();
        System.out.println("start");
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                pstmt.setInt(1, i + j);
                pstmt.setInt(2, i + j);
                String str = UUID.randomUUID().toString().substring(0, 9);
                pstmt.setString(3, str);
                pstmt.setString(4, str);
                pstmt.setString(5, str);
                pstmt.setString(6, str);
                pstmt.addBatch();
            }
            // pstmt.executeBatch();
            // conn.commit();
        }
        pstmt.executeBatch();
        conn.commit();
        long end = System.currentTimeMillis();
        System.out.println("all cost " + (end - start) + "ms");
    } catch (SQLException e) {
        e.printStackTrace();
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
```

每1000条数据提交一次，耗时99680ms。

100w条数据，构造完毕后，一次性执行，耗时101082ms。



------



class14



2、（必做）读写分离-动态切换数据源版本1.0

代码详见springboot-multi-datasource。

3、（必做）读写分离-数据库框架版本2.0

代码详见springboot-shardingsphere。



接口测试见各自项目下的Test.http文件。



建表语句

```mysql
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(127) NOT NULL DEFAULT '',
  `age` int(11) NOT NULL DEFAULT '0',
  `mark` varchar(127) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- 主库
insert into t_user(name, age, mark)
values
('Smith', 18, 'master'),
('John', 19, 'master'),
('Hello', 88, 'master');

-- 从库
insert into t_user(name, age, mark)
values
('Smith', 18, 'slave'),
('John', 19, 'slave'),
('Hello', 88, 'slave');
```

