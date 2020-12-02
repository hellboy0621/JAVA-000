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
