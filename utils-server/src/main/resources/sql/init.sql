
-- 下表要放入到默认数据源中的数据库里才行

CREATE TABLE `t_datasource`
(
    `id`              int(11)                                                       NOT NULL,
    `key`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '绑定的key,用于数据源的切换',
    `url`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '数据库连接地址',
    `username`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库用户名',
    `password`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库密码',
    `driverClassName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库驱动',
    `pool`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库连接池类型: com.alibaba.druid.pool.DruidDataSource,com.zaxxer.hikari.HikariDataSource,..',
    `type`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '数据库类型:  mysql ,oracle,..',
    `state`           int(2)                                                        NOT NULL COMMENT '是否可用: 1可用 ,2不可用',
    PRIMARY KEY (`id`),
    UNIQUE KEY `key` (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;