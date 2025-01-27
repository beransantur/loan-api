create table if not exists authority
(
    id          int auto_increment
    primary key,
    created_at  datetime(6)  null,
    modified_at datetime(6)  null,
    description varchar(255) not null,
    name        varchar(255) not null
    );

create table if not exists role
(
    id          int auto_increment
    primary key,
    created_at  datetime(6)  null,
    modified_at datetime(6)  null,
    description varchar(255) not null,
    name        varchar(255) not null
    );

create table if not exists role_authority
(
    authority_id int not null,
    role_id      int not null,
    constraint FK2052966dco7y9f97s1a824bj1
    foreign key (role_id) references role (id),
    constraint FKqbri833f7xop13bvdje3xxtnw
    foreign key (authority_id) references authority (id)
    );

create table if not exists user
(
    id          int auto_increment
    primary key,
    created_at  datetime(6)  null,
    modified_at datetime(6)  null,
    name        varchar(255) not null,
    password    varchar(255) not null,
    surname     varchar(255) not null,
    username    varchar(255) not null,
    constraint UKsb8bbouer5wak8vyiiy4pf2bx
    unique (username)
    );

create table if not exists customer
(
    credit_limit      decimal(38, 2) null,
    id                int auto_increment
    primary key,
    used_credit_limit decimal(38, 2) null,
    user_id           int            null,
    constraint UKj7ja2xvrxudhvssosd4nu1o92
    unique (user_id),
    constraint FKj8dlm21j202cadsbfkoem0s58
    foreign key (user_id) references user (id)
    );

create table if not exists loan
(
    amount             decimal(38, 2) not null,
    created_at         date           not null,
    customer_id        int            not null,
    id                 int auto_increment
    primary key,
    installment_number int            not null,
    is_paid            bit            null,
    constraint FKcwv05agfqwg5ndy6adbefsx7d
    foreign key (customer_id) references customer (id)
    );

create table if not exists installment
(
    amount       decimal(38, 2) not null,
    due_date     date           not null,
    id           int auto_increment
    primary key,
    is_paid      bit            null,
    loan_id      int            not null,
    paid_amount  decimal(38, 2) null,
    payment_date date           null,
    created_at   datetime(6)    null,
    modified_at  datetime(6)    null,
    constraint FKddvr1rongdlfl3pmj87eg48cy
    foreign key (loan_id) references loan (id)
    );

create table if not exists user_role
(
    role_id int not null,
    user_id int not null,
    constraint FK859n2jvi8ivhui0rl0esws6o
    foreign key (user_id) references user (id),
    constraint FKa68196081fvovjhkek5m97n3y
    foreign key (role_id) references role (id)
    );