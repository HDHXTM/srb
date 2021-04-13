-- borrow_info: table
CREATE TABLE `borrow_info`
(
    `id`               bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`          bigint     NOT NULL DEFAULT '0' COMMENT '借款用户id',
    `amount`           decimal(10, 2)      DEFAULT NULL COMMENT '借款金额',
    `period`           int                 DEFAULT NULL COMMENT '借款期限',
    `borrow_year_rate` decimal(10, 2)      DEFAULT NULL COMMENT '年化利率',
    `return_method`    tinyint             DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
    `money_use`        tinyint             DEFAULT NULL COMMENT '资金用途',
    `status`           tinyint    NOT NULL DEFAULT '0' COMMENT '状态（0：未提交，1：审核中， 2：审核通过， -1：审核不通过）',
    `create_time`      timestamp  NOT NULL COMMENT '创建时间',
    `update_time`      timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381827276345212931
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='借款信息表';

-- No native definition for element: idx_user_id (index)

-- borrower: table
CREATE TABLE `borrower`
(
    `id`                bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`           bigint                                                 NOT NULL DEFAULT '0' COMMENT '用户id',
    `name`              varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '姓名',
    `id_card`           varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '身份证号',
    `mobile`            varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '手机',
    `sex`               tinyint                                                         DEFAULT NULL COMMENT '性别（1：男 0：女）',
    `age`               tinyint                                                         DEFAULT NULL COMMENT '年龄',
    `education`         tinyint                                                         DEFAULT NULL COMMENT '学历',
    `is_marry`          tinyint(1)                                                      DEFAULT NULL COMMENT '是否结婚（1：是 0：否）',
    `industry`          tinyint                                                         DEFAULT NULL COMMENT '行业',
    `income`            tinyint                                                         DEFAULT NULL COMMENT '月收入',
    `return_source`     tinyint                                                         DEFAULT NULL COMMENT '还款来源',
    `contacts_name`     varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '联系人名称',
    `contacts_mobile`   varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '联系人手机',
    `contacts_relation` tinyint                                                         DEFAULT NULL COMMENT '联系人关系',
    `status`            tinyint                                                NOT NULL DEFAULT '0' COMMENT '状态（0：未认证，1：认证中， 2：认证通过， -1：认证失败）',
    `create_time`       timestamp                                              NOT NULL COMMENT '创建时间',
    `update_time`       timestamp                                              NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        tinyint(1)                                             NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381810983151894531
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='借款人';

-- borrower_attach: table
CREATE TABLE `borrower_attach`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `borrower_id` bigint     NOT NULL                                     DEFAULT '0' COMMENT '借款人id',
    `image_type`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '图片类型（idCard1：身份证正面，idCard2：身份证反面，house：房产证，car：车）',
    `image_url`   varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片路径',
    `image_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '图片名称',
    `create_time` timestamp  NOT NULL COMMENT '创建时间',
    `update_time` timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(1) NOT NULL                                     DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_borrower_id` (`borrower_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381810983177060355
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='借款人上传资源表';

-- No native definition for element: idx_borrower_id (index)

-- dict: table
CREATE TABLE `dict`
(
    `id`          bigint                                                  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `parent_id`   bigint                                                  NOT NULL DEFAULT '0' COMMENT '上级id',
    `name`        varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
    `value`       int                                                              DEFAULT NULL COMMENT '值',
    `dict_code`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '编码',
    `create_time` timestamp                                               NOT NULL COMMENT '创建时间',
    `update_time` timestamp                                               NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(1)                                              NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_parent_id_value` (`parent_id`, `value`) USING BTREE,
    KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 82062
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='数据字典';

-- No native definition for element: idx_parent_id (index)

-- integral_grade: table
CREATE TABLE `integral_grade`
(
    `id`             bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `integral_start` int                 DEFAULT NULL COMMENT '积分区间开始',
    `integral_end`   int                 DEFAULT NULL COMMENT '积分区间结束',
    `borrow_amount`  decimal(10, 2)      DEFAULT NULL COMMENT '借款额度',
    `create_time`    timestamp  NOT NULL COMMENT '创建时间',
    `update_time`    timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381065416822026242
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='积分等级表';

-- lend: table
CREATE TABLE `lend`
(
    `id`               bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`          bigint                                                  DEFAULT NULL COMMENT '借款用户id',
    `borrow_info_id`   bigint                                                  DEFAULT NULL COMMENT '借款信息id',
    `lend_no`          varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '标的编号',
    `title`            varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题',
    `amount`           decimal(10, 2)                                          DEFAULT NULL COMMENT '标的金额',
    `period`           int                                                     DEFAULT NULL COMMENT '投资期数',
    `lend_year_rate`   decimal(10, 2)                                          DEFAULT NULL COMMENT '年化利率',
    `service_rate`     decimal(10, 2)                                          DEFAULT NULL COMMENT '平台服务费率',
    `return_method`    tinyint                                                 DEFAULT NULL COMMENT '还款方式',
    `lowest_amount`    decimal(10, 2)                                          DEFAULT NULL COMMENT '最低投资金额',
    `invest_amount`    decimal(10, 2)                                          DEFAULT NULL COMMENT '已投金额',
    `invest_num`       int                                                     DEFAULT NULL COMMENT '投资人数',
    `lend_start_date`  date                                                    DEFAULT NULL COMMENT '开始日期',
    `lend_end_date`    date                                                    DEFAULT NULL COMMENT '结束日期',
    `lend_info`        text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '说明',
    `expect_amount`    decimal(10, 2)                                          DEFAULT NULL COMMENT '平台预期收益',
    `real_amount`      decimal(10, 2)                                          DEFAULT NULL COMMENT '实际收益',
    `status`           tinyint    NOT NULL                                     DEFAULT '0' COMMENT '状态',
    `check_admin_id`   bigint                                                  DEFAULT NULL COMMENT '审核用户id',
    `payment_time`     datetime                                                DEFAULT NULL COMMENT '放款时间',
    `payment_admin_id` bigint                                                  DEFAULT NULL COMMENT '放款人id',
    `create_time`      timestamp  NOT NULL COMMENT '创建时间',
    `update_time`      timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       tinyint(1) NOT NULL                                     DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_lend_no` (`lend_no`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE,
    KEY `idx_borrow_info_id` (`borrow_info_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381827378048696323
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='标的准备表';

-- No native definition for element: idx_user_id (index)

-- No native definition for element: idx_borrow_info_id (index)

-- lend_item: table
CREATE TABLE `lend_item`
(
    `id`              bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `lend_item_no`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资编号',
    `lend_id`         bigint     NOT NULL                                    DEFAULT '0' COMMENT '标的id',
    `invest_user_id`  bigint                                                 DEFAULT NULL COMMENT '投资用户id',
    `invest_name`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资人名称',
    `invest_amount`   decimal(10, 2)                                         DEFAULT NULL COMMENT '投资金额',
    `lend_year_rate`  decimal(10, 2)                                         DEFAULT NULL COMMENT '年化利率',
    `lend_start_date` date                                                   DEFAULT NULL COMMENT '开始日期',
    `lend_end_date`   date                                                   DEFAULT NULL COMMENT '结束日期',
    `expect_amount`   decimal(10, 2)                                         DEFAULT NULL COMMENT '预期收益',
    `real_amount`     decimal(10, 2)                                         DEFAULT NULL COMMENT '实际收益',
    `status`          tinyint                                                DEFAULT NULL COMMENT '状态（0：默认 1：已支付 2：已还款）',
    `create_time`     timestamp  NOT NULL COMMENT '创建时间',
    `update_time`     timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`      tinyint(1) NOT NULL                                    DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_lend_item_no` (`lend_item_no`) USING BTREE,
    KEY `idx_lend_id` (`lend_id`) USING BTREE,
    KEY `idx_invest_user_id` (`invest_user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381827944044855299
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='标的出借记录表';

-- No native definition for element: idx_lend_id (index)

-- No native definition for element: idx_invest_user_id (index)

-- lend_item_return: table
CREATE TABLE `lend_item_return`
(
    `id`               bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `lend_return_id`   bigint              DEFAULT NULL COMMENT '标的还款id',
    `lend_item_id`     bigint              DEFAULT NULL COMMENT '标的项id',
    `lend_id`          bigint     NOT NULL DEFAULT '0' COMMENT '标的id',
    `invest_user_id`   bigint              DEFAULT NULL COMMENT '出借用户id',
    `invest_amount`    decimal(10, 2)      DEFAULT NULL COMMENT '出借金额',
    `current_period`   int                 DEFAULT NULL COMMENT '当前的期数',
    `lend_year_rate`   decimal(10, 2)      DEFAULT NULL COMMENT '年化利率',
    `return_method`    tinyint             DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
    `principal`        decimal(10, 2)      DEFAULT NULL COMMENT '本金',
    `interest`         decimal(10, 2)      DEFAULT NULL COMMENT '利息',
    `total`            decimal(10, 2)      DEFAULT NULL COMMENT '本息',
    `fee`              decimal(10, 2)      DEFAULT '0.00' COMMENT '手续费',
    `return_date`      date                DEFAULT NULL COMMENT '还款时指定的还款日期',
    `real_return_time` datetime            DEFAULT NULL COMMENT '实际发生的还款时间',
    `is_overdue`       tinyint(1)          DEFAULT NULL COMMENT '是否逾期',
    `overdue_total`    decimal(10, 2)      DEFAULT NULL COMMENT '逾期金额',
    `status`           tinyint             DEFAULT NULL COMMENT '状态（0-未归还 1-已归还）',
    `create_time`      timestamp  NOT NULL COMMENT '创建时间',
    `update_time`      timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_lend_return_id` (`lend_return_id`) USING BTREE,
    KEY `idx_lend_item_id` (`lend_item_id`) USING BTREE,
    KEY `idx_lend_id` (`lend_id`) USING BTREE,
    KEY `idx_invest_user_id` (`invest_user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381828009383723012
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='标的出借回款记录表';

-- No native definition for element: idx_lend_return_id (index)

-- No native definition for element: idx_lend_item_id (index)

-- No native definition for element: idx_lend_id (index)

-- No native definition for element: idx_invest_user_id (index)

-- lend_return: table
CREATE TABLE `lend_return`
(
    `id`               bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `lend_id`          bigint                                                 DEFAULT NULL COMMENT '标的id',
    `borrow_info_id`   bigint     NOT NULL                                    DEFAULT '0' COMMENT '借款信息id',
    `return_no`        varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '还款批次号',
    `user_id`          bigint                                                 DEFAULT NULL COMMENT '借款人用户id',
    `amount`           decimal(10, 2)                                         DEFAULT NULL COMMENT '借款金额',
    `base_amount`      decimal(10, 2)                                         DEFAULT NULL COMMENT '计息本金额',
    `current_period`   int                                                    DEFAULT NULL COMMENT '当前的期数',
    `lend_year_rate`   decimal(10, 2)                                         DEFAULT NULL COMMENT '年化利率',
    `return_method`    tinyint                                                DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
    `principal`        decimal(10, 2)                                         DEFAULT NULL COMMENT '本金',
    `interest`         decimal(10, 2)                                         DEFAULT NULL COMMENT '利息',
    `total`            decimal(10, 2)                                         DEFAULT NULL COMMENT '本息',
    `fee`              decimal(10, 2)                                         DEFAULT '0.00' COMMENT '手续费',
    `return_date`      date                                                   DEFAULT NULL COMMENT '还款时指定的还款日期',
    `real_return_time` datetime                                               DEFAULT NULL COMMENT '实际发生的还款时间',
    `is_overdue`       tinyint(1)                                             DEFAULT NULL COMMENT '是否逾期',
    `overdue_total`    decimal(10, 2)                                         DEFAULT NULL COMMENT '逾期金额',
    `is_last`          tinyint(1)                                             DEFAULT NULL COMMENT '是否最后一次还款',
    `status`           tinyint                                                DEFAULT NULL COMMENT '状态（0-未归还 1-已归还）',
    `create_time`      timestamp  NOT NULL COMMENT '创建时间',
    `update_time`      timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       tinyint(1) NOT NULL                                    DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_return_no` (`return_no`) USING BTREE,
    KEY `idx_lend_id` (`lend_id`) USING BTREE,
    KEY `idx_borrow_info_id` (`borrow_info_id`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381828009333391365
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='还款记录表';

-- No native definition for element: idx_lend_id (index)

-- No native definition for element: idx_borrow_info_id (index)

-- No native definition for element: idx_user_id (index)

-- trans_flow: table
CREATE TABLE `trans_flow`
(
    `id`              bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`         bigint                                                 NOT NULL DEFAULT '0' COMMENT '用户id',
    `user_name`       varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '用户名称',
    `trans_no`        varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '交易单号',
    `trans_type`      tinyint                                                NOT NULL DEFAULT '0' COMMENT '交易类型（1：充值 2：提现 3：投标 4：投资回款 ...）',
    `trans_type_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL COMMENT '交易类型名称',
    `trans_amount`    decimal(10, 2)                                                  DEFAULT NULL COMMENT '交易金额',
    `memo`            varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL COMMENT '备注',
    `create_time`     timestamp                                              NOT NULL COMMENT '创建时间',
    `update_time`     timestamp                                              NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`      tinyint(1)                                             NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_trans_no` (`trans_no`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381829245944233987
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='交易流水表';

-- No native definition for element: idx_user_id (index)

-- user_account: table
CREATE TABLE `user_account`
(
    `id`            bigint         NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`       bigint         NOT NULL DEFAULT '0' COMMENT '用户id',
    `amount`        decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '帐户可用余额',
    `freeze_amount` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
    `create_time`   timestamp      NOT NULL COMMENT '创建时间',
    `update_time`   timestamp      NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    tinyint(1)     NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    `version`       int            NOT NULL DEFAULT '0' COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381768468310822918
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户账户';

-- user_bind: table
CREATE TABLE `user_bind`
(
    `id`          bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`     bigint                                                 NOT NULL DEFAULT '0' COMMENT '用户id',
    `name`        varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
    `id_card`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '身份证号',
    `bank_no`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '银行卡号',
    `bank_type`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '银行类型',
    `mobile`      varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
    `bind_code`   varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '绑定账户协议号',
    `status`      tinyint                                                         DEFAULT NULL COMMENT '状态',
    `create_time` timestamp                                              NOT NULL COMMENT '创建时间',
    `update_time` timestamp                                              NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(1)                                             NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381810843087306755
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户绑定表';

-- user_info: table
CREATE TABLE `user_info`
(
    `id`                 bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_type`          tinyint                                                NOT NULL DEFAULT '0' COMMENT '1：出借人 2：借款人',
    `mobile`             varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
    `password`           varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
    `nick_name`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '用户昵称',
    `name`               varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '用户姓名',
    `id_card`            varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '身份证号',
    `email`              varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL COMMENT '邮箱',
    `openid`             varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '微信用户标识openid',
    `head_img`           varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL COMMENT '头像',
    `bind_status`        tinyint                                                NOT NULL DEFAULT '0' COMMENT '绑定状态（0：未绑定，1：绑定成功 -1：绑定失败）',
    `borrow_auth_status` tinyint                                                NOT NULL DEFAULT '0' COMMENT '借款人认证状态（0：未认证 1：认证中 2：认证通过 -1：认证失败）',
    `bind_code`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '绑定账户协议号',
    `integral`           int                                                    NOT NULL DEFAULT '0' COMMENT '用户积分',
    `status`             tinyint                                                NOT NULL DEFAULT '1' COMMENT '状态（0：锁定 1：正常）',
    `create_time`        timestamp                                              NOT NULL COMMENT '创建时间',
    `update_time`        timestamp                                              NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`         tinyint(1)                                             NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `uk_mobile` (`mobile`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381768468260491270
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户基本信息';

-- No native definition for element: uk_mobile (index)

-- user_integral: table
CREATE TABLE `user_integral`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`     bigint                                                  DEFAULT NULL COMMENT '用户id',
    `integral`    int                                                     DEFAULT NULL COMMENT '积分',
    `content`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '获取积分说明',
    `create_time` timestamp  NOT NULL COMMENT '创建时间',
    `update_time` timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(1) NOT NULL                                     DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381829245843570690
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户积分记录表';

-- No native definition for element: idx_user_id (index)

-- user_login_record: table
CREATE TABLE `user_login_record`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `user_id`     bigint                                                 DEFAULT NULL COMMENT '用户id',
    `ip`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'ip',
    `create_time` timestamp  NOT NULL COMMENT '创建时间',
    `update_time` timestamp  NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(1) NOT NULL                                    DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1381828664257183747
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='用户登录记录表';

-- No native definition for element: idx_user_id (index)

