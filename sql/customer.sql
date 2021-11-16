create table if not exists business_log
(
    `id` int unsigned auto_increment
        primary key,
    `business_id` int default 0 not null comment '相应业务id',
    `params` json DEFAULT NULL comment '参数',
    `event` smallint default 4 not null comment '事件',
    `operation_id` int default 0 not null comment '操作人id（employee.id）',
    `created_at` timestamp default CURRENT_TIMESTAMP null,
    `updated_at` timestamp null on update CURRENT_TIMESTAMP
) comment '业务日志表';

1.企业表
create table if not exists corp
(
    id int(11) unsigned auto_increment
        primary key,
    name varchar(255) default '' not null comment '企业名称',
    wx_corpid char(255) default '' not null comment '企业微信ID',
    social_code char(255) default '' not null comment '企业代码(企业统一社会信用代码)',
    employee_secret char(255) default '' not null comment '企业通讯录secret',
    event_callback varchar(255) default '' not null comment '事件回调地址',
    contact_secret char(255) default '' not null comment '企业外部联系人secret',
    token char(255) default '' not null comment '回调token',
    encoding_aes_key char(255) default '' not null comment '回调消息加密串',
    created_at timestamp null,
    updated_at timestamp default CURRENT_TIMESTAMP null,
    deleted_at timestamp null
) comment '企业';

2.部门表
create table if not exists department
(
    id int(11) unsigned auto_increment comment '部门ID'
        primary key,
    wx_department_id int(11) unsigned default 0 not null comment '微信部门自增ID',
    corp_id int(11) unsigned not null comment '企业表ID（mc_corp.id）',
    name varchar(255) default '' not null comment '部门名称',
    director_wx_user_id varchar(255) default '' not null comment '当前部门负责人',
    parent_id int(11) unsigned default 0 not null comment '父部门ID',
    wx_parentid int(11) unsigned not null comment '微信父部门ID',
    `order` int(11) unsigned default 0 not null comment '排序',
    level tinyint default 0 not null comment '部门级别',
    path varchar(255) default ' ' not null comment '父ID路径【#id#-#id#】',
    created_at timestamp null,
    updated_at timestamp default CURRENT_TIMESTAMP null,
    deleted_at timestamp null
) comment '(通讯录)部门管理';

3.员工表
create table if not exists employee
(
    id int(11) unsigned auto_increment
        primary key,
    wx_user_id varchar(255) default '' not null comment 'wx.userId',
    corp_id int default 0 not null comment '所属企业corpid（mc_corp.id）',
    enaccount_user_id int(11) unsigned default 0 not null comment '账号用户id',
    enaccount_user_name varchar(255) default '' not null comment '账号系统中的登陆用户名',
    name varchar(255) default '' not null comment '名称',
    mobile char(11) default '' not null comment '手机号',
    position varchar(255) default '' not null comment '职位信息',
    gender tinyint(1) unsigned default 0 not null comment '性别。0表示未定义，1表示男性，2表示女性',
    email varchar(255) default '' not null comment '邮箱',
    telephone varchar(255) default '' not null comment '座机',
    alias varchar(255) default '' not null comment '别名',
    status tinyint(1) unsigned default 0 not null comment '激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业 0--无效状态 1--enaccount中有此账号 但在企业微信中查无此用户
2--企业微信中有此账号但账号系统中无此账号 3--企业微信&账号系统中用户建立起了一一对应关系',
    qr_code varchar(255) default '' not null comment '员工二维码',
    wx_main_department_id int(11) unsigned default 0 not null comment '微信端主部门ID',
    main_department_id int default 0 not null comment '主部门id(mc_work_department.id)',
    created_at timestamp null,
    updated_at timestamp default CURRENT_TIMESTAMP null,
    deleted_at timestamp null
) comment '企业通讯录';

create table if not exists contact
(
    id int(11) unsigned auto_increment
        primary key,
    corp_id int(11) unsigned default 0 not null comment '企业表ID（mc_crop.id）',
    wx_external_userid varchar(255) default '' not null comment '外部联系人external_userid',
    name varchar(255) default '' not null comment '外部联系人姓名',
    nick_name varchar(255) default '' not null comment '外部联系人昵称',
    avatar varchar(255) default '' not null comment '外部联系人的头像',
    follow_up_status tinyint default 0 not null comment '跟进状态（1.未跟进 2.跟进中 3.已拒绝 4.已成交 5.已复购）' ||
     '教育的跟进状态为1建立联系 2确定意向 3承诺到访 4预约试听 5完成试听 6报名缴费',
    type tinyint(4) unsigned default 1 not null comment '外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户',
    source tinyint(4) unsigned default 1 not null comment '客户来源 1：企业微信添加的外部联系人 2',
    gender tinyint(4) unsigned default 0 not null comment '外部联系人性别 0-未知 1-男性 2-女性',
    unionid varchar(255) default '' not null comment '外部联系人在微信开放平台的唯一身份标识（微信unionid）',
    label varchar(64) default '' not null comment '添加自带标签',
    external_profile json DEFAULT NULL comment '外部联系人的自定义展示信息 教育客户保存联系人信息',
--     birth_date varchar(64) default '' not null comment '出生日期', 这些字段和联系人信息全部存放在external_profile里面
--     grade varchar(64) default '' not null comment '当前年级',
--     attending_school varchar(64) default '' not null comment '就读学校',
--     home_address varchar(64) default '' not null comment '家庭住址',
--     intention_class varchar(64) default '' not null comment '意向课程',
--     intention_school varchar(64) default '' not null comment '意向校区',
--     intention_level tinyint(4) default 1 not null comment '意向级别 1强 2中 3弱',
--     remark varchar(64) default '' not null comment '备注信息',
    mobile char(11) default '' not null comment '外部联系人的手机号，只有通过手机号搜索加入才有',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '联系人表（客户列表）';

create table if not exists contact_employee
(
    id int(11) unsigned auto_increment
        primary key,
    employee_id int(11) unsigned default 0 not null comment '通讯录表ID（work_employee.id）',
    contact_id int(11) unsigned default 0 not null comment '客户表ID（work_contact.id）',
    remark varchar(255) default '' not null comment '员工对此外部联系人的备注',
    description varchar(255) default '' not null comment '员工对此外部联系人的描述',
    remark_corp_name varchar(255) default '' not null comment '员工对此客户备注的企业名称',
    remark_mobiles json DEFAULT NULL comment '员工对此客户备注的手机号码',
    remark_pet_experience varchar(255) default '' not null comment '员工对此外部联系人的买宠经验',
    add_way int(11) unsigned not null comment '表示添加客户的来源
0
未知来源
1
扫描二维码
2
搜索手机号
3
名片分享
4
群聊
5
手机通讯录
6
微信联系人
7
来自微信的添加好友申请
8
安装第三方应用时自动添加的客服人员
9
搜索邮箱
201
内部成员共享
202
管理员/负责人分配
',
    oper_userid varchar(255) default '' not null comment '发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid
',
    state varchar(255) default '' not null comment '渠道标签',
    corp_id int(11) unsigned default 0 not null comment '企业表ID（corp.id）',
    status tinyint default 1 not null comment '1.正常 2.删除 3.拉黑 4释放',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '员工添加此外部联系人的时间',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '通讯录 - 客户 中间表';

//----------------------------------------客户动态表 需要具体教育和宠物的事件类型
create table if not exists contact_process
(
    id int(11) unsigned auto_increment
        primary key,
    corp_id int(11) unsigned default 0 not null comment 'corp表id',
    name varchar(255) default '' not null comment '名称',
    description varchar(255) default '' not null comment '描述',
    `order` int(11) unsigned default 0 not null comment '排序',
    created_at timestamp null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户跟进状态';

create table if not exists contact_employee_process
(
    id int(11) unsigned not null
        primary key,
    corp_id int(11) unsigned default 0 not null comment '企业表ID（corp.id）',
    employee_id int(11) unsigned default 0 not null comment '员工ID（mc_work_employee.id）',
    contact_id int(11) unsigned default 0 not null comment '外部联系人ID（mc_work_contact.id）',
    contact_process_id int(11) unsigned default 0 not null comment '跟进流程ID',
    content text not null comment '跟进内容',
    file_url json DEFAULT NULL comment '附件地址',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '通讯录-客户-跟进记录(中间表) ';
//----------------------------------------客户动态表

//----------------------------------------买宠需求
create table if not exists contact_employee_demand
(
    id int(11) unsigned not null
        primary key,
    corp_id int(11) unsigned default 0 not null comment '企业表ID（corp.id）',
    employee_id int(11) unsigned default 0 not null comment '员工ID（mc_work_employee.id）',
    contact_id int(11) unsigned default 0 not null comment '外部联系人ID（mc_work_contact.id）',
    content text not null comment '买宠需求',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '通讯录-客户-买宠需求记录(中间表) ';
//----------------------------------------买宠需求

create table if not exists contact_tag_pivot
(
    id int(11) unsigned auto_increment
        primary key,
    contact_id int(11) unsigned not null comment '客户表ID（work_contact.id）',
    employee_id int default 0 not null comment '员工表id（work_employee.id）',
    tag_id int(11) unsigned not null comment '客户标签表ID（work_contact_tag.id）',
    type tinyint default 0 not null comment '该成员添加此外部联系人所打标签类型, 1-企业设置, 2-用户自定义',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户-标签关联表';

create table if not exists employee_tag_pivot
(
    id int(11) unsigned auto_increment
        primary key,
    employee_id int(11) unsigned not null comment '通讯录员工ID',
    tag_id int(11) unsigned not null comment 'wx标签ID',
    created_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    updated_at timestamp default CURRENT_TIMESTAMP not null
) comment '(通讯录 - 标签)中间表';

// ----------------------------标签
create table if not exists tag
(
    id int(11) unsigned auto_increment comment '企业标签ID'
        primary key,
    wx_tag_id varchar(255) default '' not null comment '微信企业标签ID',
    corp_id int(11) unsigned default 0 not null comment '企业表ID （mc_corp.id）',
    name varchar(255) default '' not null comment '标签名称',
    `order` int(11) unsigned default 0 not null comment '排序',
    tag_group_id int(11) unsigned default 0 not null comment '客户标签分组ID（mc_work_contract_tag_group.id）',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户标签';

create table if not exists tag_group
(
    id int(11) unsigned auto_increment
        primary key,
    wx_group_id varchar(60) default '' not null comment '微信企业标签分组ID',
    corp_id int(11) unsigned default 0 not null comment '企业表ID （mc_corp.id）',
    group_name varchar(30) default '' not null comment '客户标签分组名称',
    `order` int(11) unsigned default 0 not null comment '排序',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户标签 - 分组';

// ----------------------------标签


//-----------------------------------素材表
create table if not exists medium
(
    id int(11) unsigned auto_increment
        primary key,
    media_id varchar(255) default '' not null comment '素材媒体标识[有效期3天]',
    type tinyint(1) unsigned default 1 not null comment '类型 1文本、2图片、3音频、4视频、5小程序、6文件素材',
    is_sync tinyint(1) default 1 not null comment '是否同步素材库(1-同步2-不同步，默认:1)',
    content json not null comment '具体内容:',
    corp_id int(11) unsigned default 0 not null comment '企业表ID(mc_corp.id)',
    medium_group_id int(11) unsigned default 0 not null comment '素材分组ID medium_group.id',
    user_id int default 0 not null comment '上传者ID',
    user_name varchar(255) default '' not null comment '上传者名称',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp default CURRENT_TIMESTAMP null,
    deleted_at timestamp null
) comment '素材库 ';

create table if not exists medium_group
(
    id int(11) unsigned auto_increment
        primary key,
    corp_id int(11) unsigned default 0 not null comment '企业表ID',
    name varchar(255) default '' not null comment '名称',
    `order` int(11) unsigned default 0 not null comment '排序',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '素材库-分组';
//-----------------------------------素材表

//-----------------------------------渠道码表
create table if not exists channel_code
(
    id int unsigned auto_increment
        primary key,
    corp_id int default 0 not null comment '企业id',
    group_id int default 0 not null comment '渠道码分组id（mc_channel_code_group.id）',
    name varchar(255) default '' not null comment '活码名称',
    qrcode_url varchar(255) default '' not null comment '二维码地址',
    wx_config_id varchar(255) default '' not null comment '二维码凭证',
    auto_add_friend tinyint default 0 not null comment '自动添加好友（1.开启，2.关闭）',
    tags json not null comment '客户标签',
    type tinyint default 0 not null comment '类型（1.单人，2.多人）',
    drainage_employee json not null comment '引流成员设置',
    welcome_message json not null comment '欢迎语设置',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '渠道码表';

create table if not exists channel_code_group
(
    id int unsigned auto_increment
        primary key,
    corp_id int not null comment '企业id',
    name varchar(255) not null comment '分组名称',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '渠道码-分组表';

//-----------------------------------渠道码表


//--------------------------------------客户群
create table if not exists contact_room
(
    id int(11) unsigned auto_increment
        primary key,
    wx_user_id varchar(255) default '' not null,
    contact_id int(11) unsigned default 0 not null comment '客户表id（work_contact.id）',
    employee_id int(11) unsigned default 0 not null comment '员工ID (work_employee.id)',
    unionid varchar(255) default '' not null comment '仅当群成员类型是微信用户（包括企业成员未添加好友），且企业或第三方服务商绑定了微信开发者ID有此字段',
    room_id int(11) unsigned default 0 not null comment '客户群表id（work_room.id）',
    join_scene tinyint(1) unsigned default 3 null comment '入群方式1 - 由成员邀请入群（直接邀请入群）2 - 由成员邀请入群（通过邀请链接入群）3 - 通过扫描群二维码入群',
    type tinyint(4) unsigned default 1 not null comment '成员类型（1 - 企业成员 2 - 外部联系人）',
    status tinyint default 1 not null comment '成员状态。1 - 正常2 -退群',
    join_time timestamp default CURRENT_TIMESTAMP not null comment '入群时间',
    out_time varchar(50) default '' not null comment '退群时间',
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户 - 客户群 关联表';

create table if not exists work_room
(
    id int unsigned auto_increment
        primary key,
    corp_id int(11) unsigned default 0 not null comment '企业表ID（mc_corp.id）',
    wx_chat_id varchar(255) default '' not null comment '客户群ID',
    name varchar(255) default '' not null comment '客户群名称',
    owner_id int(11) unsigned default 0 not null comment '群主ID（work_employee.id）',
    notice text not null comment '群公告',
    status tinyint(4) unsigned default 0 not null comment '客户群状态（0 - 正常 1 - 跟进人离职 2 - 离职继承中 3 - 离职继承完成）',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '群创建时间',
    room_max int(10) default 0 not null comment '群成员上限',
    room_group_id int(11) unsigned default 0 not null comment '分组id（work_room_group.id）',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户群表';

create table if not exists work_room_group
(
    id int unsigned auto_increment
        primary key,
    corp_id int(11) unsigned not null comment '企业表ID（mc_corp.id）',
    name varchar(255) not null comment '分组名称',
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户群分组管理表';
//--------------------------------------客户群


//--------------------------------------一期现在不需要

create table if not exists contact_employee_track
(
    id int(11) unsigned auto_increment
        primary key,
    employee_id int(11) unsigned default 0 not null comment '通讯录ID(mc_work_employee.id)',
    contact_id int(11) unsigned default 0 not null comment '外部联系人ID work_contact.id',
    event tinyint default 0 not null comment '事件',
    content varchar(255) default '' not null comment '内容',
    corp_id int(11) unsigned default 0 not null comment '企业表ID corp.id',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '通讯录 - 客户 - 轨迹互动';

create table if not exists contact_field
(
    id int(11) unsigned auto_increment
        primary key,
    name varchar(255) default '' not null comment '字段标识 input-name',
    label varchar(255) default '' not null comment '字段名称 input-label',
    type tinyint(1) unsigned default 0 not null comment '字段类型 input-type 0text 1radio 2 checkbox 3select 4file 5date 6dateTime 7number 8rate',
    options json DEFAULT NULL comment '字段可选值 input-options',
    `order` int(11) unsigned default 0 not null comment '排序',
    status tinyint(1) unsigned default 0 not null comment '状态 0不展示 1展示',
    is_sys tinyint(1) unsigned default 0 not null comment '是否为系统字段 0否1是',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '客户高级属性';

create table if not exists contact_field_pivot
(
    id int(11) unsigned auto_increment
        primary key,
    contact_id int(11) unsigned default 0 not null comment '客户表ID（work_contact.id）',
    contact_field_id int(11) unsigned default 0 not null comment '高级属性表ID(contact_field.id）',
    value text null comment '高级属性值',
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp null on update CURRENT_TIMESTAMP,
    deleted_at timestamp null
) comment '(客户-高级属性-中间表)用户画像';