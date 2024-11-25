create table if not exists agent_account
(
    id          varchar(32) not null
    constraint agent_account_pk
    primary key,
    phone       varchar(32),
    name        varchar(32),
    password    varchar(32),
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_cashier_discount
(
    id          varchar(32) not null
    constraint m_cashier_discount_pk
    primary key,
    merchant_no bigint,
    discount    bigint,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_cashier_flow
(
    id                varchar(32) not null
    constraint m_cashier_flow_pk
    primary key,
    merchant_no       bigint,
    custom_flow       integer,
    custom_flow_model integer,
    rating_rest_flow  integer,
    init_flow         bigint,
    create_time       timestamp(0),
    update_time       timestamp(0)
    );

create table if not exists m_cashier_hang
(
    id           varchar(32) not null
    constraint m_cashier_hang_pk
    primary key,
    name         varchar(64),
    detail       jsonb,
    create_time  timestamp(0),
    operation_id varchar(32),
    hang_no      varchar(32),
    status       integer,
    update_time  timestamp(0),
    remark       varchar(512),
    merchant_no  bigint,
    store_no     bigint
    );

create table if not exists m_cashier_ignore
(
    id          varchar(32) not null
    constraint m_cashier_ignore_pk
    primary key,
    merchant_no bigint,
    type        integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_feedback
(
    id           varchar(32) not null
    constraint m_feedback_pk
    primary key,
    merchant_no  bigint,
    store_no     bigint,
    account_id   varchar(32),
    title        varchar(128),
    content      varchar(1024),
    images       varchar(1024),
    contact_type integer,
    contact_no   varchar(32),
    status       integer,
    ip           varchar(32),
    create_time  timestamp(0),
    update_time  timestamp(0),
    type         integer
    );

create table if not exists m_goods
(
    id               varchar(32) not null
    constraint m_goods_pk
    primary key,
    barcode          varchar(32),
    name             varchar(128),
    class_code       varchar(128),
    brand_name       varchar(128),
    specification    varchar(128),
    width            varchar(32),
    height           varchar(32),
    depth            varchar(32),
    code_source      varchar(128),
    firm_name        varchar(128),
    gross_weight     varchar(32),
    batch            varchar(128),
    firm_address     varchar(256),
    firm_status      varchar(32),
    firm_description varchar(512),
    barcode_status   varchar(32),
    image            jsonb,
    create_time      timestamp(0),
    update_time      timestamp(0),
    price            bigint
    );

create table if not exists m_goods_attr_category
(
    id          varchar(64) not null
    constraint m_goods_attr_category_pk
    primary key,
    merchant_no bigint,
    name        varchar(32),
    pid         varchar(64),
    level       integer default 0,
    status      integer,
    create_time timestamp,
    update_time timestamp,
    sort        integer default 0,
    deleted     integer default 0
    );

create table if not exists m_goods_attr_spec
(
    id          varchar(64) not null
    constraint m_goods_attr_spec_pk
    primary key,
    merchant_no bigint,
    name        varchar(32),
    pid         varchar(64),
    status      integer,
    create_time timestamp,
    update_time timestamp
    );

create table if not exists m_goods_attr_tag
(
    id          varchar(64) not null
    constraint m_goods_attr_tag_pk
    primary key,
    merchant_no bigint,
    name        varchar(32),
    remark      varchar(128),
    status      integer,
    create_time timestamp,
    update_time timestamp,
    sort        integer
    );

create table if not exists m_goods_attr_unit
(
    id          varchar(64) not null
    constraint m_goods_attr_unit_pk
    primary key,
    merchant_no bigint,
    name        varchar(32),
    type        bigint,
    status      integer,
    discard     integer,
    create_time timestamp,
    update_time timestamp
    );

create table if not exists m_goods_sku
(
    id               varchar(64) not null
    constraint mg_sku_pkey
    primary key,
    spu_id           varchar(64),
    sku_code         varchar(128),
    sku_no           varchar(128),
    purchase_amount  bigint,
    reference_amount bigint,
    sku_weight       bigint,
    status           integer,
    create_time      timestamp(0),
    update_time      timestamp(0),
    sku_name         varchar(512),
    deleted          integer default 0,
    join_spec        varchar(2048)
    );

create table if not exists m_goods_sku_spec
(
    id              varchar(32) not null
    constraint mg_sku_spec_pkey
    primary key,
    spu_id          varchar(32),
    sku_id          varchar(32),
    spec_id         varchar(32),
    spec_name       varchar(32),
    spec_value_id   varchar(32),
    spec_value_name varchar(32),
    status          integer,
    deleted         integer default 0,
    create_time     timestamp(0),
    update_time     timestamp(0)
    );

create table if not exists m_goods_sku_tag
(
    id          varchar(64) not null
    constraint m_goods_sku_tag_pk
    primary key,
    sku_id      varchar(64),
    tag_id      varchar(64),
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_goods_spu
(
    id           varchar(64) not null
    constraint mg_spu_pkey
    primary key,
    merchant_no  bigint,
    category_id  varchar(32),
    spu_code     varchar(128),
    spu_no       varchar(128),
    name         varchar(256),
    unit_id      varchar(64),
    remark       varchar(256),
    status       integer,
    create_time  timestamp(0),
    update_time  timestamp(0),
    pictures     jsonb,
    deleted      integer default 0,
    counted_type integer default 0
    );

comment on column m_goods_spu.counted_type is '是否可计次核销(0:不可计次，2：可计次核销)';

create table if not exists m_goods_spu_picture
(
    id          varchar(64) not null
    constraint m_goods_spu_picture_pk
    primary key,
    spu_id      varchar(64),
    url         varchar(2048),
    sort        integer,
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_market_recharge_rule
(
    id                varchar(32) not null
    constraint m_market_recharge_rule_pk
    primary key,
    merchant_no       bigint,
    store_no          bigint,
    prepaid_amount    bigint,
    name              varchar(128),
    real_amount       bigint,
    give_amount       bigint,
    give_integral     bigint,
    give_coupon_id    varchar(32),
    give_coupon_total bigint,
    status            integer,
    remark            varchar(256),
    create_time       timestamp(0),
    update_time       timestamp(0),
    deleted           integer default 0,
    sort              integer default 0
    );

create table if not exists m_market_time_card_rule
(
    id                varchar(32) not null
    constraint m_market_time_card_rule_pk
    primary key,
    merchant_no       bigint,
    store_no          bigint,
    name              varchar(128),
    expire_type       integer,
    time_total        integer,
    real_amount       bigint,
    give_integral     bigint,
    give_coupon_id    varchar(32),
    give_coupon_total bigint,
    status            integer,
    account_id        varchar(32),
    create_time       timestamp(0),
    update_time       timestamp(0),
    remark            varchar(256),
    expire_month      bigint,
    spu_ids           jsonb,
    deleted           integer default 0,
    sort              integer default 0
    );

create table if not exists m_member
(
    id           varchar(32) not null
    constraint m_member_pk
    primary key,
    merchant_no  bigint,
    store_no     bigint,
    phone        varchar(16),
    name         varchar(32),
    sex          integer,
    birth_day    timestamp(0),
    status       integer,
    remark       varchar(128),
    account_id   varchar(32),
    create_time  timestamp(0),
    update_time  timestamp(0),
    avatar       varchar(1024),
    deleted      integer default 0,
    level_id     varchar(32),
    source       integer,
    channel      integer,
    reference_id varchar(32)
    );

comment on column m_member.deleted is '是否删除';

comment on column m_member.source is '注册来源方式';

comment on column m_member.channel is '来源渠道';

comment on column m_member.reference_id is '推荐人';

create table if not exists m_member_assets
(
    id               varchar(32) not null
    constraint m_member_assets_pk
    primary key,
    now_integral     bigint,
    accrual_integral bigint,
    now_recharge     bigint,
    accrual_recharge bigint,
    create_time      timestamp(0),
    update_time      timestamp(0)
    );

comment on table m_member_assets is '会员财产信息';

create table if not exists m_member_assets_log
(
    id            varchar(32) not null
    constraint mm_member_assets_log_pkey
    primary key,
    member_id     varchar(32),
    order_no      varchar(32),
    category      integer,
    before_assets bigint,
    change_assets bigint,
    after_assets  bigint,
    remark        varchar(128),
    create_time   timestamp(0),
    update_time   timestamp(0),
    type          integer
    );

comment on column m_member_assets_log.category is '变更种类(会员储值，储值退款，会员消费，消费退款)';

create table if not exists m_member_attr
(
    id               varchar(32) not null
    constraint m_member_attr_pk
    primary key,
    merchant_no      bigint,
    new_reward       integer,
    reward_model     integer,
    reward_number    integer,
    convert_amount   bigint,
    convert_integral bigint
    );

create table if not exists m_member_del_record
(
    id          varchar(32) not null
    constraint m_member_del_record_pk
    primary key,
    merchant_no bigint,
    store_no    bigint,
    member      json,
    account_id  varchar(32),
    create_time timestamp(0)
    );

create table if not exists m_member_level
(
    id             varchar(32) not null
    constraint m_member_level_pk
    primary key,
    merchant_no    bigint,
    name           varchar(32),
    start_integral bigint,
    end_integral   bigint,
    discount       bigint,
    create_time    timestamp(0)
    );

create table if not exists m_member_tag
(
    id          varchar(32) not null
    constraint m_member_tag_pk
    primary key,
    merchant_no bigint,
    name        varchar(32),
    color       varchar(32),
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_member_tag_map
(
    id          varchar(32),
    member_id   varchar(32),
    tag_id      varchar(32),
    create_time timestamp(0)
    );

create table if not exists m_member_time_card
(
    id              varchar(32) not null
    constraint m_member_time_card_pk
    primary key,
    member_id       varchar(32),
    time_card_id    varchar(32),
    name            varchar(128),
    expire_type     integer,
    expire_time     timestamp(0),
    now_time_card   integer,
    total_time_card integer,
    create_time     timestamp(0),
    update_time     timestamp(0),
    spu_ids         jsonb,
    deleted         integer default 0
    );

comment on column m_member_time_card.deleted is '是否删除';

create table if not exists m_member_time_card_log
(
    id            varchar(32) not null
    constraint m_member_time_card_log_pk
    primary key,
    member_id     varchar(32),
    change_type   integer,
    change_before bigint,
    change_total  bigint,
    change_after  bigint,
    remark        varchar(256),
    order_no      varchar(64),
    account_id    varchar(32),
    create_time   timestamp(0),
    update_time   timestamp(0)
    );

create table if not exists m_merchant
(
    merchant_no   bigint not null
    constraint m_merchant_pk
    primary key,
    account_id    varchar(32),
    merchant_name varchar(128),
    province      varchar(32),
    city          varchar(32),
    area          varchar(32),
    address       varchar(256),
    agent_id      varchar(32),
    status        integer,
    create_time   timestamp,
    update_time   timestamp,
    code          varchar(20),
    source        integer,
    pc_logo       varchar(128),
    mobile_logo   varchar(128),
    type          integer default 0
    );

comment on column m_merchant.code is '邀请码';

comment on column m_merchant.source is '注册来源';

comment on column m_merchant.type is '商户类型(0:普通商户，1:体验商户)';

create table if not exists m_merchant_auth
(
    id                varchar(32) not null
    constraint m_merchant_auth_pk
    primary key,
    merchant_no       bigint,
    name              varchar(32),
    sex               integer,
    nation            varchar(32),
    birth_date        varchar(32),
    id_card_no        varchar(32),
    id_card_front_url varchar(256),
    id_card_back_url  varchar(256),
    status            integer,
    remark            varchar(128),
    create_time       timestamp,
    update_time       timestamp
    );

create table if not exists m_merchant_industry
(
    id          varchar(32) not null
    constraint m_merchant_industry_pk
    primary key,
    merchant_no bigint,
    industry_id varchar(32),
    expire_date timestamp(0),
    create_time timestamp(0),
    update_time timestamp(0),
    type        integer
    );

create table if not exists m_merchant_industry_log
(
    id          varchar(32) not null
    constraint mm_office_industry_merchant_log_pk
    primary key,
    merchant_no bigint,
    industry_id varchar(32),
    add_month   bigint,
    create_time timestamp(0),
    type        integer,
    order_no    varchar(32)
    );

create table if not exists m_merchant_store
(
    id             varchar(32) not null
    constraint m_merchant_store_pk
    primary key,
    merchant_no    bigint,
    store_no       bigint      not null,
    account_id     varchar(32),
    province       varchar(32),
    city           varchar(32),
    area           varchar(32),
    address        varchar(128),
    industry_one   bigint,
    industry_two   bigint,
    industry_three bigint,
    status         integer,
    create_time    timestamp,
    update_time    timestamp,
    gender         integer,
    store_name     varchar(128),
    store_phone    varchar(32),
    end_date       timestamp(0),
    main           boolean default false,
    deleted        integer default 0
    );

comment on column m_merchant_store.deleted is '是否删除';

create table if not exists m_merchant_tag
(
    id          varchar(32) not null
    constraint m_merchant_tag_pk
    primary key,
    merchant_no bigint,
    name        varchar(32),
    color       varchar(32),
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_office_industry_attr
(
    id     varchar(32) not null
    constraint m_office_industry_attr_pk
    primary key,
    name   varchar(32),
    type   integer,
    status integer,
    perm   varchar(20),
    sort   integer,
    fee    boolean
    );

comment on column m_office_industry_attr.type is '功能分类';

create table if not exists m_office_industry_attr_map
(
    id               varchar(32) not null
    constraint m_office_industry_attr_map_pk
    primary key,
    industry_attr_id varchar(32),
    industry_id      varchar(32),
    create_time      timestamp(0)
    );

create table if not exists m_office_order
(
    id             varchar(32) not null
    constraint m_office_order_pk
    primary key,
    merchant_no    bigint,
    store_no       bigint,
    account_id     varchar(32),
    order_no       varchar(32),
    subject        varchar(64),
    payable_amount bigint,
    config_id      varchar(32),
    pay_type       integer,
    status         integer,
    pay_message    varchar(128),
    source         integer,
    remark         varchar(128),
    pay_time       timestamp(0),
    trade_no       varchar(64),
    create_time    timestamp(0),
    update_time    timestamp(0)
    );

create table if not exists m_office_order_item
(
    id             varchar(32) not null
    constraint m_office_order_item_pk
    primary key,
    order_id       varchar(32),
    spu_id         varchar(32),
    sku_id         varchar(32),
    quantity       bigint,
    type           integer,
    unit           integer,
    status         integer,
    active_message varchar(128),
    payable_amount bigint,
    remark         varchar(128),
    active_time    timestamp(0),
    create_time    timestamp(0),
    update_time    timestamp(0),
    bind_store_no  bigint
    );

create table if not exists m_office_refund
(
    id             varchar(32) not null
    constraint mm_office_order_refund_pkey
    primary key,
    merchant_no    bigint,
    store_no       bigint,
    account_id     varchar(32),
    order_id       varchar(32),
    refund_no      varchar(32),
    refund_amount  bigint,
    refund_type    integer,
    status         integer,
    refund_message varchar(256),
    refund_time    timestamp(0),
    remark         varchar(256),
    create_time    timestamp(0),
    update_time    timestamp(0),
    trade_no       varchar(64),
    config_id      varchar(32)
    );

create table if not exists m_office_pay_channel
(
    id         varchar(32) not null
    constraint mm_office_pay_channel_pkey
    primary key,
    code       varchar(32),
    name       varchar(32),
    config_str jsonb
    );

create table if not exists m_office_trade_log
(
    id          varchar(32) not null
    constraint mm_office_trade_pkey
    primary key,
    trade_no    varchar(32),
    pay_type    integer,
    trade_type  varchar(32),
    pay_params  varchar(4096),
    pay_result  varchar(4096),
    remark      varchar(1024),
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_office_trade_order
(
    id           varchar(32) not null
    constraint mm_office_trade_order_pkey
    primary key,
    out_trade_no varchar(32),
    trade_no     varchar(64),
    pay_channel  integer,
    type         integer,
    total_amount bigint,
    app_id       varchar(64),
    config_id    varchar(32),
    config_str   jsonb,
    status       integer,
    message      varchar(1024),
    trade_time   timestamp(0),
    create_time  timestamp(0),
    update_time  timestamp(0)
    );

create table if not exists m_print_channel
(
    id           varchar(32) not null
    constraint m_print_channel_pk
    primary key,
    channel_code varchar(32),
    channel_name varchar(32),
    config_str   jsonb,
    status       integer,
    create_time  timestamp(0),
    update_time  timestamp(0),
    form_config  jsonb
    );

create table if not exists m_print_record
(
    id          varchar(32) not null
    constraint m_print_record_pk
    primary key,
    merchant_no bigint,
    store_no    bigint,
    template_id varchar(32),
    terminal_id varchar(32),
    content     text,
    order_no    varchar(32),
    status      integer,
    remark      varchar(256),
    create_time timestamp(0),
    update_time timestamp(0),
    print_type  integer
    );

create table if not exists m_print_template
(
    id          varchar(32) not null
    constraint m_print_template_pk
    primary key,
    merchant_no bigint,
    classify    integer,
    template    jsonb,
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0),
    type        integer
    );

create table if not exists m_print_terminal
(
    id            varchar(32) not null
    constraint m_print_terminal_pk
    primary key,
    merchant_no   bigint,
    store_no      bigint,
    classify      integer,
    channel_id    varchar(32),
    name          varchar(32),
    form_config   jsonb,
    width         integer,
    status        integer,
    create_time   timestamp(0),
    update_time   timestamp(0),
    terminal_code varchar(64),
    version       varchar(64),
    bind_types    jsonb
    );

comment on column m_print_terminal.terminal_code is '设备唯一码(每个厂商唯一码不同，根据厂商定义码)';

create table if not exists m_clerk_account
(
    id               varchar(32) not null
    constraint m_clerk_account_pk
    primary key,
    merchant_no      bigint,
    store_no         bigint,
    username         varchar(32),
    password         varchar(512),
    status           integer,
    create_time      timestamp,
    update_time      timestamp,
    name             varchar(32),
    phone            varchar(32),
    email            varchar(32),
    open_id          varchar(32),
    deleted          integer default 0,
    mobile_cash_mode integer
    );

comment on column m_clerk_account.open_id is '小程序openId';

create table if not exists m_clerk_account_login_log
(
    id          varchar(32) not null
    constraint m_clerk_account_login_log_pk
    primary key,
    account_id  varchar(32),
    login_time  timestamp(0),
    ip          varchar(32),
    create_time timestamp(0),
    platform    integer,
    address     varchar(64),
    user_agent  varchar(256),
    type        integer
    );

create table if not exists m_clerk_account_role_map
(
    id          varchar(32) not null
    constraint m_clerk_account_role_map_pk
    primary key,
    account_id  varchar(32),
    role_id     varchar(32),
    create_time timestamp
    );

create table if not exists m_clerk_resource
(
    id           varchar(32) not null
    constraint m_clerk_resource_pk
    primary key,
    pid          varchar(32),
    title        varchar(32),
    name         varchar(32),
    path         varchar(128),
    component    varchar(256),
    permissions  varchar(128),
    require_auth integer,
    type         integer,
    icon         varchar(32),
    sort         integer,
    status       integer,
    create_time  timestamp(0),
    update_time  timestamp(0),
    active_path  varchar(32),
    directory_id varchar(512)
    );

create table if not exists m_clerk_role
(
    id             varchar(32) not null
    constraint m_clerk_role_pk
    primary key,
    name           varchar(32),
    en_name        varchar(32),
    remark         varchar(128),
    merchant_no    bigint,
    default_status integer,
    status         integer,
    create_time    timestamp,
    update_time    timestamp,
    type           integer
    );

create table if not exists m_clerk_role_resource_map
(
    id             varchar(32) not null
    constraint m_clerk_role_resource_map_pk
    primary key,
    role_id        varchar(32),
    resource_id    varchar(32),
    create_time    timestamp(0),
    resource_index integer
    );

create table if not exists m_sms
(
    id          varchar(32) not null
    constraint m_sms_pk
    primary key,
    merchant_no bigint,
    recharge    bigint,
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_sms_channel
(
    id             varchar(32) not null
    constraint m_sms_channel_pk
    primary key,
    channel_code   varchar(32),
    channel_name   varchar(32),
    config_str     jsonb,
    status         integer,
    default_status integer,
    create_time    timestamp(0),
    update_time    timestamp(0),
    sms_type       integer
    );

create table if not exists m_sms_recharge_log
(
    id               varchar(32) not null
    constraint m_sms_recharge_log_pk
    primary key,
    merchant_no      bigint,
    store_no         bigint,
    change_before    bigint,
    change_in        bigint,
    change_after     bigint,
    change_type      integer,
    change_type_desc varchar(128),
    send_id          varchar(64),
    remark           varchar(256),
    create_time      timestamp(0),
    change_status    integer,
    order_no         varchar(32)
    );

create table if not exists m_sms_send
(
    id                  varchar(32) not null
    constraint m_sms_send_pk
    primary key,
    merchant_no         bigint,
    store_no            bigint,
    sign_id             varchar(32),
    temp_id             varchar(32),
    temp_group          integer,
    sms_type            integer,
    temp_type           integer,
    send_message        varchar(1024),
    message_length      integer,
    message_cnt         integer,
    free                integer,
    consume_cnt         integer,
    return_cnt          integer,
    status              integer,
    error_desc          varchar(128),
    send_time           varchar(32),
    sms_channel_id      varchar(32),
    create_time         timestamp(0),
    update_time         timestamp(0),
    message_channel_cnt integer,
    message_params      jsonb
    );

create table if not exists m_sms_send_item
(
    id          varchar(32) not null
    constraint m_sms_send_item_pk
    primary key,
    send_id     varchar(32),
    phone       varchar(32),
    user_id     varchar(32),
    status      integer,
    error_desc  varchar(128),
    create_time timestamp(0),
    update_time timestamp(0),
    content     varchar(4096)
    );

create table if not exists m_sms_sign
(
    id             varchar(32) not null
    constraint m_sms_sign_pk
    primary key,
    merchant_no    bigint,
    sign_name      varchar(64),
    default_status integer,
    status         integer,
    error_desc     varchar(128),
    operation_id   varchar(32),
    operation_time timestamp(0),
    create_time    timestamp(0),
    update_time    timestamp(0)
    );

create table if not exists m_sms_sign_channel_rel
(
    id          varchar(32) not null
    constraint m_sms_sign_channel_rel_pk
    primary key,
    sign_id     varchar(32),
    channel_id  varchar(32),
    sign_code   varchar(128),
    status      integer,
    error_desc  varchar(256),
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_sms_status_change
(
    id            varchar(32) not null
    constraint m_sms_status_change_pk
    primary key,
    send_id       varchar(32),
    before_status integer,
    change_status integer,
    after_status  integer,
    operation_id  varchar(32),
    change_time   timestamp(0)
    );

create table if not exists m_sms_template
(
    id             varchar(32) not null
    constraint m_sms_template_pk
    primary key,
    merchant_no    bigint,
    temp_group     integer,
    sms_type       integer,
    temp_type      integer,
    template       varchar(1024),
    default_status integer,
    status         integer,
    error_desc     varchar(128),
    operation_id   varchar(32),
    create_time    timestamp(0),
    update_time    timestamp(0),
    temp_name      varchar(128)
    );

create table if not exists m_sms_template_channel_rel
(
    id          varchar(32) not null
    constraint m_sms_template_channel_rel_pk
    primary key,
    temp_id     varchar(32),
    channel_id  varchar(32),
    temp_code   varchar(128),
    status      integer,
    error_desc  varchar(256),
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_stock_allocate
(
    id           varchar(32) not null
    constraint m_stock_allocate_pk
    primary key,
    merchant_no  bigint,
    store_no     bigint,
    allocate_no  varchar(32),
    in_store_no  bigint,
    out_store_no bigint,
    io_type      integer,
    status       integer,
    operation_id varchar(32),
    create_time  timestamp(0),
    update_time  timestamp(0),
    err_desc     varchar(128),
    remark       varchar(128)
    );

create table if not exists m_stock_allocate_product
(
    id                varchar(32) not null
    constraint m_stock_allocate_product_pk
    primary key,
    allocate_no       varchar(32),
    spu_id            varchar(32),
    sku_id            varchar(32),
    sku_name          varchar(128),
    unit_id           varchar(32),
    unit_name         varchar(32),
    allocate_quantity bigint,
    out_quantity      bigint,
    in_quantity       bigint,
    create_time       timestamp(0),
    update_time       timestamp(0)
    );

create table if not exists m_stock_purchase_order
(
    id                varchar(32) not null
    constraint m_stock_purchase_order_pk
    primary key,
    merchant_no       bigint,
    store_no          bigint,
    purchase_no       varchar(32),
    purchase_store_no bigint,
    status            integer,
    err_desc          varchar(128),
    refund_mark       integer,
    operation_id      varchar(32),
    delivery_time     timestamp(0),
    remark            varchar(256),
    create_time       timestamp(0),
    update_time       timestamp(0)
    );

create table if not exists m_stock_purchase_order_sku
(
    id                 varchar(32) not null
    constraint m_stock_purchase_order_sku_pk
    primary key,
    purchase_id        varchar(32),
    spu_id             varchar(32),
    sku_id             varchar(32),
    purchase_quantity  bigint,
    purchase_amount    bigint,
    refund_quantity    bigint,
    refund_amount      bigint,
    create_time        timestamp(0),
    update_time        timestamp(0),
    replenish_quantity bigint
    );

comment on column m_stock_purchase_order_sku.replenish_quantity is '入库数量';

create table if not exists m_stock_purchase_refund
(
    id           varchar(32) not null
    constraint m_stock_purchase_refund_pk
    primary key,
    merchant_no  bigint,
    store_no     bigint,
    refund_no    varchar(32),
    purchase_id  varchar(32),
    status       integer,
    operation_id varchar(32),
    remark       varchar(256),
    create_time  timestamp(0),
    update_time  timestamp(0),
    err_desc     varchar(128),
    reason_id    varchar(32)
    );

comment on column m_stock_purchase_refund.reason_id is '退货原因';

create table if not exists m_stock_purchase_refund_sku
(
    id              varchar(32) not null
    constraint m_stock_purchase_refund_sku_pk
    primary key,
    refund_id       varchar(32),
    spu_id          varchar(32),
    sku_id          varchar(32),
    refund_quantity bigint,
    refund_amount   bigint,
    create_time     timestamp(0),
    update_time     timestamp(0)
    );

create table if not exists m_stock_record
(
    id             varchar(32) not null
    constraint m_stock_record_pk
    primary key,
    merchant_no    bigint,
    store_no       bigint,
    record_no      varchar(32),
    operation_time timestamp(0),
    type           integer,
    reason_id      varchar(32),
    status         integer,
    operation_id   varchar(32),
    create_time    timestamp(0),
    update_time    timestamp(0),
    join_no        varchar(32),
    io_type        integer,
    reason_record  jsonb
    );

create table if not exists m_stock_record_reason
(
    id          varchar(32) not null
    constraint m_stock_record_reason_pk
    primary key,
    merchant_no bigint,
    reason_name varchar(32),
    io_type     integer,
    remark      varchar(256),
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists m_stock_record_sku
(
    id              varchar(32) not null
    constraint m_stock_record_sku_pk
    primary key,
    record_no       varchar(32),
    spu_id          varchar(32),
    sku_id          varchar(32),
    record_quantity bigint,
    record_amount   bigint,
    create_time     timestamp(0),
    update_time     timestamp(0),
    sku_name        varchar(64)
    );

create table if not exists m_stock_sku_lock
(
    id          varchar(32) not null
    constraint m_stock_sku_lock_pk
    primary key,
    spu_id      varchar(32),
    sku_id      varchar(32),
    merchant_no bigint,
    store_no    bigint,
    order_id    varchar(32),
    lock_stock  bigint,
    create_time timestamp(0)
    );

create table if not exists op_account
(
    id          varchar(32) not null
    constraint op_account_pk
    primary key,
    phone       varchar(20),
    name        varchar(32),
    password    varchar(64),
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0)
    );

create table if not exists op_invite_code
(
    id          varchar(32) not null
    constraint op_invite_code_pk
    primary key,
    merchant_no bigint,
    account_id  varchar(32),
    code        varchar(32),
    url         varchar(512),
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0),
    link        varchar(512)
    );

create table if not exists op_invite_code_record
(
    id             varchar(32) not null
    constraint op_invite_code_record_pk
    primary key,
    invite_code_id varchar(32),
    invite_code    varchar(32),
    url            varchar(512),
    ip             varchar(128),
    create_time    timestamp(0),
    domain         varchar(256),
    refer          varchar(256),
    useragent      varchar(256)
    );

create table if not exists op_invite_current_code
(
    code varchar(32) not null
    constraint op_invite_current_code_pk
    primary key
    );

create table if not exists m_cashier_order
(
    id                  varchar(32) not null
    primary key,
    merchant_no         bigint,
    store_no            bigint,
    order_no            varchar(64),
    type                integer,
    subject             varchar(64),
    original_amount     bigint,
    ignore_type         integer,
    ignore_amount       bigint,
    discount            bigint,
    discount_amount     bigint,
    preferential_amount bigint,
    collect_amount      bigint,
    adjust_amount       bigint,
    payable_amount      bigint,
    reality_amount      bigint,
    pay_type            jsonb,
    status              integer,
    message             varchar(128),
    refund_mark         integer,
    refund_amount       bigint,
    member_id           varchar(32),
    send_sms            integer,
    operation_id        varchar(32),
    service_account_ids jsonb,
    source              integer,
    remark              varchar(128),
    create_time         timestamp(0),
    update_time         timestamp(0),
    category            integer,
    cashier_time        timestamp(0),
    deleted             integer default 0,
    customer_no         varchar(32),
    integral            bigint  default 0,
    counted_total       bigint  default 0,
    refund_integral     bigint  default 0
    );

comment on column m_cashier_order.cashier_time is '销售日期';

comment on column m_cashier_order.deleted is '是否删除';

comment on column m_cashier_order.customer_no is '自定义单号';

comment on column m_cashier_order.integral is '订单积分';

comment on column m_cashier_order.counted_total is '计次核销次数';

comment on column m_cashier_order.refund_integral is '已退积分';

create table if not exists m_cashier_order_pay
(
    id             varchar(32) not null
    primary key,
    order_id       varchar(32),
    pay_code       integer,
    payable_amount bigint,
    reality_amount bigint,
    auth_code      varchar(32),
    trade_no       varchar(64),
    trade_time     timestamp(0),
    status         integer,
    message        varchar(128),
    create_time    timestamp(0),
    update_time    timestamp(0),
    counted_id     varchar(32),
    counted_total  bigint default 0
    );

comment on column m_cashier_order_pay.counted_id is '计次卡id';

comment on column m_cashier_order_pay.counted_total is '计次次数';

create table if not exists m_cashier_order_product
(
    id               varchar(32) not null
    primary key,
    order_id         varchar(32),
    spu_id           varchar(32),
    sku_id           varchar(32),
    sku_name         varchar(128),
    sale_quantity    bigint,
    reference_amount bigint,
    adjust_amount    bigint,
    payable_amount   bigint,
    divide_amount    bigint,
    refund_quantity  bigint,
    refund_amount    bigint,
    status           integer,
    create_time      timestamp(0),
    update_time      timestamp(0),
    picture          varchar(256),
    integral         integer default 0
    );

comment on column m_cashier_order_product.integral is '本单积分';

create table if not exists m_cashier_refund
(
    id              varchar(32) not null
    primary key,
    merchant_no     bigint,
    store_no        bigint,
    refund_no       varchar(32),
    order_id        varchar(32),
    subject         varchar(256),
    original_amount bigint,
    adjust_amount   bigint,
    payable_amount  bigint,
    reality_amount  bigint,
    refund_mode     integer,
    pay_type        jsonb,
    status          integer,
    message         varchar(128),
    send_sms        integer,
    member_id       varchar(32),
    operation_id    varchar(32),
    remark          varchar(128),
    create_time     timestamp(0),
    update_time     timestamp(0),
    order_no        varchar(32),
    integral        bigint default 0,
    counted_total   bigint default 0,
    type            integer,
    category        integer
    );

comment on column m_cashier_refund.integral is '退积分';

comment on column m_cashier_refund.counted_total is '退计次次数';

create table if not exists m_cashier_refund_pay
(
    id             varchar(32) not null
    primary key,
    refund_id      varchar(32),
    pay_code       integer,
    payable_amount bigint,
    reality_amount bigint,
    trade_no       varchar(32),
    trade_time     timestamp(0),
    status         integer,
    message        varchar(128),
    create_time    timestamp(0),
    update_time    timestamp(0),
    counted_id     varchar(32),
    counted_total  bigint
    );

create table if not exists m_cashier_refund_product
(
    id               varchar(32) not null
    constraint m_cashier_refund_product_pk
    primary key,
    refund_id        varchar(64),
    order_product_id varchar(32),
    spu_id           varchar(32),
    sku_id           varchar(32),
    sku_name         varchar(128),
    refund_quantity  bigint,
    reference_amount bigint,
    adjust_amount    bigint,
    payable_amount   bigint,
    status           integer,
    create_time      timestamp(0),
    update_time      timestamp(0),
    picture          varchar(256)
    );

comment on column m_cashier_refund_product.picture is '商品图';

create table if not exists m_merchant_store_bind_quota
(
    id              varchar(32) not null
    primary key,
    merchant_no     bigint,
    store_no        bigint,
    before_end_date timestamp(0),
    add_days        bigint,
    after_end_date  timestamp(0),
    create_time     timestamp(0)
    );

create table if not exists m_merchant_region
(
    id        varchar(20),
    pid       varchar(20),
    name      varchar(32),
    code      varchar(20),
    level     integer,
    longitude numeric(11, 6),
    latitude  numeric(11, 6),
    id_path   varchar(128),
    name_path varchar(255)
    );

create table if not exists oauth_client_details
(
    client_id               varchar(256) not null
    primary key,
    resource_ids            varchar(256),
    client_secret           varchar(256),
    scope                   varchar(256),
    authorized_grant_types  varchar(256),
    web_server_redirect_uri varchar(256),
    authorities             varchar(256),
    access_token_validity   integer,
    refresh_token_validity  integer,
    additional_information  varchar(4096),
    autoapprove             varchar(256)
    );

create table if not exists m_office_industry
(
    id          varchar(32),
    name        varchar(32),
    type        integer,
    status      integer,
    create_time timestamp(0),
    quantity    bigint,
    unit        integer
    );

create table if not exists m_office_package_sku
(
    id              varchar(32),
    spu_id          varchar(32),
    industry_id     varchar(32),
    quantity        bigint,
    unit            integer,
    name            varchar(128),
    now_amount      bigint,
    original_amount bigint,
    sort            integer,
    unit_price_desc varchar(32),
    quantity_desc   varchar(32),
    type            integer,
    specs           varchar(256)
    );

create table if not exists m_office_package_spec
(
    id     varchar(32),
    spu_id varchar(32),
    name   varchar(32),
    sort   integer
    );

create table if not exists m_office_package_spec_values
(
    id               varchar(32),
    spec_id          varchar(32),
    name             varchar(32),
    sort             integer,
    discount_desc    varchar(64),
    unit_amount_desc varchar(20)
    );

create table if not exists m_office_package_spu
(
    id              varchar(32),
    name            varchar(32),
    status          integer,
    check_stock     integer,
    stock           bigint,
    unit_price_desc varchar(32),
    ability_desc    jsonb,
    remark          varchar(128),
    sort            integer,
    "desc"          varchar(128),
    type            integer
    );

create table if not exists m_clerk_handover
(
    id            varchar(32) not null
    constraint m_clerk_handover_pk
    primary key,
    merchant_no   bigint,
    store_no      bigint,
    account_id    varchar(32),
    type          integer,
    handover_time timestamp(0)
    );

create table if not exists m_stock_setting
(
    merchant_no             bigint not null
    constraint m_stock_setting_pk
    primary key,
    purchase_order_examine  integer,
    purchase_refund_examine integer,
    allocate_examine        integer,
    claim                   integer
);

comment on column m_stock_setting.purchase_order_examine is '采购订单审核';

comment on column m_stock_setting.purchase_refund_examine is '采购退货审核';

comment on column m_stock_setting.allocate_examine is '调拨审核';

comment on column m_stock_setting.claim is '是否自动拆包';

create table if not exists m_leaf_code
(
    merchant_no    bigint not null
    constraint m_leaf_code_pk
    primary key,
    goods_spu_code bigint default 0,
    goods_sku_code bigint default 0,
    docket_no      bigint default 0
);

comment on column m_leaf_code.goods_spu_code is '商品spu编码';

comment on column m_leaf_code.goods_sku_code is '商品sku编码';

comment on column m_leaf_code.docket_no is '单据编号';

create table if not exists m_member_recharge_order
(
    id              varchar(32) not null
    constraint m_member_recharge_order_pk
    primary key,
    merchant_no     bigint,
    store_no        bigint,
    member_id       varchar(32),
    order_no        varchar(32),
    rule_id         varchar(32),
    quantity        bigint,
    recharge_amount bigint,
    give_amount     bigint,
    receives_amount bigint,
    give_integral   bigint,
    refunded        integer,
    recharge_time   timestamp(0),
    refund_time     timestamp(0)
    );

create table if not exists m_summary_cashier
(
    id             varchar(32) not null
    constraint m_summary_cashier_pk
    primary key,
    merchant_no    bigint,
    store_no       bigint,
    summary_date   timestamp(6),
    cashier_total  integer,
    cashier_amount bigint,
    refund_amount  bigint,
    create_time    timestamp(0)
    );

comment on column m_summary_cashier.cashier_total is '销售单数';

comment on column m_summary_cashier.cashier_amount is '销售总额';

comment on column m_summary_cashier.refund_amount is '退款金额';

create table if not exists m_trade
(
    id               varchar(32) not null
    constraint m_trade_pk
    primary key,
    merchant_no      bigint,
    store_no         bigint,
    trade_no         varchar(32),
    channel_id       varchar(32),
    pay_mode         integer,
    pay_way          integer,
    order_no         varchar(32),
    order_subject    varchar(64),
    order_amount     bigint,
    channel_trade_no varchar(32),
    bank_order_no    varchar(32),
    bank_trade_no    varchar(32),
    member_id        varchar(32),
    auth_code        varchar(32),
    success_time     timestamp(0),
    buyer_pay_amount bigint,
    status           integer,
    describe         varchar(128),
    refund           integer,
    refund_amount    integer,
    create_time      timestamp(0)
    );

create table if not exists m_trade_refund
(
    id                  varchar(32) not null
    constraint m_trade_refund_pk
    primary key,
    merchant_no         bigint,
    store_no            bigint,
    trade_no            varchar(32),
    refund_trade_no     integer,
    refund_subject      varchar(64),
    refund_amount       bigint,
    channel_id          varchar(32),
    channel_trade_no    varchar(32),
    bank_order_no       varchar(64),
    bank_trade_no       varchar(32),
    pay_way             integer,
    status              integer,
    refund_success_time integer,
    describe            varchar(128),
    create_time         timestamp(0)
    );

create table if not exists m_trade_channel
(
    id             varchar(32),
    merchant_no    bigint,
    store_no       bigint,
    channel_code   varchar(32),
    channel_name   varchar(32),
    channel_config jsonb,
    status         integer,
    create_time    timestamp(0),
    deleted        integer default 0
    );

create table if not exists m_stock_claim
(
    id             varchar(32) not null
    constraint m_stock_claim_pk
    primary key,
    merchant_no    bigint,
    large_spu_id   varchar(32),
    large_spu_no   varchar(32),
    large_spu_code varchar(32),
    large_spu_name varchar(64),
    large_unit_id  varchar(32),
    small_spu_id   varchar(32),
    small_spu_no   varchar(32),
    small_spu_code varchar(32),
    small_spu_name varchar(64),
    small_unit_id  varchar(32),
    multiple       bigint,
    status         integer,
    create_time    timestamp(0),
    update_time    timestamp(0)
    );

create table if not exists m_stock_claim_sku
(
    id             varchar(32) not null
    constraint m_stock_claim_sku_pk
    primary key,
    split_id       varchar(32),
    large_spu_id   varchar(32),
    large_sku_id   varchar(32),
    large_sku_no   varchar(32),
    large_sku_code varchar(32),
    large_sku_name varchar(64),
    small_spu_id   varchar(32),
    small_sku_id   varchar(32),
    small_sku_no   varchar(32),
    small_sku_code varchar(32),
    small_sku_name varchar(64)
    );

create table if not exists m_print_channel_map
(
    id         varchar(32) not null
    constraint m_print_channel_map_pk
    primary key,
    classify   integer,
    channel_id varchar(32)
    );

create table if not exists m_stock_inventory
(
    id                   varchar(32) not null
    constraint m_stock_inventory_pk
    primary key,
    merchant_no          bigint,
    store_no             bigint,
    inventory_no         varchar(32),
    range                integer,
    categories           jsonb,
    inventory_start_time timestamp(0),
    inventory_end_time   timestamp(0),
    product_num          bigint,
    error_product_num    bigint,
    operation_id         varchar(32),
    remark               varchar(128),
    status               integer,
    create_time          timestamp(0),
    update_time          timestamp(0)
    );

create table if not exists m_stock_inventory_product
(
    id            varchar(32) not null
    constraint m_stock_inventory_product_pk
    primary key,
    inventory_id  varchar(32),
    spu_id        varchar(32),
    spu_name      varchar(32),
    category_id   varchar(32),
    category_name varchar(32),
    before_stock  bigint,
    reality_stock bigint,
    phase_stock   bigint,
    create_time   timestamp(0),
    update_time   timestamp(0)
    );

create table if not exists m_clerk_commission_rule
(
    id          varchar(32) not null
    constraint m_clerk_commission_rule_pk
    primary key,
    merchant_no bigint,
    type        integer,
    mode        integer,
    unit        integer,
    rate        integer,
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0),
    deleted     integer default 0
    );

create table if not exists m_clerk_commission
(
    id                 varchar(32) not null
    constraint m_clerk_commission_pk
    primary key,
    merchant_no        bigint,
    store_no           bigint,
    commission_time    timestamp(0),
    account_id         varchar(32),
    commission_rule_id varchar(32),
    total_amount       bigint,
    cashier_amount     bigint,
    recharge_amount    bigint,
    counted_amount     bigint,
    service_amount     bigint,
    create_time        timestamp(0),
    update_time        timestamp(0)
    );

create table if not exists m_summary_account
(
    id              varchar(32) not null
    constraint m_summary_account_pk
    primary key,
    merchant_no     bigint,
    store_no        bigint,
    summary_date    timestamp(0),
    account_id      varchar(32),
    total_amount    bigint,
    cashier_amount  bigint,
    recharge_amount bigint,
    counted_amount  bigint,
    service_amount  bigint,
    create_time     timestamp(0),
    update_time     timestamp(0)
    );

create table if not exists m_summary_store
(
    id              varchar(32) not null
    constraint m_summary_store_pk
    primary key,
    merchant_no     bigint,
    store_no        bigint,
    summary_date    timestamp(0),
    total_amount    bigint,
    cashier_amount  bigint,
    recharge_amount bigint,
    counted_amount  bigint,
    service_amount  bigint,
    create_time     timestamp(0),
    update_time     timestamp(0)
    );

create table if not exists m_sms_template_setting
(
    id          varchar(32) not null
    constraint m_sms_template_setting_pk
    primary key,
    merchant_no bigint,
    temp_type   integer,
    temp_id     varchar(32),
    status      integer
    );

create table if not exists m_clerk_account_scan
(
    id            varchar(32) not null
    constraint m_clerk_account_scan_pk
    primary key,
    scan_no       varchar(32),
    login_info    jsonb,
    source        integer,
    status        integer,
    expire_time   bigint,
    create_time   timestamp(0),
    update_time   timestamp(0),
    grant_type    varchar(32),
    client_id     varchar(32),
    client_secret varchar(32)
    );

comment on column m_clerk_account_scan.scan_no is '扫码编号';

comment on column m_clerk_account_scan.login_info is '登录信息';

comment on column m_clerk_account_scan.source is '扫码场景';

comment on column m_clerk_account_scan.status is '状态,1:创建，2:已扫码，3:已过期';

comment on column m_clerk_account_scan.expire_time is '13位时间戳过期时间';

comment on column m_clerk_account_scan.grant_type is '授权类型';

comment on column m_clerk_account_scan.client_id is '客户端id';

comment on column m_clerk_account_scan.client_secret is '客户端秘钥';

create table if not exists m_stock_spu
(
    id           varchar(32) not null
    constraint m_stock_spu_pk
    primary key
    constraint m_stock_spu_pk_2
    unique,
    merchant_no  bigint,
    store_no     bigint,
    spu_id       varchar(32),
    category_id  varchar(32),
    spu_code     varchar(32),
    spu_no       varchar(32),
    many_code    varchar(256),
    name         varchar(32),
    unit_id      varchar(32),
    pictures     jsonb,
    counted_type integer,
    remark       varchar(128),
    status       integer,
    deleted      integer default 0,
    create_time  timestamp(0),
    update_time  timestamp(0),
    goods_status integer
    );

comment on column m_stock_spu.goods_status is '商品库商品状态';

create table if not exists m_stock_sku
(
    id               varchar(32) not null
    constraint m_stock_sku_pk
    primary key,
    stock_spu_id     varchar(32),
    merchant_no      bigint,
    store_no         bigint,
    spu_id           varchar(32),
    sku_id           varchar(32),
    sku_code         varchar(32),
    sku_no           varchar(32),
    sku_name         varchar(32),
    purchase_amount  bigint,
    reference_amount bigint,
    sku_weight       bigint,
    sell_stock       bigint,
    status           integer,
    deleted          integer default 0,
    create_time      timestamp(0),
    update_time      timestamp(0),
    join_spec        varchar(2048)
    );

create table if not exists m_document_resource
(
    id          varchar(32) not null
    constraint m_document_resource_pk
    primary key,
    pid         varchar(32),
    name        varchar(128),
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0),
    sort        integer,
    deleted     integer default 0
    );

comment on column m_document_resource.name is '标题';

comment on column m_document_resource.status is '状态(启用，禁用)';

create table if not exists m_document_help
(
    id          varchar(20) not null
    constraint m_document_help_pk
    primary key,
    resource_id varchar(20),
    code        varchar(32),
    title       varchar(128),
    content     text,
    status      integer,
    create_time timestamp(0),
    update_time timestamp(0),
    sort        integer default 0,
    deleted     integer default 0
    );

comment on column m_document_help.sort is '排序';

create table if not exists m_leaf_config
(
    id                varchar(32),
    wechat_app_id     varchar(32),
    wechat_app_secret varchar(256)
    );

create table if not exists m_cashier_setting
(
    id               varchar(32),
    merchant_no      bigint,
    store_no         bigint,
    account_id       varchar(32),
    mobile_cash_mode integer
    );

create table if not exists m_biz_action_log
(
    id          varchar(32),
    merchant_no bigint,
    store_no    bigint,
    account_id  varchar(32),
    position    integer,
    event       varchar(128),
    create_time timestamp(0),
    source      integer
    );