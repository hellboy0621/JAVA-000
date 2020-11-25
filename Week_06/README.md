

1、（选做）：基于课程中的设计原则和最佳实践，分析是否可以将自己负责的业务系统进行数据库设计或是数据库服务器方面的优化。

2、（必做）：基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL的SQL文件到Github（后面2周的作业依然要是用到这个表结构）。

```mysql
CREATE TABLE IF NOT EXISTS t_user(
	user_id INT NOT NULL AUTO_INCREMENT COMMENT '用户自增主键',
    nickname VARCHAR(45) NOT NULL COMMENT '用户昵称', 
    login_name VARCHAR(45) NOT NULL COMMENT '登录用户名',
    password VARCHAR(45) NOT NULL COMMENT '登录密码', 
    mobile VARCHAR(11) NOT NULL COMMENT '手机号',
    avatar VARCHAR(45) NOT NULL COMMENT '用户头像图片地址', 
    create_time DATETIME COMMENT '创建时间', 
    update_time DATETIME COMMENT '更新时间', 
    del TINYINT NOT NULL DEFAULT 0 COMMENT '删除标识 0(未删) 1(已删)', 
    PRIMARY KEY (user_id)
) COMMENT '用户表';

CREATE TABLE IF NOT EXISTS t_product (
    product_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品自增主键',
    name varchar(127) NOT NULL COMMENT '商品名称',
    introduction VARCHAR(255) DEFAULT NULL COMMENT '商品描述',
    picture_path varchar(255) NOT NULL COMMENT '商品图片路径',
    price INT NOT NULL COMMENT '商品价格，以分为单位',
    storage INT NOT NULL COMMENT '商品库存',
    display_order INT NOT NULL DEFAULT '100' COMMENT '显示排序，数大显示靠前',
    on_sale_flag TINYINT DEFAULT 0 COMMENT '上架标志 0(下架) 1(上架)',
    create_time DATETIME NOT NULL COMMENT '创建时间', 
    update_time DATETIME NOT NULL COMMENT '更新时间', 
    PRIMARY KEY (product_id)
) COMMENT '商品表';

CREATE TABLE IF NOT EXISTS t_order(
	order_id INT NOT NULL AUTO_INCREMENT COMMENT '订单自增主键',
    user_id INT NOT NULL COMMENT '用户ID',
    discount INT DEFAULT 0 COMMENT '总优惠，以分为单位',
    total_price INT DEFAULT 0 COMMENT '订单支付总价，以分为单位',
    remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间', 
    update_time DATETIME NOT NULL COMMENT '更新时间', 
    PRIMARY KEY (order_id)
) comment '订单表';

CREATE TABLE IF NOT EXISTS t_order_item(
	order_item_id INT NOT NULL AUTO_INCREMENT COMMENT '订单详情自增主键',
    order_id INT NOT NULL COMMENT '订单ID',
    user_id INT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_amount INT NOT NULL COMMENT '商品数量',
    product_price INT NOT NULL COMMENT '商品售价，以分为单位',
    discount INT DEFAULT 0 COMMENT '优惠，以分为单位',
    remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间', 
    update_time DATETIME NOT NULL COMMENT '更新时间', 
    PRIMARY KEY (order_item_id)
) comment '订单详情表';

```



3、（选做）：尽可能多的从“常见关系数据库”中列的清单，安装运行，并使用上一题的SQL测试简单的增删改查。

4、（选做）：基于上一题，尝试对各个数据库测试100万订单数据的增删改查性能。

5、（选做）：尝试对MySQL不同引擎下测试100万订单数据的增删改查性能。

6、（选做）：模拟1000万订单数据，测试不同方式下导入导出（数据备份还原）MySQL的速度，包括jdbc程序处理和命令行处理。思考和实践，如何提升处理效率。

7、（选做）：对MySQL配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查100万次，对比性能，生成报告。

