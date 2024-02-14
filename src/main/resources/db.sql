create
database if not exists chatroom charset utf8;

use
chatroom;

drop table if exists user;
create table user
(
    id         int primary key auto_increment,
    username   varchar(20) unique                  not null,
    password   varchar(20)                         not null,
    phone      varchar(50)                         not null,
    email      varchar(255)                        not null,
    photo      varchar(255),
    createTime timestamp DEFAULT CURRENT_TIMESTAMP not null,
    updateTime timestamp DEFAULT CURRENT_TIMESTAMP not null
);

insert into user(username, password, phone, email)
values ('zhangsan', '111', '1111', '11111');
insert into user(username, password, phone, email)
values ('lisi', '111', '1111', '11111');
insert into user(username, password, phone, email)
values ('wangwu', '111', '1111', '11111');
insert into user(username, password, phone, email)
values ('zhaoliu', '111', '1111', '11111');

-- 创建好友表
drop table if exists friend;
create table friend
(
    userId   int,
    friendId int
);

insert into friend
values (1, 2);
insert into friend
values (2, 1);
insert into friend
values (1, 3);
insert into friend
values (3, 1);
insert into friend
values (1, 4);
insert into friend
values (4, 1);

-- 创建会话表
drop table if exists session;
create table session
(
    sessionId int primary key auto_increment,
    -- 上次访问时间
    lastTime  datetime
);

insert into session
values (1, '2024-01-01 00:00:00');
insert into session
values (2, '2024-02-01 00:00:00');

-- 创建会话和用户的关联表
drop table if exists session_user;
create table session_user
(
    sessionId int,
    userId    int
);

insert into session_user
values (1, 1),
       (1, 2);
insert into session_user
values (2, 1),
       (2, 3);

-- 创建消息表
drop table if exists message;
create table message
(
    messageId int primary key auto_increment,
    fromId int,
    sessionId int,
    content varchar(2048),
    postTime datetime
);
insert into message
values (1, 1, 1, '今晚吃啥', '2000-05-01 17:00:00');
insert into message
values (2, 2, 1, '随便', '2000-05-01 17:01:00');
insert into message
values (3, 1, 1, '那吃面?', '2000-05-01 17:02:00');
insert into message
values (4, 2, 1, '不想吃', '2000-05-01 17:03:00');
insert into message
values (5, 1, 1, '那你想吃啥', '2000-05-01 17:04:00');
insert into message
values (6, 2, 1, '随便', '2000-05-01 17:05:00');
insert into message
values (11, 1, 1, '那吃米饭炒菜?', '2000-05-01 17:06:00');
insert into message
values (8, 2, 1, '不想吃', '2000-05-01 17:07:00');
insert into message
values (9, 1, 1, '那你想吃啥?', '2000-05-01 17:08:00');
insert into message
values (10, 2, 1, '随便', '2000-05-01 17:09:00');
insert into message
values (7, 1, 2, '晚上一起约?', '2000-05-02 12:00:00');