create table tb_refresh_tokens (
    id bigserial primary key,
    user_id bigint not null,
    token varchar(255) not null unique,
    expires_at timestamptz not null,
    created_at timestamptz not null default now(),

    constraint uq_tb_refresh_tokens_user unique (user_id),
    constraint fk_tb_tb_refresh_tokens_user_id foreign key (user_id) references tb_users (id) on delete cascade,
    constraint ck_tb_refresh_tokens_expiration check (expires_at > created_at)
);