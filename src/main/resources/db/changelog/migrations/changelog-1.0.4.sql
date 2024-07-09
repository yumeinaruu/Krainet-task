--liquibase formatted sql

--changeset yumeinaruu:4
--comment table security created
create table public.security
(
    id       bigserial
        constraint security_pk
            primary key,
    login    varchar not null,
    password varchar not null,
    role     varchar not null,
    user_id  bigint  not null
        constraint security_users_id_fk
            references public.users
            on update cascade on delete cascade
);

alter table public.security
    owner to postgres;

