

create table meeting_provider
(
    id   varchar(255) not null
        constraint meeting_provider_pkey
            primary key,
    deleted               boolean      not null,
    created_by            varchar(255),
    created_date          bigint       not null,
    last_modified_date    bigint       not null,
    updated_by            varchar(255),
    accounts              jsonb,
    conference_type       integer      not null,
    is_active             boolean,
    meeting_provider_type integer      not null,
    name                  varchar(255) not null,
    settings              jsonb,
    user_role_group       varchar(255)
);

alter table meeting_provider
    owner to postgres;

create table meeting
(
    id                  varchar(255) not null
        constraint meeting_pkey
            primary key,
    deleted             boolean      not null,
    created_by          varchar(255),
    created_date        bigint       not null,
    last_modified_date  bigint       not null,
    updated_by          varchar(255),
    calendar_event_id   varchar(255),
    description         varchar(255),
    end_date            bigint       not null,
    event_id            varchar(255),
    meetingurl          varchar(255),
    organizer           varchar(255) not null,
    provider_account    varchar(255) not null,
    start_date          bigint       not null,
    title               varchar(255),
    meeting_provider_id varchar(255) not null
        constraint fkjkgxrtrc8rptecmwe6u8fb52x
            references meeting_provider
);

alter table meeting
    owner to postgres;

create table recipient
(
    id             varchar(255) not null
        constraint recipient_pkey
            primary key,
    deleted        boolean      not null,
    email_received boolean,
    name           varchar(255) not null,
    meeting_id     varchar(255) not null
        constraint fkinmmrd3375xs0n90op5i8ep6v
            references meeting
);

alter table recipient
    owner to postgres;

create table slot_request
(
    id                 varchar(255) not null
        constraint slot_request_pkey
            primary key,
    deleted            boolean      not null,
    created_by         varchar(255),
    created_date       bigint       not null,
    last_modified_date bigint       not null,
    updated_by         varchar(255),
    creator            varchar(255),
    description        varchar(255),
    end_date           bigint       not null,
    organizer          varchar(255),
    request_status     integer,
    start_date         bigint       not null,
    title              varchar(255),
    meeting_id         varchar(255) not null
        constraint fknsyh9yd8jwmsrvnbajtyp82cx
            references meeting
);

alter table slot_request
    owner to postgres;

-- create table zoom_account
-- (
--     id               varchar(255) not null
--         constraint zoom_account_pkey
--             primary key,
--     account_mail     varchar(255) not null,
--     api_key          varchar(255) not null,
--     api_secret       varchar(255) not null,
--     application_name varchar(255) not null
-- );

-- alter table zoom_account
--     owner to postgres;

