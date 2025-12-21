create table tb_users (
    id bigserial primary key,
    name varchar(100) not null,
    username varchar(32) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    is_active boolean not null default false,
    deleted_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz,
    updated_by bigint,

    constraint uq_tb_users_username unique (username),
    constraint uq_tb_users_email unique (email),
    constraint fk_tb_users_updated_by foreign key (updated_by)
        references tb_users (id)
        on delete set null
);


create table tb_profiles (
    id bigserial primary key,
    name varchar(50) not null,
    description text not null,

    constraint uq_tb_profiles_name unique (name)
);


create table tb_tags (
    id bigserial primary key,
    name varchar(50) not null,

    constraint uq_tb_tags_name unique (name)
);


create table tb_posts (
    id bigserial primary key,
    title varchar(100) not null,
    content text not null,
    image_url varchar(255),
    author_id bigint not null,
    deleted_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz,
    updated_by bigint,

    constraint fk_tb_posts_author_id foreign key (author_id)
        references tb_users (id)
        on delete cascade,

    constraint fk_tb_posts_updated_by foreign key (updated_by)
        references tb_users (id)
        on delete set null
);


create table tb_replies (
    id bigserial primary key,
    content text not null,
    post_id bigint not null,
    author_id bigint not null,
    deleted_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz,
    updated_by bigint,

    constraint fk_tb_replies_post_id foreign key (post_id)
        references tb_posts (id)
        on delete cascade,

    constraint fk_tb_replies_author_id foreign key (author_id)
        references tb_users (id)
        on delete cascade,

    constraint fk_tb_replies_updated_by foreign key (updated_by)
        references tb_users (id)
        on delete set null
);


create table tb_validation_codes (
    id bigserial primary key,
    code char(6) not null,
    user_id bigint not null,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,
    validated_at timestamptz,

    constraint uq_tb_validation_codes_user_code unique (user_id, code),
    constraint ck_tb_validation_codes_expiration check (expires_at > created_at),

    constraint fk_tb_validation_codes_user_id foreign key (user_id)
        references tb_users (id)
        on delete cascade
);


create table tb_user_profile (
    user_id bigint not null,
    profile_id bigint not null,

    constraint pk_tb_user_profile primary key (user_id, profile_id),

    constraint fk_tb_user_profile_user_id foreign key (user_id)
        references tb_users (id)
        on delete cascade,

    constraint fk_tb_user_profile_profile_id foreign key (profile_id)
        references tb_profiles (id)
        on delete cascade
);


create table tb_post_tag (
    post_id bigint not null,
    tag_id bigint not null,

    constraint pk_tb_post_tag primary key (post_id, tag_id),

    constraint fk_tb_post_tag_post_id foreign key (post_id)
        references tb_posts (id)
        on delete cascade,

    constraint fk_tb_post_tag_tag_id foreign key (tag_id)
        references tb_tags (id)
        on delete cascade
);


create table tb_refresh_tokens (
    id bigserial primary key,
    user_id bigint not null,
    token varchar(255) not null unique,
    expires_at timestamptz not null,
    created_at timestamptz not null default now(),

    constraint uq_tb_refresh_tokens_user unique (user_id),
    constraint ck_tb_refresh_tokens_expiration check (expires_at > created_at),

    constraint fk_tb_refresh_tokens_user_id foreign key (user_id)
        references tb_users (id)
        on delete cascade
);


insert into tb_tags (name) values
    ('TECHNOLOGY'),
    ('FOOD'),
    ('TRAVEL'),
    ('NEWS'),
    ('ENTERTAINMENT'),
    ('SPORTS'),
    ('FUN'),
    ('HEALTH'),
    ('EDUCATION'),
    ('BUSINESS'),
    ('CULTURE');


insert into tb_profiles (name, description) values
    ('REGULAR', 'Can operate over its own resources'),
    ('ADMIN', 'Can operate over all posts');

